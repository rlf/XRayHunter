package dk.lockfuglsang.xrayhunter.command;

import dk.lockfuglsang.util.TimeUtil;
import dk.lockfuglsang.xrayhunter.coreprotect.Callback;
import dk.lockfuglsang.xrayhunter.coreprotect.CoreProtectHandler;
import dk.lockfuglsang.xrayhunter.XRayHunter;
import dk.lockfuglsang.xrayhunter.command.common.AbstractCommand;
import dk.lockfuglsang.xrayhunter.model.HuntSession;
import dk.lockfuglsang.xrayhunter.model.PlayerStats;
import dk.lockfuglsang.xrayhunter.model.PlayerStatsComparator;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lookups possible candidates for the last days.
 */
class LookupCommand extends AbstractCommand {
    private final XRayHunter plugin;

    LookupCommand(XRayHunter plugin) {
        super("lookup|l", null, "time", "Hunt in the past (1d, 2h, 2h30m)");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(final CommandSender sender, String alias, Map<String, Object> data, String... args) {
        if (args.length == 1) {
            long millis = TimeUtil.millisFromString(args[0]);
            if (millis == 0) {
                sender.sendMessage("Invalid time-argument, try \u00a792d");
                return false;
            }
            CoreProtectHandler.performLookup(plugin, sender, TimeUtil.millisAsSeconds(millis), PlayerStatsComparator.MATS, null, new LookupCallback(sender));
            return true;
        }
        return false;
    }

    private void increment(Map<Integer, Integer> blockCount, Integer blockId) {
        if (!blockCount.containsKey(blockId)) {
            blockCount.put(blockId, 1);
        } else {
            blockCount.put(blockId, blockCount.get(blockId) + 1);
        }
    }

    private class LookupCallback extends Callback {
        private final CommandSender sender;

        LookupCallback(CommandSender sender) {
            this.sender = sender;
        }

        @Override
        public void run() {
            List<String[]> result = getData();
            if (result == null || result.isEmpty()) {
                if (sender instanceof Player) {
                    sender.sendMessage(MessageFormat.format("No suspicious activity within that time-frame in {0}!", ((Player) sender).getLocation().getWorld().getName()));
                } else {
                    sender.sendMessage("No suspicious activity within that time-frame!");
                }
                return;
            }
            Map<Integer, Integer> blockCount = new HashMap<>();
            Map<String, Map<Integer, Integer>> playerCount = new HashMap<>();
            Map<String, List<CoreProtectAPI.ParseResult>> dataMap = new HashMap<>();
            for (String[] line : result) {
                CoreProtectAPI.ParseResult parse = plugin.getAPI().parseResult(line);
                Integer blockId = parse.getTypeId();
                increment(blockCount, blockId);
                if (!playerCount.containsKey(parse.getPlayer())) {
                    playerCount.put(parse.getPlayer(), new HashMap<Integer, Integer>());
                }
                increment(playerCount.get(parse.getPlayer()), blockId);
                if (!dataMap.containsKey(parse.getPlayer())) {
                    dataMap.put(parse.getPlayer(), new ArrayList<CoreProtectAPI.ParseResult>());
                }
                dataMap.get(parse.getPlayer()).add(parse);
            }
            List<PlayerStats> top10 = new ArrayList<>();
            for (String player : playerCount.keySet()) {
                top10.add(new PlayerStats(player, playerCount.get(player)));
            }
            Collections.sort(top10, new PlayerStatsComparator());
            HuntSession.getSession(sender)
                    .setLookupCache(top10)
                    .setUserData(dataMap);

            StringBuilder sb = new StringBuilder();
            sb.append("Listing");
            for (Material mat : PlayerStatsComparator.MATS) {
                sb.append(PlayerStatsComparator.MAT_COLORS.get(mat) + "§l " + mat.name().substring(0, 3));
            }
            sb.append("\n");
            int place = 1;
            for (PlayerStats stat : top10.subList(0, Math.min(top10.size(), 10))) {
                sb.append(MessageFormat.format("§7#{0}", place));
                for (Material mat : PlayerStatsComparator.MATS) {
                    sb.append(PlayerStatsComparator.MAT_COLORS.get(mat) +
                            MessageFormat.format(" §l{0,number,##}§7({1,number,##}%)", stat.getCount(mat), 100 * stat.getRatio(mat)));
                }
                sb.append(" §9" + stat.getPlayer() + "\n");
                place++;
            }
            sender.sendMessage(sb.toString().split("\n"));
        }
    }
}
