package dk.lockfuglsang.xrayhunter.command;

import dk.lockfuglsang.xrayhunter.XRayHunter;
import dk.lockfuglsang.xrayhunter.command.common.AbstractCommandExecutor;

public class MainCommand extends AbstractCommandExecutor {
    public MainCommand(XRayHunter plugin) {
        super("xhunt", "xhunt.use", "Main XRay Hunter command");
        add(new LookupCommand(plugin));
        add(new DetailCommand());
        add(new TeleportCommand());
    }
}
