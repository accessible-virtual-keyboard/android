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
        binaryDictionary = new BinaryDictionary(ResourceLoader.loadDictionaryFromFile("./src/main/res/raw/word.list"));  // Working directory for test is: /AccessibleVirtualKeyboard/AViKEYB/app
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
        expectedOutputs = Arrays.asList("dystrophications");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("dystrophications"));

        expectedOutputs = Arrays.asList("aberrational");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("aberrational"));

        expectedOutputs = Arrays.asList("podzolizations");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("podzolizations"));
    }


    /**
     * Test when expecting multiple results in return.
     */
    @Test
    public void testMultipleResults() {
        expectedOutputs = Arrays.asList("isoclinal", "isoclinally", "isoclinals", "isocline", "isoclines", "isoclinic", "isoclinics");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("isocl"));

        expectedOutputs = Arrays.asList("tenia", "teniacide", "teniacides", "teniae", "teniafuge", "teniafuges", "tenias", "teniases", "teniasis");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("tenia"));
    }

    /**
     * Test if a partial prefix gives the same result as using the whole first returned word as the prefix.
     * This should only be the case if all the returned words starts with whole first word.
     */
    @Test
    public void testPartialPrefix() {
        expectedOutputs = Arrays.asList("sarment", "sarmenta", "sarmentaceous", "sarmentose", "sarmentous", "sarments", "sarmentum");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("sarmen"));
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("sarment"));
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
        expectedOutputs = Arrays.asList("aa", "aah", "aahed", "aahing", "aahs", "aal", "aalii", "aaliis", "aals", "aardvark", "aardvarks", "aardwolf", "aardwolves", "aargh", "aarrgh", "aarrghh", "aas", "aasvogel", "aasvogels");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("aa"));
    }

    /**
     * Test cases where end of dictionary is reached.
     */
    @Test
    public void testEndReached() {
        expectedOutputs = Arrays.asList("zyzzyva", "zyzzyvas");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("zyz"));

        expectedOutputs = Arrays.asList("zyzzyvas");
        assertEquals(expectedOutputs, binaryDictionary.prefixSearch("zyzzyvas"));
    }

}
