package no.ntnu.stud.avikeyb.backend.dictionary;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Unit tests for the resource handler.
 * <p>
 * Created by Kristian Honningsvag.
 */

public class ResourceHandlerTest {

    private InMemoryDictionary dictionary;

    /**
     * Working directory for tests are: /AccessibleVirtualKeyboard/AViKEYB/app
     */
    @Before
    public void setUp() {
        dictionary = new BinaryDictionary(new DictionaryFileLoader("./src/main/res/raw/word.list").loadDictionary());
    }

    /**
     * Test storing a loaded dictionary.
     */
    @Test
    public void testInvalidInput() {
//        expectedOutputs = Arrays.asList();
//        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary(""));
//        ResourceHandler.storeDictionary(dictionary, "")
    }

}
