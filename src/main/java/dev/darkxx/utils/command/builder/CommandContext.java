package dev.darkxx.utils.command.builder;

import org.bukkit.command.CommandSender;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/command/builder/CommandContext.class */
public class CommandContext {
    private final CommandSender sender;
    private String label;
    private final String[] args;

    public CommandContext(CommandSender sender, String label, String[] args) {
        this.sender = sender;
        this.label = label;
        this.args = args;
    }

    public CommandContext(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public String getLabel() {
        return this.label;
    }

    public String[] getArgs() {
        return this.args;
    }
}
