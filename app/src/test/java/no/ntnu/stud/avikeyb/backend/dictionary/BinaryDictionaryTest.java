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

    private BinaryDictionary binaryDictionaryNoFrequency;
    private BinaryDictionary binaryDictionaryWithOneFrequency;
    private BinaryDictionary binaryDictionaryWithTwoFrequency;
    List<String> expectedOutputs;

    /**
     * Working directory for tests are: /AccessibleVirtualKeyboard/AViKEYB/app
     */
    @Before
    public void setUp() {
//        System.out.println(getClass().getClassLoader().getResourceAsStream("raw/word.list"));
        binaryDictionaryNoFrequency = new BinaryDictionary(new DictionaryFileLoader("./src/main/res/raw/word.list").loadDictionary());
        binaryDictionaryWithOneFrequency = new BinaryDictionary(new DictionaryFileLoader("./src/main/res/raw/dictionary.txt").loadDictionary());
        binaryDictionaryWithTwoFrequency = new BinaryDictionary(new DictionaryFileLoader("./src/main/res/raw/test_dictionary.txt").loadDictionary());
    }

    /**
     * Test sending in invalid input.
     */
    @Test
    public void testInvalidInput() {
        expectedOutputs = Arrays.asList();
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary(""));
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("1239461598247264023421552"));
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("./!@#$%^&*("));
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary(null));
    }

    /**
     * Test when expecting a single result in return.
     */
    @Test
    public void testSingleResults() {
        expectedOutputs = Arrays.asList("enormousnesses");
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("enormousness"));

        expectedOutputs = Arrays.asList("brattishnesses");
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("brattishness"));

        expectedOutputs = Arrays.asList("patrializations");
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("patrialization"));
    }


    /**
     * Test when expecting multiple results in return.
     */
    @Test
    public void testMultipleResults() {
        expectedOutputs = Arrays.asList("isoclinal", "isoclinally", "isoclinals", "isocline", "isoclines", "isoclinic", "isoclinics");
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("isocl"));

        expectedOutputs = Arrays.asList("teniacide", "teniacides", "teniae", "teniafuge", "teniafuges", "tenias", "teniases", "teniasis");
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("tenia"));

        expectedOutputs = Arrays.asList("sarment", "sarmenta", "sarmentaceous", "sarmentose", "sarmentous", "sarments", "sarmentum");
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("sarmen"));
    }


    /**
     * Test with a word not in dictionary.
     */
    @Test
    public void testNotInDictionary() {
        expectedOutputs = Arrays.asList();
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("kkxkkxkfkkdfksdfkd"));
    }


    /**
     * Test with the first entry in dictionary.
     */
    @Test
    public void testFirstEntry() {
        expectedOutputs = Arrays.asList("aah", "aahed", "aahing", "aahs", "aal", "aalii", "aaliis", "aals", "aardvark", "aardvarks", "aardwolf", "aardwolves", "aargh", "aarrgh", "aarrghh", "aas", "aasvogel", "aasvogels");
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("aa"));
    }

    /**
     * Test cases where end of dictionary is reached.
     */
    @Test
    public void testEndReached() {
        expectedOutputs = Arrays.asList("zyzzyva", "zyzzyvas");
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("zyz"));

        expectedOutputs = Arrays.asList();
        assertEquals(expectedOutputs, binaryDictionaryNoFrequency.prefixSearchBinary("zyzzyvas"));
    }

    /**
     * Test with a dictionary that has one set of frequencies in addition to the words.
     */
    @Test
    public void testWithOneFrequencyDictionary() {
        expectedOutputs = Arrays.asList("you", "your", "you're", "yeah", "yes", "you've");
        assertEquals(expectedOutputs, binaryDictionaryWithOneFrequency.prefixSearchBinary("y").subList(0, 6));
    }

    /**
     * Test with a dictionary that has both standard and user frequencies in addition to the words.
     */
    @Test
    public void testWithTwoFrequencyDictionary() {
        expectedOutputs = Arrays.asList("you're", "yeah", "you", "your", "yes");
        assertEquals(expectedOutputs, binaryDictionaryWithTwoFrequency.prefixSearchBinary("y").subList(0, 5));
    }

}
