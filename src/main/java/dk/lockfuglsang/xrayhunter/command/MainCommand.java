package dk.lockfuglsang.xrayhunter.command;

import dk.lockfuglsang.minecraft.command.BaseCommandExecutor;
import dk.lockfuglsang.xrayhunter.XRayHunter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MainCommand extends BaseCommandExecutor {
    private final XRayHunter plugin;

    public MainCommand(XRayHunter plugin) {
        super("xhunt", "xhunt.use", "Main XRay Hunter command");
        this.plugin = plugin;
        add(new LookupCommand(plugin));
        add(new DetailCommand());
        add(new TeleportCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (plugin.getAPI() == null) {
            commandSender.sendMessage("No valid CoreProtect plugin was found!");
            return true;
        }
        return super.onCommand(commandSender, command, alias, args);
    }
}
