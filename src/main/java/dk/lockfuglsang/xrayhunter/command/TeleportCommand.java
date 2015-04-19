package dk.lockfuglsang.xrayhunter.command;

import dk.lockfuglsang.util.LocationUtil;
import dk.lockfuglsang.xrayhunter.command.common.AbstractCommand;
import dk.lockfuglsang.xrayhunter.model.HuntSession;
import dk.lockfuglsang.xrayhunter.model.OreVein;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

import static dk.lockfuglsang.xrayhunter.i18n.I18nUtil.tr;

/**
 * Supports teleporting to a vein location
 */
public class TeleportCommand extends AbstractCommand {
    public TeleportCommand() {
        super("teleport|tp", null, "index", "Teleports you to a vein location");
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        HuntSession session = HuntSession.getSession(sender);
        if (args.length == 1 && args[0].matches("\\d+") && sender instanceof Player) {
            int index = Integer.parseInt(args[0], 10);
            if (index < 1 || session.getVeins() == null || session.getVeins().size() < index) {
                sender.sendMessage(tr("Invalid index supplied, try running lookup again."));
            } else {
                safeTeleport((Player) sender, session.getVeins().get(index-1));
            }
            return true;
        }
        return false;
    }

    private void safeTeleport(Player player, OreVein oreVein) {
        Location loc = oreVein.getLocation();
        Location tpLoc = LocationUtil.findSafeLocation(loc, 7);
        if (tpLoc != null) {
            Location d = loc.clone().subtract(tpLoc);
            tpLoc.setDirection(d.toVector());
            player.teleport(tpLoc.add(0.5, 0, 0.5));
        } else {
            player.sendMessage(tr("§cNo safe teleport location found near {0}", LocationUtil.asString(loc)));
        }
    }
}
