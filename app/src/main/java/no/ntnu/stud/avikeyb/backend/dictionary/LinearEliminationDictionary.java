package no.ntnu.stud.avikeyb.backend.dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class LinearEliminationDictionary extends LinearDictionary {

    private List<List<DictionaryEntry>> suggestionHistory;
    //private int searchIndex = 0;
    /**
     * Constructs a dictionary
     */
    public LinearEliminationDictionary() {
        super();
    }

    /**
     * Finds suggestions from the previous suggestion list. The words is added to the new
     * suggestion list if the specified index matches a letter in the list. Thus the new suggestion
     * list is much smaller than the previous one.
     */
    public void findValidSuggestions(List<String> lettersToFindAtIndex) {
        if (suggestionHistory == null) {
            suggestionHistory = new ArrayList<>();
            suggestionHistory.add(getDictionary());
        }
        List<DictionaryEntry> reducedSuggestionList = new ArrayList<>();

        for (DictionaryEntry entry : getLastSuggestions()) {
            for (int i = 0; i < lettersToFindAtIndex.size(); i++) {
                String letter = lettersToFindAtIndex.get(i);
                boolean contained = entry.getWord().substring(findSearchIndex()).startsWith(letter);
                if (contained) {
                    reducedSuggestionList.add(entry);
                    break;
                }
            }
        }

        System.out.println("Suggestions: " + getLastSuggestions().size());
        if(reducedSuggestionList.isEmpty()){
            resetSuggestionHistory();
        }else{
            //searchIndex++;
            suggestionHistory.add(reducedSuggestionList);
        }


    }

    /**
     * Returns the last valid suggestions
     *
     * @return List<DictionaryEntry> containing valid suggestions
     */
    private List<DictionaryEntry> getLastSuggestions(){
        return suggestionHistory.get(suggestionHistory.size()-1);
    }

    /**
     * Resets the suggestion history to all the words contained in the dictionary.
     * Should be called when the user sends his word
     */
    public void resetSuggestionHistory() {
        suggestionHistory = suggestionHistory.subList(0,1); //We want to keep the dictionary list
        //searchIndex = 0;
    }

    /**
     * Prints n suggestions from the suggestion list
     *
     * @param n the amount of suggestions needed
     */
    public void printListWithNSuggestions(int n) {
        List<DictionaryEntry> lastSuggestions = getLastSuggestions();
        sortList(lastSuggestions, SortingOrder.FREQUENCY_HIGH_TO_LOW);
        printList(getValidSuggestions(n));
    }

    /**
     * Reverts the suggestion history n steps, so it deletes n entries from the end of the
     * suggestion history (used to implement backspace functionality).
     * @param steps number of steps to revert.
     */
    public void revertLastSuggestions(int steps){
        int index = suggestionHistory.size()-steps;
        suggestionHistory = suggestionHistory.subList(0, index);
        //searchIndex -= steps;
    }

    /**
     * Reverts the suggestion history one step, so it deletes a entry from the end of the
     * suggestion history (used to implement backspace functionality).
     */
    public void revertLastSuggestions(){
        revertLastSuggestions(1);
    }

    /**
     * Returns the suggestion list containing n elements of the original list.
     *
     * @param n number of suggestions to include in sublist
     * @return
     */
    public List<DictionaryEntry> getValidSuggestions(int n) {
        List<DictionaryEntry> lastSuggestions = getLastSuggestions();
        if (lastSuggestions.size() < n) {
            return lastSuggestions;
        } else {
            return lastSuggestions.subList(0, n);
        }
    }

    private int findSearchIndex(){
        return suggestionHistory.size()-1;
    }
}
