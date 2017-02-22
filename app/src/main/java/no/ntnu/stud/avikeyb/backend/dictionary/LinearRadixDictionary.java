package no.ntnu.stud.avikeyb.backend.dictionary;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class LinearRadixDictionary extends LinearDictionary {

    private ArrayList<DictionaryEntry> radixSuggestions;

    /**
     * Constructs a dictionary
     *
     * @param context
     */
    public LinearRadixDictionary(Context context) {
        super(context);
        radixSuggestions = new ArrayList<>();
    }


    public void findSuggestionsWithRadix(String radix) {
        findPrimarySuggestions(radix);
        radixSuggestions.addAll(getPrimarySuggestions());
    }

    public ArrayList<DictionaryEntry> getRadixSuggestions() {
        return radixSuggestions;
    }

    public void resetRadixSuggestions(){
        radixSuggestions = new ArrayList<>();
    }

    public ArrayList<DictionaryEntry> getSortedRadixSuggestions(){
        sortList(radixSuggestions, SortingOrder.FREQUENCY_HIGH_TO_LOW);
        return radixSuggestions;
    }
}
