package dk.lockfuglsang.xrayhunter.command;

import dk.lockfuglsang.util.LocationUtil;
import dk.lockfuglsang.util.TimeUtil;
import dk.lockfuglsang.xrayhunter.command.common.AbstractCommand;
import dk.lockfuglsang.xrayhunter.model.HuntSession;
import dk.lockfuglsang.xrayhunter.model.OreVein;
import dk.lockfuglsang.xrayhunter.model.PlayerStats;
import dk.lockfuglsang.xrayhunter.model.PlayerStatsComparator;
import dk.lockfuglsang.xrayhunter.model.VeinLocator;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

import static dk.lockfuglsang.xrayhunter.i18n.I18nUtil.tr;

/**
 * Shows details about ore-veins found by a user
 */
public class DetailCommand extends AbstractCommand {
    public DetailCommand() {
        super("detail|d", null, "index", "Shows mining details for a player");
    }

    @Override
    public boolean execute(CommandSender sender, String alias, Map<String, Object> data, String... args) {
        HuntSession session = HuntSession.getSession(sender);
        if (args.length == 1 && args[0].matches("\\d+")) {
            int index = Integer.parseInt(args[0], 10);
            if (index < 1 || session.getLookupCache() == null || session.getLookupCache().size() < index) {
                sender.sendMessage(tr("Invalid index supplied, try running lookup again."));
            } else {
                PlayerStats playerStats = session.getLookupCache().get(index-1);
                session.setPlayerStat(playerStats);
                showDetails(sender, playerStats, session);
            }
            return true;
        } else if (args.length == 1) {
            PlayerStats stats = session.getPlayerStats(args[0]);
            if (stats != null) {
                showDetails(sender, stats, session);
            } else {
                sender.sendMessage(tr("No player named {0} found in cache, try running lookup again.", args[0]));
            }
            return true;
        } else if (session.getVeins() != null) {
            showVeins(sender, session.getPlayerStats(), session.getVeins());
            return true;
        } else {
            sender.sendMessage(tr("No player selected, use name or index!"));
        }
        return false;
    }

    private void showDetails(CommandSender sender, PlayerStats playerStats, HuntSession session) {
        // TODO: 19/04/2015 - R4zorax: Step 2: Calculate time between finds
        // TODO: 19/04/2015 - R4zorax: Step 3: Support TPing to a find (closest valid TP location)
        List<CoreProtectAPI.ParseResult> data = session.getUserData(playerStats.getPlayer());
        if (data.isEmpty()) {
            sender.sendMessage(tr("No data found for player {0}, try running lookup again.", playerStats.getPlayer()));
            return;
        }
        List<OreVein> veins = VeinLocator.getVeins(data);
        session.setVeins(veins);
        showVeins(sender, playerStats, veins);
    }

    private void showVeins(CommandSender sender, PlayerStats playerStats, List<OreVein> veins) {
        // TODO: 19/04/2015 - R4zorax: Pagination
        StringBuilder sb = new StringBuilder();
        sb.append(tr("Showing what {0} has found", playerStats.getPlayer()) + "\n");
        long tlast = System.currentTimeMillis();
        int index = 1;
        for (OreVein vein : veins) {
            sb.append(tr("§7#{5,number} {0}: §9found §e{1} {2}{3}§9 ores at {4}",
                    TimeUtil.millisAsString(tlast - vein.getTime()),
                    vein.getSize(),
                    PlayerStatsComparator.MAT_COLORS.get(vein.getType()),
                    vein.getType().name().substring(0,3),
                    LocationUtil.asShortString(vein.getLocation()),
                    index++
            ) + "\n");
            tlast = vein.getTime();
        }
        sender.sendMessage(sb.toString().split("\n"));
    }
}
