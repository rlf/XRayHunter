package dk.lockfuglsang.xrayhunter.model;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Responsible for remembering actions / data / lookups for a player, allowing
 * for easier command designs (i.e. you can refine previous searches).
 */
public class HuntSession {
    private static final Map<String, HuntSession> sessionMap = new ConcurrentHashMap<>();

    public synchronized static HuntSession getSession(CommandSender sender) {
        if (!sessionMap.containsKey(sender.getName())) {
            sessionMap.put(sender.getName(), new HuntSession());
        }
        return sessionMap.get(sender.getName());
    }

    /**
     * timestamp of last activity (enables us to clear-cache).
     */
    private long activity;

    /**
     * Last invoked /xhunt lookup
     */
    private List<PlayerStats> lookupCache = Collections.emptyList();
    private PlayerStats currentStat;

    private Map<String, List<CoreProtectAPI.ParseResult>> userData;

    private List<OreVein> veins;

    private HuntSession() {
        activity = System.currentTimeMillis();
    }

    public long getActivity() {
        return activity;
    }

    public List<PlayerStats> getLookupCache() {
        activity = System.currentTimeMillis();
        return lookupCache;
    }

    public PlayerStats getPlayerStats() {
        return currentStat;
    }

    public void setPlayerStat(PlayerStats currentStat) {
        activity = System.currentTimeMillis();
        this.currentStat = currentStat;
    }

    public PlayerStats getPlayerStats(String player) {
        for (PlayerStats stats : getLookupCache()) {
            if (stats.getPlayer().equalsIgnoreCase(player)) {
                currentStat = stats;
                return stats;
            }
        }
        currentStat = null;
        veins = null;
        return null;
    }

    public HuntSession setLookupCache(List<PlayerStats> lookupCache) {
        activity = System.currentTimeMillis();
        this.lookupCache = lookupCache;
        this.currentStat = null;
        this.veins = null;
        return this;
    }

    public List<CoreProtectAPI.ParseResult> getUserData(String player) {
        activity = System.currentTimeMillis();
        return userData != null && userData.containsKey(player) ? userData.get(player) : Collections.<CoreProtectAPI.ParseResult>emptyList();
    }

    public HuntSession setUserData(Map<String, List<CoreProtectAPI.ParseResult>> userData) {
        activity = System.currentTimeMillis();
        this.userData = userData;
        this.veins = null;
        return this;
    }

    public void setVeins(List<OreVein> veins) {
        activity = System.currentTimeMillis();
        this.veins = veins;
    }

    public List<OreVein> getVeins() {
        activity = System.currentTimeMillis();
        return veins;
    }
}
