package dk.lockfuglsang.xrayhunter.coreprotect;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Common code accross CoreProtect versions.
 */
public class AbstractCoreProtectAdaptor {
    protected static List<String> convertMats(List<Material> matList) {
        List<String> result = new ArrayList<>(matList != null ? matList.size() : 0);
        if (matList != null) {
            for (Material i : matList) {
                result.add("" + i.getId());
            }
        }
        return result;
    }

    private static List<String> convert(List<Integer> integerList) {
        List<String> result = new ArrayList<>(integerList != null ? integerList.size() : 0);
        if (integerList != null) {
            for (Integer i : integerList) {
                result.add("" + i);
            }
        }
        return result;
    }

    protected Class<?> getLookupClass() {
        try {
            return Class.forName("net.coreprotect.database.Lookup");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
