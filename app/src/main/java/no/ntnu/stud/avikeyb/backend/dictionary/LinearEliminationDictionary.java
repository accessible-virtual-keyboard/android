package no.ntnu.stud.avikeyb.backend.dictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.Symbol;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class LinearEliminationDictionary extends LinearDictionary {

    private List<DictionaryEntry> suggestionList;
    private List<DictionaryEntry> reducedSuggestionList;
    /**
     * Constructs a dictionary
     *
     * @param dictionaryLoader
     */
    public LinearEliminationDictionary(DictionaryLoader dictionaryLoader) {
        super(dictionaryLoader);
        suggestionList = getDictionary();
        reducedSuggestionList = new ArrayList<>();
    }


    /**
     * Finds suggestions matching radix and adds them to the suggestion list
     *
     */

    public void findValidSuggestions(int index, List<Symbol> prefixes) {
        reducedSuggestionList = new ArrayList<>();

        for (DictionaryEntry entry: suggestionList) {
            for (int i = 0; i < prefixes.size(); i++) {
                String letter = prefixes.get(i).getContent();
                boolean contained = entry.getWord().substring(index).startsWith(letter);
                if (contained) {
                    reducedSuggestionList.add(entry);
                    break;
                }
            }
        }
        suggestionList = reducedSuggestionList;
        System.out.println("Suggestions: " + suggestionList.size());
    }


    public void resetSuggestionList(){
        suggestionList = getDictionary();
    }

    public void printListWithNSuggestions(int n){
        sortList(suggestionList,SortingOrder.FREQUENCY_HIGH_TO_LOW);
        for (int i = 0; i < suggestionList.size(); i++) {
            DictionaryEntry entry = suggestionList.get(i);
            System.out.println(entry.getWord() + " - " + entry.getFrequency());
            if(i == n){
                break;
            }
        }
    }

}
