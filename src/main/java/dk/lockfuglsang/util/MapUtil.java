package dk.lockfuglsang.util;

import java.util.*;

public class MapUtil {
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map, final Comparator<V> comparator) {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return comparator.compare(o1.getValue(), o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}