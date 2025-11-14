package com.mojang.brigadier;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/CommandDispatcher.class */
public class CommandDispatcher<S> {
    public static final String ARGUMENT_SEPARATOR = " ";
    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';
    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";
    private final RootCommandNode<S> root;
    private final Predicate<CommandNode<S>> hasCommand;
    private ResultConsumer<S> consumer;

    public CommandDispatcher(RootCommandNode<S> root) {
        this.hasCommand = new Predicate<CommandNode<S>>() { // from class: com.mojang.brigadier.CommandDispatcher.1
            @Override // java.util.function.Predicate
            public boolean test(CommandNode<S> input) {
                return input != null && (input.getCommand() != null || input.getChildren().stream().anyMatch(CommandDispatcher.this.hasCommand));
            }
        };
        this.consumer = (c, s, r) -> {
        };
        this.root = root;
    }

    public CommandDispatcher() {
        this(new RootCommandNode());
    }

    public LiteralCommandNode<S> register(LiteralArgumentBuilder<S> command) {
        LiteralCommandNode<S> build = (LiteralCommandNode<S>) command.build();
        this.root.addChild(build);
        return build;
    }

    public void setConsumer(ResultConsumer<S> consumer) {
        this.consumer = consumer;
    }

    public int execute(String input, S source) throws CommandSyntaxException {
        return execute(new StringReader(input), (S) source);
    }

    public int execute(StringReader input, S source) throws CommandSyntaxException {
        ParseResults<S> parse = parse(input, (S) source);
        return execute(parse);
    }

    public int execute(ParseResults<S> parse) throws CommandSyntaxException {
        if (parse.getReader().canRead()) {
            if (parse.getExceptions().size() == 1) {
                throw parse.getExceptions().values().iterator().next();
            }
            if (parse.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
            }
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
        }
        int result = 0;
        int successfulForks = 0;
        boolean forked = false;
        boolean foundCommand = false;
        String command = parse.getReader().getString();
        CommandContext<S> original = parse.getContext().build(command);
        List<CommandContext<S>> listSingletonList = Collections.singletonList(original);
        while (true) {
            List<CommandContext<S>> contexts = listSingletonList;
            ArrayList<CommandContext<S>> next = null;
            if (contexts != null) {
                int size = contexts.size();
                for (int i = 0; i < size; i++) {
                    CommandContext<S> context = contexts.get(i);
                    CommandContext<S> child = context.getChild();
                    if (child != null) {
                        forked |= context.isForked();
                        if (child.hasNodes()) {
                            foundCommand = true;
                            RedirectModifier<S> modifier = context.getRedirectModifier();
                            if (modifier == null) {
                                if (next == null) {
                                    next = new ArrayList<>(1);
                                }
                                next.add(child.copyFor(context.getSource()));
                            } else {
                                try {
                                    Collection<S> results = modifier.apply(context);
                                    if (!results.isEmpty()) {
                                        if (next == null) {
                                            next = new ArrayList<>(results.size());
                                        }
                                        for (S source : results) {
                                            next.add(child.copyFor(source));
                                        }
                                    }
                                } catch (CommandSyntaxException ex) {
                                    this.consumer.onCommandComplete(context, false, 0);
                                    if (!forked) {
                                        throw ex;
                                    }
                                }
                            }
                        } else {
                            continue;
                        }
                    } else if (context.getCommand() != null) {
                        foundCommand = true;
                        try {
                            int value = context.getCommand().run(context);
                            result += value;
                            this.consumer.onCommandComplete(context, true, value);
                            successfulForks++;
                        } catch (CommandSyntaxException ex2) {
                            this.consumer.onCommandComplete(context, false, 0);
                            if (!forked) {
                                throw ex2;
                            }
                        }
                    } else {
                        continue;
                    }
                }
                listSingletonList = next;
            } else {
                if (foundCommand) {
                    return forked ? successfulForks : result;
                }
                this.consumer.onCommandComplete(original, false, 0);
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
            }
        }
    }

    public ParseResults<S> parse(String command, S source) throws CommandSyntaxException {
        return parse(new StringReader(command), (S) source);
    }

    public ParseResults<S> parse(StringReader command, S source) throws CommandSyntaxException {
        CommandContextBuilder<S> context = new CommandContextBuilder<>(this, source, this.root, command.getCursor());
        return parseNodes(this.root, command, context);
    }

