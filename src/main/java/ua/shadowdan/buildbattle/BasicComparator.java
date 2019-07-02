package ua.shadowdan.buildbattle;

import java.util.Comparator;

/**
 * Created by SHADOWDAN on 02.07.2019.
 */
public class BasicComparator<T> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        if (o1.equals(o2)) {
            return 0;
        }
        return -1;
    }
}
