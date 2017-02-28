package no.ntnu.stud.avikeyb.backend.dictionary;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


/**
 * Unit tests for the resource handler.
 * <p>
 * Created by Kristian Honningsvag.
 */

public class ResourceHandlerTest {

    private DictionaryHandler dictionaryHandler;

    /**
     * Working directory for tests are: /AccessibleVirtualKeyboard/AViKEYB/app
     */
    @Before
    public void setUp() {
        dictionaryHandler = new DictionaryHandler(new DictionaryFileLoader("./src/main/res/raw/test_dictionary.txt").loadDictionary());
    }

    /**
     * Test storing a dictionaryHandler that has been loaded into memory.
     */
    @Test
    public void testStoreDictionaryEntries() {
//        expectedOutputs = Arrays.asList();
//        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary(""));
        try {
            ResourceHandler.storeDictionaryToFile(dictionaryHandler.getDictionary(), "/tmp/test_dictionary.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
