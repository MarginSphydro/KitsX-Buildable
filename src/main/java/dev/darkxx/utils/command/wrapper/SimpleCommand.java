package dev.darkxx.utils.command.wrapper;

import dev.darkxx.utils.command.builder.Argument;
import dev.darkxx.utils.command.builder.CommandContext;
import dev.darkxx.utils.command.builder.CommandInfo;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/command/wrapper/SimpleCommand.class */
public abstract class SimpleCommand extends Command {
    private final CommandInfo info;
    private Consumer<CommandContext> executor;
    private Function<CommandContext, List<String>> tabCompleter;
    private final List<Argument<?>> arguments;
    private final Map<String, SimpleSubCommand> subcommands;
    private final Map<Integer, List<TabCommand>> tabComplete;
    private static CommandMap commandMap;

    public abstract void execute(CommandContext commandContext);

    public abstract List<String> suggestions(CommandContext commandContext);

    static {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SimpleCommand(String name, Consumer<CommandInfo> infoConsumer) {
        super(name);
        this.info = new CommandInfo();
        this.arguments = new ArrayList();
        this.subcommands = new HashMap();
        this.tabComplete = new HashMap();
        infoConsumer.accept(this.info);
        setDescription(this.info.getDescription());
        setUsage(this.info.getUsage());
        setAliases(this.info.getAliases());
        setPermission(this.info.getPermission());
        setPermissionMessage(this.info.getPermissionMessage());
    }

    public SimpleCommand executes(Consumer<CommandContext> executor) {
        this.executor = executor;
        return this;
    }

    public SimpleCommand suggests(Function<CommandContext, List<String>> tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    public SimpleCommand withArgument(Argument<?> argument) {
        this.arguments.add(argument);
        return this;
    }

    public SimpleCommand withSubCommand(SimpleSubCommand subCommand) {
        this.subcommands.put(subCommand.getName(), subCommand);
        return this;
    }

    public SimpleCommand withTabComplete(int index, List<TabCommand> commands) {
        this.tabComplete.put(Integer.valueOf(index), commands);
        return this;
    }

    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        CommandContext ctx = new CommandContext(sender, label, args);
        if (args.length > 0 && this.subcommands.containsKey(args[0])) {
            this.subcommands.get(args[0]).execute(ctx);
            return true;
        }
        if (this.executor != null) {
            this.executor.accept(ctx);
            return true;
        }
        sender.sendMessage("Command not implemented.");
        return true;
    }

    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        CommandContext ctx = new CommandContext(sender, alias, args);
        if (args.length > 0 && this.subcommands.containsKey(args[0])) {
            return this.subcommands.get(args[0]).suggestions(ctx);
        }
        if (this.tabCompleter != null) {
            return this.tabCompleter.apply(ctx);
        }
        int index = args.length - 1;
        if ((getPermission() != null && !sender.hasPermission(getPermission())) || this.tabComplete.isEmpty() || !this.tabComplete.containsKey(Integer.valueOf(index))) {
            return super.tabComplete(sender, alias, args);
        }
        List<String> suggestions = (List) this.tabComplete.get(Integer.valueOf(index)).stream().filter(tabCommand -> {
            return tabCommand.getTextAvant() == null || tabCommand.getTextAvant().contains(args[index - 1]);
        }).filter(tabCommand2 -> {
            return tabCommand2.getPermission() == null || sender.hasPermission(tabCommand2.getPermission());
        }).filter(tabCommand3 -> {
            return tabCommand3.getText().startsWith(args[index]);
        }).map((v0) -> {
            return v0.getText();
        }).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
        return suggestions.isEmpty() ? super.tabComplete(sender, alias, args) : suggestions;
    }

    public void register(JavaPlugin plugin) {
        commandMap.register(plugin.getName(), this);
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/command/wrapper/SimpleCommand$TabCommand.class */
    public static class TabCommand {
        private final String text;
        private final String textAvant;
        private final String permission;

        public TabCommand(String text, String textAvant, String permission) {
            this.text = text;
            this.textAvant = textAvant;
            this.permission = permission;
        }

        public String getText() {
            return this.text;
        }

        public String getTextAvant() {
            return this.textAvant;
        }

        public String getPermission() {
            return this.permission;
        }
    }
}
