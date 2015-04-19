package dk.lockfuglsang.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.Collection;

/**
 * Utility for common block related functions.
 */
public enum BlockUtil {;
    private static final Collection<Material> FLUIDS = Arrays.asList(Material.STATIONARY_WATER, Material.WATER, Material.LAVA, Material.STATIONARY_LAVA);

    public static boolean isBreathable(Material block) {
        return !block.isSolid() && !isFluid(block);
    }

    public static boolean isFluid(Material block) {
        return FLUIDS.contains(block);
    }
}
