package dk.lockfuglsang.xrayhunter.command.common;

import org.bukkit.command.TabCompleter;

/**
 * Convenience implementation of the USBCommand
 */
public abstract class AbstractCommand implements Command {
    private final String[] aliases;
    private final String permission;
    private final String description;
    private final String usage;
    private final String[] params;
    private CompositeCommand parent;

    public AbstractCommand(String name, String permission, String params, String description, String usage) {
        this.aliases = name.split("\\|");
        this.permission = permission;
        this.description = description;
        this.usage = usage;
        this.params = params != null && !params.trim().isEmpty() ? params.split(" ") : new String[0];
    }

    public AbstractCommand(String name, String permission, String params, String description) {
        this(name, permission, params, description, null);
    }

    public AbstractCommand(String name, String permission, String description) {
        this(name, permission, null, description, null);
    }

    public AbstractCommand(String name, String description) {
        this(name, null, null, description, null);
    }

    @Override
    public String getName() {
        return aliases[0];
    }

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public String[] getParams() {
        return params;
    }

    @Override
    public TabCompleter getTabCompleter() {
        return null;
    }

    @Override
    public CompositeCommand getParent() {
        return parent;
    }

    @Override
    public void setParent(CompositeCommand parent) {
        this.parent = parent;
    }
}
