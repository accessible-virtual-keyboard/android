package no.ntnu.stud.avikeyb.backend.dictionary;


import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.Dictionary;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by pitmairen on 22/02/2017.
 */
public class LinearDictionaryTest {

    private LinearDictionary dictionary;

    @Before
    public void setUp() throws Exception {

        dictionary = new LinearDictionary(Arrays.asList(
                de("test11", 8),
                de("test2", 7),
                de("test3", 6),
                de("hello1", 5),
                de("hello2", 4),
                de("hello3", 3),
                de("testing4", 2),
                de("testing55", 1)
        ));
    }

    @Test
    public void testEmptyDictionary() throws Exception {
        Dictionary dictionary = new LinearDictionary(Collections.emptyList());
        List<String> res = dictionary.getSuggestionsStartingWith("test");
        assertThat(res, is(Collections.emptyList()));
    }


    @Test
    public void testSingleElementNoMatch() throws Exception {
        Dictionary dictionary = new LinearDictionary(Arrays.asList(de("test", 0)));
        List<String> res = dictionary.getSuggestionsStartingWith("hello");
        assertThat(res, is(Collections.emptyList()));
    }

    /**
     * Word longer than the word in dictionary
     */
    @Test
    public void testSingleElementNoMatchTooLong() throws Exception {
        Dictionary dictionary = new LinearDictionary(Arrays.asList(de("test", 0)));
        List<String> res = dictionary.getSuggestionsStartingWith("testing");
        assertThat(res, is(Collections.emptyList()));
    }

    @Test
    public void testSingleElementWithMatch() throws Exception {
        Dictionary dictionary = new LinearDictionary(Arrays.asList(de("test", 0)));
        List<String> res = dictionary.getSuggestionsStartingWith("te");
        assertThat(res, is(Arrays.asList("test")));
    }

    @Test
    public void testDontIncludePerfectMatch() throws Exception {
        Dictionary dictionary = new LinearDictionary(Arrays.asList(de("test", 0)));
        // perfect match
        List<String> res = dictionary.getSuggestionsStartingWith("test");
        assertThat(res, is(Collections.emptyList()));
    }

    @Test
    public void testMultipleWords() throws Exception {

        List<String> res = dictionary.getSuggestionsStartingWith("te");
        assertThat(res, is(Arrays.asList("test11", "test2", "test3", "testing4", "testing55")));

        res = dictionary.getSuggestionsStartingWith("testing");
        assertThat(res, is(Arrays.asList("testing4", "testing55")));


        res = dictionary.getSuggestionsStartingWith("testing6");
        assertThat(res, is(Collections.emptyList()));


        // Don't include perfect match
        res = dictionary.getSuggestionsStartingWith("test2");
        assertThat(res, is(Collections.emptyList()));

        res = dictionary.getSuggestionsStartingWith("hel");
        assertThat(res, is(Arrays.asList("hello1", "hello2", "hello3")));
    }


    @Test
    public void testMatchLastWord() throws Exception {
        List<String> res = dictionary.getSuggestionsStartingWith("testing5");
        assertThat(res, is(Arrays.asList("testing55")));
    }


    @Test
    public void testMatchFirstWord() throws Exception {
        List<String> res = dictionary.getSuggestionsStartingWith("test1");
        assertThat(res, is(Arrays.asList("test11")));
    }

    @Test
    public void testStandardFrequencies() throws Exception {
        // User frequencies are all 0
        Dictionary dictionary = new LinearDictionary(Arrays.asList(
                de("test1", 1),
                de("test2", 10),
                de("test3", 4),
                de("testing4", 2),
                de("testing5", 2),
                de("testing6", 8),
                de("testing7", 4)
        ));

        List<String> res = dictionary.getSuggestionsStartingWith("test");
        assertThat(res, is(Arrays.asList("test2", "testing6", "test3", "testing7", "testing4", "testing5", "test1")));
    }


    @Test
    public void testUserFrequencies() throws Exception {
        // User frequency should count more than the standard frequency, but the standard frequency
        // should still be used for sorting as well
        Dictionary dictionary = new LinearDictionary(Arrays.asList(
                de("test1", 1, 100),
                de("test2", 10, 0),
                de("test3", 4, 50),
                de("testing4", 2),
                de("testing5", 2),
                de("testing6", 8),
                de("testing7", 4)
        ));

        List<String> res = dictionary.getSuggestionsStartingWith("test");
        assertThat(res, is(Arrays.asList("test1", "test3", "test2", "testing6", "testing7", "testing4", "testing5")));
    }


    private DictionaryEntry de(String word, int frequency) {
        return new DictionaryEntry(word, frequency, 0);
    }

    private DictionaryEntry de(String word, int frequency, int userFreq) {
        return new DictionaryEntry(word, frequency, userFreq);
    }
}