package dk.lockfuglsang.xrayhunter.coreprotect;

import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.Statement;
import java.util.List;

/**
 * Adaptor allowing implementation to utilize multiple different CP versions
 */
public interface CoreProtectAdaptor {
    boolean isAvailable();
    List<String[]> performLookup(Statement stmt, List<Material> restrictBlocks, List<Integer> actions,
                                 Location location, int time, boolean restrictWorld);
}
