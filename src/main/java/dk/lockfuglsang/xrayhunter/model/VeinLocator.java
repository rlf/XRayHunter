package dk.lockfuglsang.xrayhunter.model;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for locating veins within mining data.
 */
public enum VeinLocator {;
    private static final Collection<Material> IGNORE = Arrays.asList(
            Material.STONE,
            Material.IRON_ORE,
            Material.GOLD_ORE
    );
    public static List<OreVein> getVeins(List<CoreProtectAPI.ParseResult> data) {
        List<OreVein> veins = new ArrayList<>();
        Map<Material, OreVein> current = new HashMap<>();
        for (CoreProtectAPI.ParseResult ore : data) {
            Material mat = ore.getType();
            if (IGNORE.contains(mat)) {
                continue;
            }
            if (!current.containsKey(mat)) {
                current.put(mat, new OreVein(ore));
            } else {
                OreVein existing = current.get(mat);
                if (existing.isValid(ore)) {
                    existing.add(ore);
                } else {
                    veins.add(existing);
                    current.put(mat, new OreVein(ore));
                }
            }
        }
        veins.addAll(current.values());
        Collections.sort(veins, new OreVeinComparator());
        return veins;
    }
}
