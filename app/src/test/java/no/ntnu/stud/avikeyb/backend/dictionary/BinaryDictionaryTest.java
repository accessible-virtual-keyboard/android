package no.ntnu.stud.avikeyb.backend.dictionary;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Unit tests for the binary dictionary.
 * <p>
 * Created by Kristian Honningsvag.
 */

public class BinaryDictionaryTest {

    private BinaryDictionary binaryDictionary;
    List<String> expectedOutputs;

    @Before
    public void setUp() {
        binaryDictionary = new BinaryDictionary(new DictionaryFileLoader("./src/main/res/raw/word.list").loadDictionary());  // Working directory for test is: /AccessibleVirtualKeyboard/AViKEYB/app
    }

    /**
     * Test sending in invalid input.
     */
    @Test
    public void testInvalidInput() {
        expectedOutputs = Arrays.asList();
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch(""));
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("1239461598247264023421552"));
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("./!@#$%^&*("));
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch(null));
    }

    /**
     * Test when expecting a single result in return.
     */
    @Test
    public void testSingleResults() {
        expectedOutputs = Arrays.asList("enormousnesses");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("enormousness"));

        expectedOutputs = Arrays.asList("brattishnesses");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("brattishness"));

        expectedOutputs = Arrays.asList("patrializations");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("patrialization"));
    }


    /**
     * Test when expecting multiple results in return.
     */
    @Test
    public void testMultipleResults() {
        expectedOutputs = Arrays.asList("isoclinal", "isoclinally", "isoclinals", "isocline", "isoclines", "isoclinic", "isoclinics");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("isocl"));

        expectedOutputs = Arrays.asList("teniacide", "teniacides", "teniae", "teniafuge", "teniafuges", "tenias", "teniases", "teniasis");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("tenia"));

        expectedOutputs = Arrays.asList("sarment", "sarmenta", "sarmentaceous", "sarmentose", "sarmentous", "sarments", "sarmentum");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("sarmen"));
    }


    /**
     * Test with a word not in dictionary.
     */
    @Test
    public void testNotInDictionary() {
        expectedOutputs = Arrays.asList();
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("kkxkkxkfkkdfksdfkd"));
    }


    /**
     * Test with the first entry in dictionary.
     */
    @Test
    public void testFirstEntry() {
        expectedOutputs = Arrays.asList("aah", "aahed", "aahing", "aahs", "aal", "aalii", "aaliis", "aals", "aardvark", "aardvarks", "aardwolf", "aardwolves", "aargh", "aarrgh", "aarrghh", "aas", "aasvogel", "aasvogels");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("aa"));
    }

    /**
     * Test cases where end of dictionary is reached.
     */
    @Test
    public void testEndReached() {
        expectedOutputs = Arrays.asList("zyzzyva", "zyzzyvas");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("zyz"));

        expectedOutputs = Arrays.asList();
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("zyzzyvas"));
    }

}
