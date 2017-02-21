package no.ntnu.stud.avikeyb.backend.dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Handles loading resources.
 *
 * @author Kristian Honningsvag.
 */
public class ResourceLoader {

    /**
     * Loads a dictionary from a text file. And returns it's contents as an array of strings.
     *
     * @param filePath File path of the file containing the dictionary.
     * @return The contents of the file as an array of strings.
     */
    public static ArrayList<String> loadDictionaryFromFile(String filePath) {

        ArrayList<String> dictionary = new ArrayList<String>();
//        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));

            String line;
            // Read each line from the file and add them to the dictionary.
            while ((line = bufferedReader.readLine()) != null) {
                dictionary.add(line.toLowerCase());
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

}
