package no.ntnu.stud.avikeyb.backend.dictionary;

import java.util.ArrayList;

/**
 * Handles the dictionary features.
 *
 * @author Kristian Honningsvag.
 */
public class Dictionary {

    private ArrayList<String> dictionaryWords;

//    /**
//     * TODO: This is just for testing. Remove later.
//     */
//    public static void main(String[] args) {
//        Dictionary dictionary = new Dictionary();
//    }


    /**
     * Constructor.
     */
    public Dictionary() {
        dictionaryWords = ResourceLoader.loadDictionaryFromFile("./res/word.list");
//        prefixSearch("aa");
    }


    /**
     * Performs a prefix search on the dictionary, returning a list of all words that begin with the
     * prefix.
     *
     * @param prefix The prefix
     * @return All words that begin with the prefix.
     */
    public ArrayList<String> prefixSearch(String prefix) {

        ArrayList<String> foundWords = new ArrayList<String>();
        String foundWord = null;

        int firstLocation = 0;
        int lastLocation = dictionaryWords.size() - 1;
        int middleLocation = (firstLocation + lastLocation) / 2;

        // Find first occurrence of the prefix in dictionary.
        while (firstLocation + 1 < lastLocation) {
//            System.out.println("firstLocation:" + firstLocation + " middleLocation:" + middleLocation + " lastLocation:" + lastLocation);
            // Get the dictionary word at the current location, and find out if we need to search backwards or forwards.
            foundWord = dictionaryWords.get(middleLocation);
            int delta = prefix.compareTo(foundWord);
            if (delta < 0) {
                // Target might be behind us.
                lastLocation = middleLocation;
                middleLocation = (firstLocation + lastLocation) / 2;
            } else if (delta > 0) {
                // Target might be ahead of us.
                firstLocation = middleLocation;
                middleLocation = (firstLocation + lastLocation) / 2;
            } else if (delta == 0) {
                // Landed directly on the first occurrence.
                break;
            }
        }

        // Iterate forwards until we find all the matches.
        boolean allFound = false;
        while (!allFound && middleLocation < dictionaryWords.size()) {
            if (foundWord.startsWith(prefix)) {
                foundWords.add(foundWord);
                middleLocation += 1;
                foundWord = dictionaryWords.get(middleLocation);
            } else {
                // There are now more matches.
                allFound = true;
            }
        }

//        for (String word : foundWords) {
//            System.out.println(word);
//        }
        return foundWords;
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

}
