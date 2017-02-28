package no.ntnu.stud.avikeyb.backend.core;

import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Suggestions;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryHandler;

/**
 * Simple single threaded implementation of the suggestion engine that runs the
 * query in the main thread.
 */
public class SingleThreadSuggestions extends Suggestions {

    private DictionaryHandler dictionaryHandler;

    public SingleThreadSuggestions(Keyboard keyboard, DictionaryHandler dictionaryHandler) {
        super(keyboard);
        this.dictionaryHandler = dictionaryHandler;
    }

    @Override
    protected void executeQuery(String word) {
        notifyListeners(dictionaryHandler.getSuggestionsStartingWith(word));
    }
}
