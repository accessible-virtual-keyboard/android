package no.ntnu.stud.avikeyb.gui.core;

import java.util.Arrays;
import java.util.List;

/**
 * Dummy dictionary that always returns the same suggestions
 */
public class DummyDictionary {

    public List<String> getSuggestionsStartingWith(String query) {
        return Arrays.asList(query, "test1", "test2");
    }


    public void updateWordUsage(String string) {
    }
}
