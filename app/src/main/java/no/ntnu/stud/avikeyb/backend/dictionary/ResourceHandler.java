package no.ntnu.stud.avikeyb.backend.dictionary;

import android.nfc.FormatException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Handles loading resources.
 *
 * @author Kristian Honningsvag.
 */
public class ResourceHandler {

    /**
     * Loads a dictionary from a text file, and returns it's contents as an array of dictionary entries.
     * The returned list is in alphabetically ordering.
     *
     * @param filePath File path of the file containing the dictionary.
     * @return The contents of the file as an array of strings.
     */
    public static List<DictionaryEntry> loadDictionaryFromFile(String filePath) throws FormatException, NumberFormatException, IOException {
        return loadDictionaryFromStream(new FileInputStream(filePath));
    }

    /**
     * Loads a dictionary from an input stream, and returns it's contents as an array of dictionary entries.
     * The returned list is in alphabetically ordering.
     *
     * @param inputStream The input stream containing the dictionary.
     * @return The contents of the stream as an array dictionary entries.
     */
    public static List<DictionaryEntry> loadDictionaryFromStream(InputStream inputStream) throws FormatException, NumberFormatException, IOException {

        ArrayList<DictionaryEntry> dictionary = new ArrayList<>();
        BufferedReader bufferedReader = null;
        String line;
        String[] parts;
        int standardFrequency = 0;
        int userFrequency = 0;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Read each line and add them to the dictionary.
            while ((line = bufferedReader.readLine()) != null) {
                parts = line.split(" ");  // Split at whitespace to check if line contains usage frequency.

                if (parts.length == 1) {  // Assume format: (word)
                    dictionary.add(new DictionaryEntry(line.toLowerCase(), 0, 0));  // Set all frequencies to zero since there is no data.
                } else if (parts.length == 2) {  // Assume format: (word, standardFrequency)
                    try {
                        standardFrequency = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException ex) {
                        System.err.println("Incorrect dictionary format detected. Frequency element on line is not an integer.");
                        ex.printStackTrace();
                        throw ex;
                    }
                    dictionary.add(new DictionaryEntry(parts[0].toLowerCase(), standardFrequency, 0));
                } else if (parts.length == 3) {  // Assume format: (word, standardFrequency, userFrequency)
                    try {
                        standardFrequency = Integer.parseInt(parts[1]);
                        userFrequency = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException ex) {
                        System.err.println("Incorrect dictionary format detected. Frequency element on line is not an integer.");
                        ex.printStackTrace();
                        throw ex;
                    }
                    dictionary.add(new DictionaryEntry(parts[0].toLowerCase(), standardFrequency, userFrequency));
                } else {
                    FormatException ex = new FormatException();
                    System.err.println("Incorrect dictionary format detected. More than 3 elements on a line.");
                    ex.printStackTrace();
                    throw ex;
                }
            }
            bufferedReader.close();

        } catch (IOException ex) {
            System.err.println("Failed when attempting to read from input stream.");
            ex.printStackTrace();
            throw ex;
        }

        // Sort the dictionary alphabetically.
        Collections.sort(dictionary, new Comparator<DictionaryEntry>() {
            @Override
            public int compare(DictionaryEntry o1, DictionaryEntry o2) {
                return o1.getWord().compareToIgnoreCase(o2.getWord());
            }
        });
        return dictionary;
    }


    /**
     * Stores an in memory dictionary at the specified location.
     *
     * @param dictionary
     * @param filePath
     * @return
     */
    public static int storeDictionary(Dictionary dictionary, String filePath) throws IOException {

//        try {
//            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
//            for (DictionaryEntry dictionaryEntry : dictionary) {
//                writer.println(dictionaryEntry.getWord() + " "
//                        + dictionaryEntry.getStandardFrequency() + " "
//                        + dictionaryEntry.getUserFrequency());
//                writer.close();
//            }
//        } catch (IOException ex) {
//            throw ex;
//        }

        return 1;
    }

}