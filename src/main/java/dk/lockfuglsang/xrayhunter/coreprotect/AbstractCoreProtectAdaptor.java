package dk.lockfuglsang.xrayhunter.coreprotect;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    protected static List<String> convertMatsName(List<Material> matList) {
        List<String> result = new ArrayList<>(matList != null ? matList.size() : 0);
        if (matList != null) {
            for (Material i : matList) {
                result.add("" + i.name().toLowerCase());
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

    protected boolean isVersionLaterThan(String version, String otherVersion) {
        Pattern versionPattern = Pattern.compile("v?(?<major>[0-9]+)\\.(?<minor>[0-9]+).*");
        Matcher m1 = versionPattern.matcher(version);
        Matcher m2 = versionPattern.matcher(otherVersion);
        if (m1.matches() && m2.matches()) {
            int major1 = Integer.parseInt(m1.group("major"), 10);
            int major2 = Integer.parseInt(m2.group("major"), 10);
            int minor1 = Integer.parseInt(m1.group("minor"), 10);
            int minor2 = Integer.parseInt(m1.group("minor"), 10);
            return major1 >= major2 || (major1 == major2 && minor1 >= minor2);
        }
        return false;
    }
}
