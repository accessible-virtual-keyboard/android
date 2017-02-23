package no.ntnu.stud.avikeyb.backend.dictionary;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading a dictionary from a local file.
 *
 * @author Kristian Honningsvag.
 */
public class DictionaryFileLoader implements DictionaryLoader {

    private String filePath;

    /**
     * Constructs the dictionary loader.
     *
     * @param filePath File path of the file containing the dictionary.
     */
    public DictionaryFileLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<DictionaryEntry> loadDictionary() {
        List<DictionaryEntry> dictionary = new ArrayList<>();
        try {
            dictionary = ResourceLoader.loadDictionaryFromFile(filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Failed loading dictionary from file " + filePath);
            e.printStackTrace();
        }
        return dictionary;
    }

}
