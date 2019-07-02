package ua.shadowdan.buildbattle.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by SHADOWDAN on 02.07.2019.
 */
public class CollectionUtils {

    public static <K, V extends Comparable<V>> K getKeyWithHighestValue(Map<K, V> map) {
        return Collections.max(map.entrySet(), Comparator.comparing(Map.Entry::getValue)).getKey();
    }
}
