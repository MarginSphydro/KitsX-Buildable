package dev.darkxx.utils.command;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.java.JavaPlugin;

@Deprecated
/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/command/XyrisCommand.class */
public abstract class XyrisCommand<T extends JavaPlugin> extends Command implements CommandExecutor, PluginIdentifiableCommand {
    private static CommandMap commandMap;
    private final T plugin;
    private final HashMap<Integer, ArrayList<TabCommand>> tabComplete;
    private final String namespace;
    private boolean register;
    private final Map<String, XyrisCommand<?>> subcommands;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XyrisCommand.class.desiredAssertionStatus();
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (CommandMap) f.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    protected XyrisCommand(T plugin, String namespace, String name) {
        super(name);
        this.register = false;
        if (!$assertionsDisabled && commandMap == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && plugin == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && name.length() <= 0) {
            throw new AssertionError();
        }
        setLabel(name);
        this.plugin = plugin;
        this.namespace = namespace;
        this.tabComplete = new HashMap<>();
        this.subcommands = new HashMap();
    }

    protected void setAliases(String... aliases) {
        if (aliases != null) {
            if (this.register || aliases.length > 0) {
                setAliases((List) Arrays.stream(aliases).collect(Collectors.toList()));
            }
        }
    }

    protected void addTabbComplete(int indice, String permission, String[] beforeText, String... arg) {
        if (arg != null && arg.length > 0 && indice >= 0) {
            if (this.tabComplete.containsKey(Integer.valueOf(indice))) {
                this.tabComplete.get(Integer.valueOf(indice)).addAll((Collection) Arrays.stream(arg).collect(ArrayList::new, (tabCommands, s) -> {
                    tabCommands.add(new TabCommand(indice, s, permission, beforeText));
                }, (v0, v1) -> {
                    v0.addAll(v1);
                }));
            } else {
                this.tabComplete.put(Integer.valueOf(indice), (ArrayList) Arrays.stream(arg).collect(ArrayList::new, (tabCommands2, s2) -> {
                    tabCommands2.add(new TabCommand(indice, s2, permission, beforeText));
                }, (v0, v1) -> {
                    v0.addAll(v1);
                }));
            }
        }
    }

    protected void addTabbComplete(int indice, String... arg) {
        addTabbComplete(indice, null, null, arg);
    }

    protected boolean registerCommand() {
        if (!this.register) {
            this.register = commandMap.register(this.namespace, this);
        }
        return this.register;
    }

    public void addSubcommand(XyrisCommand<?> subcommand) {
        this.subcommands.put(subcommand.getName(), subcommand);
    }

    public void removeSubcommand(String name) {
        this.subcommands.remove(name);
    }

    /* renamed from: getPlugin, reason: merged with bridge method [inline-methods] */
    public T m25getPlugin() {
        return this.plugin;
    }
    
    // Proper implementation of PluginIdentifiableCommand interface
    @Override
    public T getPlugin() {
        return this.plugin;
    }

    public boolean execute(CommandSender sender, String command, String[] arg) {
        if (arg.length > 0 && this.subcommands.containsKey(arg[0])) {
            XyrisCommand<?> subcommand = this.subcommands.get(arg[0]);
            return subcommand.execute(sender, command, (String[]) Arrays.copyOfRange(arg, 1, arg.length));
        }
        if (getPermission() != null && !sender.hasPermission(getPermission())) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + (getPermissionMessage() != null ? getPermissionMessage() : "You don't have permission to execute this command!"));
            return false;
        }
        if (onCommand(sender, this, command, arg)) {
            return true;
        }
        sender.sendMessage(String.valueOf(ChatColor.RED) + getUsage());
        return false;
    }

    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        int indice = args.length - 1;
        if ((getPermission() != null && !sender.hasPermission(getPermission())) || this.tabComplete.size() == 0 || !this.tabComplete.containsKey(Integer.valueOf(indice))) {
            return super.tabComplete(sender, alias, args);
        }
        List<String> list = (List) this.tabComplete.get(Integer.valueOf(indice)).stream().filter(tabCommand -> {
            return tabCommand.getTextAvant() == null || tabCommand.getTextAvant().contains(args[indice - 1]);
        }).filter(tabCommand2 -> {
            return tabCommand2.getPermission() == null || sender.hasPermission(tabCommand2.getPermission());
        }).filter(tabCommand3 -> {
            return tabCommand3.getText().startsWith(args[indice]);
        }).map((v0) -> {
            return v0.getText();
        }).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
        return list.size() < 1 ? super.tabComplete(sender, alias, args) : list;
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/command/XyrisCommand$TabCommand.class */
    private static class TabCommand {
        private final int indice;
        private final String text;
        private final String permission;
        private final ArrayList<String> textAvant;

        private TabCommand(int indice, String text, String permission, String... textAvant) {
            this.indice = indice;
            this.text = text;
            this.permission = permission;
            if (textAvant == null || textAvant.length < 1) {
                this.textAvant = null;
            } else {
                this.textAvant = (ArrayList) Arrays.stream(textAvant).collect(ArrayList::new, (v0, v1) -> {
                    v0.add(v1);
                }, (v0, v1) -> {
                    v0.addAll(v1);
                });
            }
        }

        public String getText() {
            return this.text;
        }

        public int getIndice() {
            return this.indice;
        }

        public String getPermission() {
            return this.permission;
        }

        public ArrayList<String> getTextAvant() {
            return this.textAvant;
        }
    }
}
