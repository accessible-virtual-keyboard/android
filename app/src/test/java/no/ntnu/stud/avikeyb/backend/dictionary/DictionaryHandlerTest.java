package no.ntnu.stud.avikeyb.backend.dictionary;

import android.nfc.FormatException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Unit tests for the binary dictionary.
 * <p>
 * Created by Kristian Honningsvag.
 */

public class DictionaryHandlerTest {

    private DictionaryHandler dictionaryHandlerNoFrequency;
    private DictionaryHandler dictionaryHandlerWithOneFrequency;
    private DictionaryHandler dictionaryHandlerWithTwoFrequency;
    List<String> expectedOutputs;

    /**
     * Working directory for tests are: /AccessibleVirtualKeyboard/AViKEYB/app
     */
    @Before
    public void setUp() {
//        System.out.println(getClass().getClassLoader().getResourceAsStream("raw/word.list"));
        dictionaryHandlerNoFrequency = new DictionaryHandler(new DictionaryFileLoader("./src/main/res/raw/word.list").loadDictionary());
        dictionaryHandlerWithOneFrequency = new DictionaryHandler(new DictionaryFileLoader("./src/main/res/raw/dictionary.txt").loadDictionary());
        dictionaryHandlerWithTwoFrequency = new DictionaryHandler(new DictionaryFileLoader("./src/main/res/raw/test_dictionary.txt").loadDictionary());
    }

    /**
     * Test sending in invalid input.
     */
    @Test
    public void testInvalidInput() {
        expectedOutputs = Arrays.asList();
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary(""));
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("1239461598247264023421552"));
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("./!@#$%^&*("));
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary(null));
    }

    /**
     * Test when expecting a single result in return.
     */
    @Test
    public void testSingleResults() {
        expectedOutputs = Arrays.asList("enormousnesses");
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("enormousness"));

        expectedOutputs = Arrays.asList("brattishnesses");
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("brattishness"));

        expectedOutputs = Arrays.asList("patrializations");
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("patrialization"));
    }


    /**
     * Test when expecting multiple results in return.
     */
    @Test
    public void testMultipleResults() {
        expectedOutputs = Arrays.asList("isoclinal", "isoclinally", "isoclinals", "isocline", "isoclines", "isoclinic", "isoclinics");
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("isocl"));

        expectedOutputs = Arrays.asList("teniacide", "teniacides", "teniae", "teniafuge", "teniafuges", "tenias", "teniases", "teniasis");
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("tenia"));

        expectedOutputs = Arrays.asList("sarment", "sarmenta", "sarmentaceous", "sarmentose", "sarmentous", "sarments", "sarmentum");
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("sarmen"));
    }


    /**
     * Test with a word not in dictionary.
     */
    @Test
    public void testNotInDictionary() {
        expectedOutputs = Arrays.asList();
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("kkxkkxkfkkdfksdfkd"));
    }


    /**
     * Test with the first entry in dictionary.
     */
    @Test
    public void testFirstEntry() {
        expectedOutputs = Arrays.asList("aah", "aahed", "aahing", "aahs", "aal", "aalii", "aaliis", "aals", "aardvark", "aardvarks", "aardwolf", "aardwolves", "aargh", "aarrgh", "aarrghh", "aas", "aasvogel", "aasvogels");
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("aa"));
    }

    /**
     * Test cases where end of dictionary is reached.
     */
    @Test
    public void testEndReached() {
        expectedOutputs = Arrays.asList("zyzzyva", "zyzzyvas");
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("zyz"));

        expectedOutputs = Arrays.asList();
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("zyzzyvas"));
    }

    /**
     * Test with a dictionary that has one set of frequencies in addition to the words.
     */
    @Test
    public void testWithOneFrequencyDictionary() {
        expectedOutputs = Arrays.asList("you", "your", "you're", "yeah", "yes", "you've");
        assertEquals(expectedOutputs, dictionaryHandlerWithOneFrequency.prefixSearchBinary("y").subList(0, 6));
    }

    /**
     * Test with a dictionary that has both standard and user frequencies in addition to the words.
     */
    @Test
    public void testWithTwoFrequencyDictionary() {
        expectedOutputs = Arrays.asList("you're", "yeah", "you", "your", "yes");
        assertEquals(expectedOutputs, dictionaryHandlerWithTwoFrequency.prefixSearchBinary("y").subList(0, 5));
    }

    /**
     * Test adding a word.
     */
    @Test
    public void testAddingWord() {
        expectedOutputs = Arrays.asList("zzzbkldiutgudzxkeudz");
        dictionaryHandlerNoFrequency.addWordToDictionary("zzzbkldiutgudzxkeudz", 0, 1);
        assertEquals(expectedOutputs, dictionaryHandlerNoFrequency.prefixSearchBinary("zzzbkldiutgudzxkeud").subList(0, 1));
    }

//    @Test
//    public void standardizeDictionary() {
//        DictionaryHandler dictionaryHandler = new DictionaryHandler();
//        try {
//            dictionaryHandler.setDictionary(ResourceHandler.loadDictionaryFromFile("./src/main/res/dictionary/dictionary.txt"));
//            dictionaryHandler.mergeDuplicateEntries();
//            ResourceHandler.storeDictionaryToFile(dictionaryHandler.getDictionary(), "./src/main/res/dictionary/dictionary.txt");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

}