    private ParseResults<S> parseNodes(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar) throws CommandSyntaxException {
        S source = contextSoFar.getSource();
        Map<CommandNode<S>, CommandSyntaxException> errors = null;
        List<ParseResults<S>> potentials = null;
        int cursor = originalReader.getCursor();
        for (CommandNode<S> child : node.getRelevantNodes(originalReader)) {
            if (child.canUse(source)) {
                CommandContextBuilder<S> context = contextSoFar.copy();
                StringReader reader = new StringReader(originalReader);
                try {
                    try {
                        child.parse(reader, context);
                        if (reader.canRead() && reader.peek() != ' ') {
                            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
                        }
                        context.withCommand(child.getCommand());
                        if (reader.canRead(child.getRedirect() == null ? 2 : 1)) {
                            reader.skip();
                            if (child.getRedirect() != null) {
                                CommandContextBuilder<S> childContext = new CommandContextBuilder<>(this, source, child.getRedirect(), reader.getCursor());
                                ParseResults<S> parse = parseNodes(child.getRedirect(), reader, childContext);
                                context.withChild(parse.getContext());
                                return new ParseResults<>(context, parse.getReader(), parse.getExceptions());
                            }
                            ParseResults<S> parse2 = parseNodes(child, reader, context);
                            if (potentials == null) {
                                potentials = new ArrayList<>(1);
                            }
                            potentials.add(parse2);
                        } else {
                            if (potentials == null) {
                                potentials = new ArrayList<>(1);
                            }
                            potentials.add(new ParseResults<>(context, reader, Collections.emptyMap()));
                        }
                    } catch (RuntimeException ex) {
                        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, ex.getMessage());
                    }
                } catch (CommandSyntaxException ex2) {
                    if (errors == null) {
                        errors = new LinkedHashMap<>();
                    }
                    errors.put(child, ex2);
                    reader.setCursor(cursor);
                }
            }
        }
        if (potentials != null) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return -1;
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return 1;
                    }
                    if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return potentials.get(0);
        }
        return new ParseResults<>(contextSoFar, originalReader, errors == null ? Collections.emptyMap() : errors);
    }

    public String[] getAllUsage(CommandNode<S> node, S source, boolean restricted) {
        ArrayList<String> result = new ArrayList<>();
        getAllUsage(node, source, result, "", restricted);
        return (String[]) result.toArray(new String[result.size()]);
    }

    private void getAllUsage(CommandNode<S> node, S source, ArrayList<String> result, String prefix, boolean restricted) {
        if (restricted && !node.canUse(source)) {
            return;
        }
        if (node.getCommand() != null) {
            result.add(prefix);
        }
        if (node.getRedirect() != null) {
            String redirect = node.getRedirect() == this.root ? "..." : "-> " + node.getRedirect().getUsageText();
            result.add(prefix.isEmpty() ? node.getUsageText() + ARGUMENT_SEPARATOR + redirect : prefix + ARGUMENT_SEPARATOR + redirect);
        } else if (!node.getChildren().isEmpty()) {
            for (CommandNode<S> child : node.getChildren()) {
                getAllUsage(child, source, result, prefix.isEmpty() ? child.getUsageText() : prefix + ARGUMENT_SEPARATOR + child.getUsageText(), restricted);
            }
        }
    }

    public Map<CommandNode<S>, String> getSmartUsage(CommandNode<S> node, S source) {
        Map<CommandNode<S>, String> result = new LinkedHashMap<>();
        boolean optional = node.getCommand() != null;
        for (CommandNode<S> child : node.getChildren()) {
            String usage = getSmartUsage(child, source, optional, false);
            if (usage != null) {
                result.put(child, usage);
            }
        }
        return result;
    }

    private String getSmartUsage(CommandNode<S> node, S source, boolean optional, boolean deep) {
        if (!node.canUse(source)) {
            return null;
        }
        String self = optional ? USAGE_OPTIONAL_OPEN + node.getUsageText() + USAGE_OPTIONAL_CLOSE : node.getUsageText();
        boolean childOptional = node.getCommand() != null;
        String open = childOptional ? USAGE_OPTIONAL_OPEN : USAGE_REQUIRED_OPEN;
        String close = childOptional ? USAGE_OPTIONAL_CLOSE : USAGE_REQUIRED_CLOSE;
        if (!deep) {
            if (node.getRedirect() != null) {
                String redirect = node.getRedirect() == this.root ? "..." : "-> " + node.getRedirect().getUsageText();
                return self + ARGUMENT_SEPARATOR + redirect;
            }
            Collection<CommandNode<S>> children = (Collection) node.getChildren().stream().filter(c -> {
                return c.canUse(source);
            }).collect(Collectors.toList());
            if (children.size() == 1) {
                String usage = getSmartUsage(children.iterator().next(), source, childOptional, childOptional);
                if (usage != null) {
                    return self + ARGUMENT_SEPARATOR + usage;
                }
            } else if (children.size() > 1) {
                Set<String> childUsage = new LinkedHashSet<>();
                for (CommandNode<S> child : children) {
                    String usage2 = getSmartUsage(child, source, childOptional, true);
                    if (usage2 != null) {
                        childUsage.add(usage2);
                    }
                }
                if (childUsage.size() == 1) {
                    String usage3 = childUsage.iterator().next();
                    return self + ARGUMENT_SEPARATOR + (childOptional ? USAGE_OPTIONAL_OPEN + usage3 + USAGE_OPTIONAL_CLOSE : usage3);
                }
                if (childUsage.size() > 1) {
                    StringBuilder builder = new StringBuilder(open);
                    int count = 0;
                    for (CommandNode<S> child2 : children) {
                        if (count > 0) {
                            builder.append(USAGE_OR);
                        }
                        builder.append(child2.getUsageText());
                        count++;
                    }
                    if (count > 0) {
                        builder.append(close);
                        return self + ARGUMENT_SEPARATOR + builder.toString();
                    }
                }
            }
        }
        return self;
    }

    public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> parse) {
        return getCompletionSuggestions(parse, parse.getReader().getTotalLength());
    }

    public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> parse, int cursor) {
        CommandContextBuilder<S> context = parse.getContext();
        SuggestionContext<S> nodeBeforeCursor = context.findSuggestionContext(cursor);
        CommandNode<S> parent = nodeBeforeCursor.parent;
        int start = Math.min(nodeBeforeCursor.startPos, cursor);
        String fullInput = parse.getReader().getString();
        String truncatedInput = fullInput.substring(0, cursor);
        String truncatedInputLowerCase = truncatedInput.toLowerCase(Locale.ROOT);
        CompletableFuture<Suggestions>[] futures = new CompletableFuture[parent.getChildren().size()];
        int i = 0;
        for (CommandNode<S> node : parent.getChildren()) {
            CompletableFuture<Suggestions> future = Suggestions.empty();
            try {
                future = node.listSuggestions(context.build(truncatedInput), new SuggestionsBuilder(truncatedInput, truncatedInputLowerCase, start));
            } catch (CommandSyntaxException e) {
            }
            int i2 = i;
            i++;
            futures[i2] = future;
        }
        CompletableFuture<Suggestions> result = new CompletableFuture<>();
        CompletableFuture.allOf(futures).thenRun(() -> {
            ArrayList arrayList = new ArrayList();
            for (CompletableFuture completableFuture : futures) {
                arrayList.add(completableFuture.join());
            }
            result.complete(Suggestions.merge(fullInput, arrayList));
        });
        return result;
    }

    public RootCommandNode<S> getRoot() {
        return this.root;
    }

    public Collection<String> getPath(CommandNode<S> target) {
        List<List<CommandNode<S>>> nodes = new ArrayList<>();
        addPaths(this.root, nodes, new ArrayList());
        for (List<CommandNode<S>> list : nodes) {
            if (list.get(list.size() - 1) == target) {
                List<String> result = new ArrayList<>(list.size());
                for (CommandNode<S> node : list) {
                    if (node != this.root) {
                        result.add(node.getName());
                    }
                }
                return result;
            }
        }
        return Collections.emptyList();
    }

    public CommandNode<S> findNode(Collection<String> path) {
        CommandNode<S> node = this.root;
        for (String name : path) {
            node = node.getChild(name);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    public void findAmbiguities(AmbiguityConsumer<S> consumer) {
        this.root.findAmbiguities(consumer);
    }

    private void addPaths(CommandNode<S> node, List<List<CommandNode<S>>> result, List<CommandNode<S>> parents) {
        List<CommandNode<S>> current = new ArrayList<>(parents);
        current.add(node);
        result.add(current);
        for (CommandNode<S> child : node.getChildren()) {
            addPaths(child, result, current);
        }
    }
}
