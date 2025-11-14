package dev.darkxx.utils.command.wrapper;

import dev.darkxx.utils.command.builder.Argument;
import dev.darkxx.utils.command.builder.CommandContext;
import dev.darkxx.utils.command.builder.CommandInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/command/wrapper/SimpleSubCommand.class */
public abstract class SimpleSubCommand {
    private final String name;
    private Consumer<CommandContext> executor;
    private Function<CommandContext, List<String>> tabCompleter;
    private final CommandInfo info = new CommandInfo();
    private final List<Argument<?>> arguments = new ArrayList();

    public abstract void execute(CommandContext commandContext);

    public abstract List<String> suggestions(CommandContext commandContext);

    public SimpleSubCommand(String name, Consumer<CommandInfo> infoConsumer) {
        this.name = name;
        infoConsumer.accept(this.info);
    }

    public SimpleSubCommand executes(Consumer<CommandContext> executor) {
        this.executor = executor;
        return this;
    }

    public SimpleSubCommand suggests(Function<CommandContext, List<String>> tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    public SimpleSubCommand withArgument(Argument<?> argument) {
        this.arguments.add(argument);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CommandInfo getInfo() {
        return this.info;
    }

    public Consumer<CommandContext> getExecutor() {
        return this.executor;
    }

    public Function<CommandContext, List<String>> getTabCompleter() {
        return this.tabCompleter;
    }

    public List<Argument<?>> getArguments() {
        return this.arguments;
    }
}
