package dk.lockfuglsang.xrayhunter.model;

import java.util.Comparator;

public class OreVeinComparator implements Comparator<OreVein> {
    @Override
    public int compare(OreVein v1, OreVein v2) {
        return (int) (v2.getTime() - v1.getTime());
    }
}
