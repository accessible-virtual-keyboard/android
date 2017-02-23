package no.ntnu.stud.avikeyb.backend.dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading resources.
 *
 * @author Kristian Honningsvag.
 */
public class ResourceLoader {

    /**
     * Loads a dictionary from a text file. And returns it's contents as an array of strings.
     * Loader expects a single word per line.
     *
     * @param filePath File path of the file containing the dictionary.
     * @return The contents of the file as an array of strings.
     */
    public static List<DictionaryEntry> loadDictionaryFromFile(String filePath) {

        ArrayList<DictionaryEntry> dictionary = new ArrayList<DictionaryEntry>();
//        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));

            String line;
            // Read each line from the file and add them to the dictionary.
            while ((line = bufferedReader.readLine()) != null) {
                dictionary.add(new DictionaryEntry(line.toLowerCase(), 0));
//                stringBuilder.append(line).append("\n");  // Also manually adding newline.
            }
            bufferedReader.close();

        } catch (Exception ex) {
            System.err.println("Failed when attempting to read from " + filePath);
            ex.printStackTrace();
//            System.exit(1);
        }
        return dictionary;
    }

    /**
     * Loads a dictionary from an input stream, and returns it's contents as an array of dictionary
     * entries.
     *
     * @param inputStream The input stream containing the dictionary.
     * @return The contents of the stream as an array dictionary entries.
     */
    public static List<DictionaryEntry> loadDictionaryFromStream(InputStream inputStream) {

        ArrayList<DictionaryEntry> dictionary = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            // Read each line and add them to the dictionary.
            while ((line = bufferedReader.readLine()) != null) {
                dictionary.add(new DictionaryEntry(line.toLowerCase(), 0));
            }
            bufferedReader.close();

        } catch (Exception ex) {
            System.err.println("Failed when attempting to read from input stream");
            ex.printStackTrace();
        }
        return dictionary;
    }

}
