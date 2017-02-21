package no.ntnu.stud.avikeyb.backend.dictionary;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.Dictionary;

/**
 * Handles the dictionary features.
 *
 * @author Kristian Honningsvag.
 */
public class DictionaryHandler implements Dictionary {

    private ArrayList<String> dictionaryWords;

//    /**
//     * TODO: This is just for testing. Remove later.
//     */
//    public static void main(String[] args) {
//        DictionaryHandler dictionaryHandler = new DictionaryHandler();
//    }


    /**
     * Constructor.
     */
    public DictionaryHandler() {
        dictionaryWords = ResourceLoader.loadDictionaryFromFile("./res/word.list");
//        prefixSearch("zyzz");
    }


    /**
     * Performs a prefix search on the dictionary, returning a list of all words that begin with the
     * prefix.
     *
     * @param prefix The prefix
     * @return All words that begin with the prefix.
     */
    public ArrayList<String> prefixSearch(String prefix) {

        ArrayList<String> matchingWords = new ArrayList<String>();
        String currentWord = null;
        boolean doneSearching = false;

        int firstLocation = 0;
        int lastLocation = dictionaryWords.size() - 1;
        int middleLocation = (firstLocation + lastLocation) / 2;

        // Find the first matching word.
        while (!doneSearching) {

            // Get the dictionary word at the current location.
            currentWord = dictionaryWords.get(middleLocation);
            int delta = prefix.compareToIgnoreCase(currentWord);

            // When there are only two alternatives left.
            if ((lastLocation - firstLocation) <= 1) {
                if (delta > 0) {
                    middleLocation = lastLocation;
                } else if (delta < 0) {
                    middleLocation = firstLocation;
                } else if (delta == 0) {
                    // Do nothing. We are in the correct position.
                }
                currentWord = dictionaryWords.get(middleLocation);
                doneSearching = true;
                break;
            }

            // Find out if we need to search backwards or forwards.
            if (delta < 0) {
                // Target might be behind us.
                lastLocation = middleLocation;
                middleLocation = (firstLocation + lastLocation) / 2;
            } else if (delta > 0) {
                // Target might be ahead of us.
                firstLocation = middleLocation;
                middleLocation = (firstLocation + lastLocation) / 2;
            } else if (delta == 0) {
                // Landed directly on the first matching word.
                doneSearching = true;
            }
//            System.out.println("firstLocation:" + firstLocation + " middleLocation:" + middleLocation + " lastLocation:" + lastLocation);
        }

        // Iterate forwards and collect all the matches.
        boolean allFound = false;
        while (!allFound && middleLocation < dictionaryWords.size() - 1) {
            if (currentWord.startsWith(prefix)) {
                matchingWords.add(currentWord);
                middleLocation += 1;
                currentWord = dictionaryWords.get(middleLocation);
//                System.out.println(currentWord);
            } else {
                // There are now more matches.
                allFound = true;
            }
        }

//        for (String word : matchingWords) {
//            System.out.println(word);
//        }
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
    }

}
