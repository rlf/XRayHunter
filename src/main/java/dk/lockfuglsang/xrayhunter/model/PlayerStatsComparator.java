package dk.lockfuglsang.xrayhunter.model;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comparator the maps of block-counts for two users
 */
public class PlayerStatsComparator implements Comparator<PlayerStats> {
    public static final List<Material> MATS = Arrays.asList(
            Material.DIAMOND_ORE,
            Material.MOB_SPAWNER,
            Material.EMERALD_ORE,
            Material.GOLD_ORE,
            Material.IRON_ORE,
            Material.STONE
    );
    public static final Map<Material, String> MAT_COLORS = new HashMap<>();
    static {
        MAT_COLORS.put(Material.DIAMOND_ORE, "§b");
        MAT_COLORS.put(Material.MOB_SPAWNER, "§0");
        MAT_COLORS.put(Material.EMERALD_ORE, "§a");
        MAT_COLORS.put(Material.GOLD_ORE, "§e");
        MAT_COLORS.put(Material.IRON_ORE, "§f");
        MAT_COLORS.put(Material.STONE, "§7");
    }

    @Override
    public int compare(PlayerStats o1, PlayerStats o2) {
        int cmp = 0;
        for (Material blockId : MATS) {
            int c1 = o1.getCount(blockId);
            int c2 = o2.getCount(blockId);
            cmp = c2 - c1;
            if (cmp != 0) {
                return cmp;
            }
        }
        return cmp;
    }
}
