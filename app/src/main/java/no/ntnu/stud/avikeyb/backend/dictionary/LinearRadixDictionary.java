package no.ntnu.stud.avikeyb.backend.dictionary;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class LinearRadixDictionary extends LinearDictionary {

    private List<DictionaryEntry> radixSuggestions;
    private List<DictionaryEntry> lastAddendum;
    /**
     * Constructs a dictionary
     *
     * @param dictionaryLoader
     */
    public LinearRadixDictionary(DictionaryLoader dictionaryLoader) {
        super(dictionaryLoader);
        radixSuggestions = new ArrayList<>();
    }


    /**
     * Finds suggestions matching radix and adds them to the suggestion list
     * @param radix
     */
    public void findSuggestionsWithRadix(String radix) {
        findPrimarySuggestions(radix);
        radixSuggestions.addAll(getPrimarySuggestions());
    }

    /**
     * list containing suggestions for all the radix(es) that has been searched for.
     * @return list of suggestions
     */
    public List<DictionaryEntry> getRadixSuggestions() {
        return radixSuggestions;
    }

    /**
     * list with suggestions for only last radix
     * @return list of suggestions
     */
    public List<DictionaryEntry> getLastAddendums(){
        return getPrimarySuggestions();
    }

    /**
     * Resets the radix suggestions
     */
    public void resetRadixSuggestions(){
        radixSuggestions = new ArrayList<>();
    }

    /**
     * Sorts the radix suggestions according to what's the most likely suggestions
     * @return
     */
    public List<DictionaryEntry> getSortedRadixSuggestions(){
        sortList(radixSuggestions, SortingOrder.FREQUENCY_HIGH_TO_LOW);
        return radixSuggestions;
    }
}
