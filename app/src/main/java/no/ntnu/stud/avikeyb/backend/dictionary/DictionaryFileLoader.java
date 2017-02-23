package no.ntnu.stud.avikeyb.backend.dictionary;

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
        return ResourceLoader.loadDictionaryFromFile(filePath);
    }

}
