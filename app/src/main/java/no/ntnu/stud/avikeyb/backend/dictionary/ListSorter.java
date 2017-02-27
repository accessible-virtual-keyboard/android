package no.ntnu.stud.avikeyb.backend.dictionary;

import android.support.test.espresso.core.deps.guava.collect.ComparisonChain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Tor-Martin Holen on 24-Feb-17.
 */

public abstract class ListSorter {
    /**
     * Sorts list according to an order from the SortingOrder enum
     *
     * @param list  List to sort
     * @param order SortingOrder Enum
     */
    static void sortList(List<DictionaryEntry> list, final SortingOrder order){
        Comparator<DictionaryEntry> comparator = null;
        if (order != SortingOrder.CURRENT_ORDER) {
            if (order == SortingOrder.ALPHABETICALLY_A_TO_Z) {
                comparator = (o1, o2) -> o1.getWord().compareTo(o2.getWord());
            }
            if (order == SortingOrder.ALPHABETICALLY_Z_TO_A) {
                comparator = (o1, o2) -> o2.getWord().compareTo(o1.getWord());
            }
            if (order == SortingOrder.FREQUENCY_HIGH_TO_LOW) {
                comparator = (o1, o2) -> ComparisonChain.start()
                        .compare(o2.getUserFrequency(),o1.getUserFrequency())
                        .compare(o2.getStandardFrequency(),o1.getStandardFrequency())
                        .compare(o1.getWord(),o2.getWord()).result();
            }
            if (order == SortingOrder.FREQUENCY_LOW_TO_HIGH) {
                comparator = (o1, o2) -> ComparisonChain.start()
                        .compare(o1.getUserFrequency(),o2.getUserFrequency())
                        .compare(o1.getStandardFrequency(),o2.getStandardFrequency())
                        .compare(o1.getWord(),o2.getWord()).result();
            }

            Collections.sort(list, comparator);
        }
    }
}
