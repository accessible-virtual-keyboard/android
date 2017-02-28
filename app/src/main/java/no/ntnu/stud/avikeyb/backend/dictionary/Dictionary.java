package no.ntnu.stud.avikeyb.backend.dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Dictionary that uses binary search to find matches.
 *
 * @author Kristian Honningsvag.
 * @author Tor-Martin Holen.
 */
public class Dictionary {

    private List<DictionaryEntry> dictionaryEntries;
    private List<DictionaryEntry> primarySuggestions;
    private List<DictionaryEntry> secondarySuggestions;
    private SortingOrder preferredOrder = SortingOrder.FREQUENCY_HIGH_TO_LOW;
    private String textWritten = "";


    /**
     * Constructs a dictionary with no entries.
     */
    public Dictionary() {
        this(new ArrayList<DictionaryEntry>());
    }

    /**
     * Constructs the dictionary.
     * Automatically sorts the dictionary alphabetically.
     *
     * @param dictionary
     */
    public Dictionary(List<DictionaryEntry> dictionary) {
        this.dictionaryEntries = dictionary;
        sortDictionary();
    }

    /**
     * Sorts the dictionary in an alphabetically order.
     */
    private void sortDictionary() {
        // Sort the dictionary alphabetically.
        Collections.sort(dictionaryEntries, new Comparator<DictionaryEntry>() {
            @Override
            public int compare(DictionaryEntry o1, DictionaryEntry o2) {
                return o1.getWord().compareToIgnoreCase(o2.getWord());
            }
        });
    }

    /**
     * Performs a prefix search on the dictionary, returning a list of all words that begin with the
     * prefix. The returned list is sorted by the words frequency of occurrence.
     * <p>
     * This method expects the dictionary to be in an alphabetical ordering.
     *
     * @param prefix The prefix.
     * @return All words that begin with the prefix.
     */
    public List<String> prefixSearchBinary(String prefix) {

        // Return empty list if invalid input, or if dictionary is empty.
        if (prefix == null || prefix.equalsIgnoreCase("") || dictionaryEntries.isEmpty()) {
            return new ArrayList<String>();
        }

        ArrayList<String> matchingWords = new ArrayList<>();
        ArrayList<DictionaryEntry> matchingDictionaryEntries = new ArrayList<>();
        String currentWord = null;
        boolean doneSearching = false;
        boolean allFound = false;

        int leftLimit = 0;
        int rightLimit = dictionaryEntries.size() - 1;
        int searchLocation = (leftLimit + rightLimit) / 2;

        // Find the word with the lowest string value that starts with the prefix.
        while (!doneSearching) {

            // Get dictionary word at current location.
            currentWord = dictionaryEntries.get(searchLocation).getWord();
            int delta = prefix.compareToIgnoreCase(currentWord);

            // When there are only two alternatives left.
            if ((rightLimit - leftLimit) <= 1) {
                if (delta > 0) {
                    searchLocation = rightLimit;
                } else if (delta < 0) {
                    searchLocation = leftLimit;
                } else if (delta == 0) {
                    // Do nothing. We are in the correct position.
                }
                currentWord = dictionaryEntries.get(searchLocation).getWord();
                doneSearching = true;
            }

            if (!doneSearching) {
                // Find out if we need to search backwards or forwards.
                if (delta < 0) {
                    // Target might be behind us.
                    rightLimit = searchLocation;
                    searchLocation = (leftLimit + rightLimit) / 2;
                } else if (delta > 0) {
                    // Target might be ahead of us.
                    leftLimit = searchLocation;
                    searchLocation = (leftLimit + rightLimit) / 2;
                } else if (delta == 0) {
                    // Landed directly on the first matching word.
                    doneSearching = true;
                }
//            System.out.println("L:" + leftLimit + " M:" + searchLocation + " R:" + rightLimit);
            }
        }

        // Iterate forwards and collect all the matches.
        while (!allFound) {
            if (currentWord.startsWith(prefix)) {
                if (!currentWord.equalsIgnoreCase(prefix)) {  // Don't add the word if it is identical to the prefix.
                    matchingDictionaryEntries.add(dictionaryEntries.get(searchLocation));
                }
                searchLocation += 1;
                if (searchLocation < dictionaryEntries.size()) {
                    currentWord = dictionaryEntries.get(searchLocation).getWord();
                } else {
                    // Reached end of dictionary.
                    allFound = true;
                }
            } else {
                // There are now more matches.
                allFound = true;
            }
        }

        // Sort list by the words frequency of occurrence.
        // Sorts first on standard frequencies, then on user frequencies.
        Collections.sort(matchingDictionaryEntries, new Comparator<DictionaryEntry>() {
            @Override
            public int compare(DictionaryEntry o1, DictionaryEntry o2) {
                return o2.getStandardFrequency() - o1.getStandardFrequency();
            }
        });
        Collections.sort(matchingDictionaryEntries, new Comparator<DictionaryEntry>() {
            @Override
            public int compare(DictionaryEntry o1, DictionaryEntry o2) {
                return o2.getUserFrequency() - o1.getUserFrequency();
            }
        });

        // Get only the words.
        for (DictionaryEntry dictionaryEntry : matchingDictionaryEntries) {
            matchingWords.add(dictionaryEntry.getWord());
        }
        return matchingWords;
    }


