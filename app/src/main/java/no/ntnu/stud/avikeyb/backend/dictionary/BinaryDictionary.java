package no.ntnu.stud.avikeyb.backend.dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.Dictionary;

/**
 * Dictionary that uses binary search to find matches.
 *
 * @author Kristian Honningsvag.
 */
public class BinaryDictionary implements Dictionary, InMemoryDictionary {

    private List<DictionaryEntry> dictionaryEntries;


    /**
     * Constructs a dictionary with no entries.
     */
    public BinaryDictionary() {
        this(new ArrayList<DictionaryEntry>());
    }

    /**
     * Constructs the dictionary.
     *
     * @param dictionary
     */
    public BinaryDictionary(List<DictionaryEntry> dictionary) {
        this.dictionaryEntries = dictionary;
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
    public List<String> prefixSearch(String prefix) {

        // Return empty list if invalid input.
        if (prefix == null || prefix.equalsIgnoreCase("")) {
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
        Collections.sort(matchingDictionaryEntries, new Comparator<DictionaryEntry>() {
            @Override
            public int compare(DictionaryEntry o1, DictionaryEntry o2) {
                return o2.getFrequency() - o1.getFrequency();
            }
        });

        // Get only the words.
        for (DictionaryEntry dictionaryEntry : matchingDictionaryEntries) {
            matchingWords.add(dictionaryEntry.getWord());
        }
        return matchingWords;
    }

    /**
     * Takes to characters, and returns their position relative to each other in the alphabet.
     *
     * @param firstCharacter
     * @param secondCharacter
     * @return 0 if it is the same letter. Positive if the first letter comes after the second in
     * the alphabet. Negative if the if the first letter comes before the second in the alphabet.
     */
    private int findRelativeLocation(Character firstCharacter, Character secondCharacter) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        return alphabet.lastIndexOf(firstCharacter) - alphabet.lastIndexOf(secondCharacter);
    }

    @Override
    public List<String> getSuggestionsStartingWith(String match) {
        return prefixSearch(match);
    }

    @Override
    public void updateWordUsage(String string) {
        for (DictionaryEntry dictionaryEntry : dictionaryEntries) {
            if (dictionaryEntry.getWord().equals(string)) {
                dictionaryEntry.setFrequency(dictionaryEntry.getFrequency() + 1);
            }
        }
    }

    @Override
    public void setDictionary(List<DictionaryEntry> dictionary) {
        this.dictionaryEntries = dictionary;
    }
}
