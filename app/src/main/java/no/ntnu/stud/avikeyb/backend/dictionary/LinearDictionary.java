package no.ntnu.stud.avikeyb.backend.dictionary;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.Dictionary;

/**
 * Dictionary using linear search
 * Created by Tor-Martin Holen on 21-Feb-17 (Originally 31-Jan-17).
 */

public class LinearDictionary implements Dictionary, InMemoryDictionary {

    private List<DictionaryEntry> dictionary;
    private List<DictionaryEntry> primarySuggestions;
    private List<DictionaryEntry> secondarySuggestions;
    private SortingOrder preferredOrder = SortingOrder.FREQUENCY_HIGH_TO_LOW;
    private String textWritten = "";
    private Boolean dictionaryLoaded = false;

    /**
     * Constructs a dictionary
     */

    public LinearDictionary(List<DictionaryEntry> dictionary) {
        this.dictionary = dictionary;
    }

    public LinearDictionary() {
        this(new ArrayList<>());
    }



    @Override
    public void setDictionary(List<DictionaryEntry> dictionary) {
        this.dictionary = dictionary;
    }

    public List<DictionaryEntry> getDictionary() {
        return dictionary;
    }

    public SortingOrder getPreferredOrder() {
        return preferredOrder;
    }

    public void setPreferredOrder(SortingOrder preferredOrder) {
        this.preferredOrder = preferredOrder;
    }

    @Override
    public List<String> getSuggestionsStartingWith(String match) {

        primarySuggestions = new ArrayList<>();
        this.textWritten = match;
        for (DictionaryEntry currentEntry : dictionary) {
            String currentWord = currentEntry.getWord();
            if (currentWord.startsWith(match) && !currentWord.equals(match)) {
                primarySuggestions.add(currentEntry);
            }
        }
        ListSorter.sortList(primarySuggestions, SortingOrder.FREQUENCY_HIGH_TO_LOW);
        return extractWords(primarySuggestions);
    }

    public List<String> extractWords(List<DictionaryEntry> list) {
        ArrayList<String> suggestions = new ArrayList<>();
        for (DictionaryEntry entry : list) {
            suggestions.add(entry.getWord());
        }
        return suggestions;
    }

    @Override
    public void updateWordUsage(String string) {


        for (DictionaryEntry entry:dictionary) {
            if (entry.getWord().equals(string)) {
                int newFrequency = entry.getUserFrequency() + 1;
                entry.setUserFrequency(newFrequency);
            }
        }
    }

    /**
     * Use with a list that contains duplicate entries to fix them.
     * Note:dictionary.txt doesn't contain duplicates, but other lists might
     */
    private void fixDuplicateEntries() {
        ListSorter.sortList(dictionary, SortingOrder.ALPHABETICALLY_A_TO_Z);
        DictionaryEntry previousEntry = null;
        for (int i = 0; i < dictionary.size(); i++) {
            DictionaryEntry currentEntry = dictionary.get(i);
            if (previousEntry != null && previousEntry.getWord().equals(currentEntry.getWord())) {
                previousEntry.setUserFrequency(previousEntry.getUserFrequency() + currentEntry.getUserFrequency());
                dictionary.remove(currentEntry);
                i--;
            } else {
                previousEntry = currentEntry;
            }
        }

        ListSorter.sortList(dictionary, preferredOrder);
    }

    /**
     * Finds suggestions starting with or containing whats already written
     *
     * @param textWritten Currently written text
     */
    public void findSuggestions(String textWritten){

        primarySuggestions = new ArrayList<>();
        secondarySuggestions = new ArrayList<>();
        this.textWritten = textWritten;

        for (DictionaryEntry currentEntry : dictionary) {
            String currentWord = currentEntry.getWord();

            if (currentWord.startsWith(textWritten)) {
                primarySuggestions.add(currentEntry);
            } else if (currentWord.contains(textWritten) && !currentWord.startsWith(textWritten)) {
                secondarySuggestions.add(currentEntry);
            }
        }

        ListSorter.sortList(secondarySuggestions, preferredOrder);
    }

    /**
     * Finds suggestions starting with
     *
     * @param textWritten Currently written text
     */
    public void findPrimarySuggestions(String textWritten) {
        primarySuggestions = new ArrayList<>();
        this.textWritten = textWritten;

        for (DictionaryEntry currentEntry : dictionary) {
            String currentWord = currentEntry.getWord();
            if (currentWord.startsWith(textWritten)) {
                primarySuggestions.add(currentEntry);
            }
        }
    }

    public void findPrimarySuggestions(List<DictionaryEntry> listToSearch, String textWritten) {
        primarySuggestions = new ArrayList<>();
        this.textWritten = textWritten;

        for (DictionaryEntry currentEntry : listToSearch) {
            String currentWord = currentEntry.getWord();
            if (currentWord.startsWith(textWritten)) {
                primarySuggestions.add(currentEntry);
            }
        }
    }


    public void printPrimarySuggestions() {

        printSuggestions(primarySuggestions, "Primary");
    }

    public void printSecondarySuggestions() {

        printSuggestions(secondarySuggestions, "Secondary");
    }

    public List<DictionaryEntry> getPrimarySuggestions() {

        return primarySuggestions;
    }

    public List<DictionaryEntry> getSecondarySuggestions() {

        return secondarySuggestions;
    }

    /**
     * Returns all the suggestions starting with or containing the search
     *
     * @return
     */
    public List<DictionaryEntry> getSuggestions() {
        List<DictionaryEntry> allSuggestions = getPrimarySuggestions();
        allSuggestions.addAll(getSecondarySuggestions());
        printSuggestions(allSuggestions, "All");
        return allSuggestions;
    }

    /**
     * Prints suggestions
     *
     * @param suggestions    List containing suggestions
     * @param suggestionType Title for which type of suggestions this is
     */
    private void printSuggestions(List<DictionaryEntry> suggestions, String suggestionType){
        System.out.println(suggestionType + " suggestion for: \"" + textWritten + "\"");
        printList(suggestions);
    }

    /**
     * Prints all the words in the dictionary.
     */
    public void printDictionary() {
        printList(dictionary);
    }


    /**
     * Prints a list with the word and frequency
     *
     * @param list
     */
    public void printList(List<DictionaryEntry> list){
        for (DictionaryEntry entry : list) {
            System.out.println(entry.getWord() + " - " + entry.getUserFrequency());
        }
    }

    /**
     * Gets which index current word is in the list that is being searched.
     *
     * @param list       List to search.
     * @param targetWord Word to find.
     * @return
     */
    public int findWordIndex(List<DictionaryEntry> list, String targetWord){
        for (int i = 0; i < list.size(); i++) {
            String currentWord = list.get(i).getWord();
            if (currentWord.equals(targetWord)) {
                return i;
            }
        }
        return -1;
    }

}