    /**
     * @param match
     * @return
     */
    public List<String> prefixSearchLinear(String match) {

        primarySuggestions = new ArrayList<>();
        this.textWritten = match;
        for (DictionaryEntry currentEntry : dictionaryEntries) {
            String currentWord = currentEntry.getWord();
            if (currentWord.startsWith(match) && !currentWord.equals(match)) {
                primarySuggestions.add(currentEntry);
            }
        }
        ListSorter.sortList(primarySuggestions, SortingOrder.FREQUENCY_HIGH_TO_LOW);
        return extractWords(primarySuggestions);
    }

    /**
     * Adds a new word to the dictionary.
     *
     * @param word              Word to be added.
     * @param standardFrequency The standard usage frequency.
     * @param userFrequency     The user specific usage frequency.
     * @return 1 if word was successfully added. -1 if there was an error.
     */
    public int addWordToDictionary(String word, int standardFrequency, int userFrequency) {

        // First check if word already exists.
        for (DictionaryEntry dictionaryEntry : this.dictionaryEntries) {
            if (dictionaryEntry.getWord().equals(word)) {
                // Entry already exists.
                return -1;
            }
        }
        DictionaryEntry newEntry = new DictionaryEntry(word, standardFrequency, userFrequency);
        dictionaryEntries.add(newEntry);
        sortDictionary();
        // TODO: Verify that everything works.
        return 1;
    }


    public List<DictionaryEntry> getDictionaryEntries() {
        return dictionaryEntries;
    }

    /**
     * Use with a list that contains duplicate entries to fix them.
     * Note:dictionary.txt doesn't contain duplicates, but other lists might
     */
    private void fixDuplicateEntries() {
        ListSorter.sortList(dictionaryEntries, SortingOrder.ALPHABETICALLY_A_TO_Z);
        DictionaryEntry previousEntry = null;
        for (int i = 0; i < dictionaryEntries.size(); i++) {
            DictionaryEntry currentEntry = dictionaryEntries.get(i);
            if (previousEntry != null && previousEntry.getWord().equals(currentEntry.getWord())) {
                previousEntry.setUserFrequency(previousEntry.getUserFrequency() + currentEntry.getUserFrequency());
                dictionaryEntries.remove(currentEntry);
                i--;
            } else {
                previousEntry = currentEntry;
            }
        }

        ListSorter.sortList(dictionaryEntries, preferredOrder);
    }

    public SortingOrder getPreferredOrder() {
        return preferredOrder;
    }

    public void setPreferredOrder(SortingOrder preferredOrder) {
        this.preferredOrder = preferredOrder;
    }

    /**
     * Finds suggestions starting with or containing whats already written
     *
     * @param textWritten Currently written text
     */
    public void findSuggestions(String textWritten) {

        primarySuggestions = new ArrayList<>();
        secondarySuggestions = new ArrayList<>();
        this.textWritten = textWritten;

        for (DictionaryEntry currentEntry : dictionaryEntries) {
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

        for (DictionaryEntry currentEntry : dictionaryEntries) {
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
    private void printSuggestions(List<DictionaryEntry> suggestions, String suggestionType) {
        System.out.println(suggestionType + " suggestion for: \"" + textWritten + "\"");
        printList(suggestions);
    }

    /**
     * Prints all the words in the dictionary.
     */
    public void printDictionary() {
        printList(dictionaryEntries);
    }


    /**
     * Prints a list with the word and frequency
     *
     * @param list
     */
    public void printList(List<DictionaryEntry> list) {
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
    public int findWordIndex(List<DictionaryEntry> list, String targetWord) {
        for (int i = 0; i < list.size(); i++) {
            String currentWord = list.get(i).getWord();
            if (currentWord.equals(targetWord)) {
                return i;
            }
        }
        return -1;
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

    public List<String> extractWords(List<DictionaryEntry> list) {
        ArrayList<String> suggestions = new ArrayList<>();
        for (DictionaryEntry entry : list) {
            suggestions.add(entry.getWord());
        }
        return suggestions;
    }


    /**
     * Returns a list of words that starts with the match string.
     *
     * @param match the string to match against the start of the words
     * @return a list of suggested words
     */
    public List<String> getSuggestionsStartingWith(String match) {
        return prefixSearchBinary(match);
    }


    /**
     * Should be called every time a word is used to update usage statistics.
     *
     * @param string the word that was used and should be updated
     */
    public void updateWordUsage(String string) {
        boolean wordExist = false;
        for (DictionaryEntry dictionaryEntry : this.dictionaryEntries) {
            if (dictionaryEntry.getWord().equals(string)) {
                dictionaryEntry.setUserFrequency(dictionaryEntry.getUserFrequency() + 1);
                wordExist = true;
            }
            if (!wordExist) {
                // Word does not exist in dictionary. Add it.
//                addWordToDictionary(string, 0, 1);
            }
        }
    }

    /**
     * @param dictionary
     */
    public void setDictionary(List<DictionaryEntry> dictionary) {
        this.dictionaryEntries = dictionary;
        sortDictionary();
    }

}
