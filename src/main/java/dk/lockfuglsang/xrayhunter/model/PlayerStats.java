package dk.lockfuglsang.xrayhunter.model;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * Data object for holding the player stats.
 */
public class PlayerStats {
    private final String player;
    private final float total;
    private final Map<Integer, Integer> blockCount = new HashMap<>();

    public PlayerStats(String player, Map<Integer, Integer> map) {
        this.player = player;
        int sum = 0;
        for (Map.Entry<Integer,Integer> entry : map.entrySet()) {
            int val = entry.getValue();
            blockCount.put(entry.getKey(), val);
            sum += val;
        }
        total = sum;
    }

    public String getPlayer() {
        return player;
    }

    public int getCount(Material mat) {
        if (blockCount.containsKey(mat.getId())) {
            return blockCount.get(mat.getId());
        }
        return 0;
    }

    public float getRatio(Material mat) {
        return getCount(mat) / total;
    }
}
