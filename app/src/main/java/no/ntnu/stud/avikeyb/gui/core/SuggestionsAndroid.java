package no.ntnu.stud.avikeyb.gui.core;

import android.os.AsyncTask;

import java.util.List;

import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Suggestions;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryHandler;

/**
 * WordHistory engine implementation that runs the query in a async task to prevent
 * blocking of the GUI.
 */
public class SuggestionsAndroid extends Suggestions {

    // Remember the current task so it can be canceled if a new search is started
    // before the previous one is done

    private AsyncTask<String, Void, List<String>> currentTask;
    private DictionaryHandler dictionaryHandler;

    public SuggestionsAndroid(Keyboard keyboard, DictionaryHandler dictionaryHandler) {
        super(keyboard);
        this.dictionaryHandler = dictionaryHandler;
    }


    @Override
    public void executeQuery(String word) {

        if (currentTask != null) {
            currentTask.cancel(false);
        }

        currentTask = new AsyncTask<String, Void, List<String>>() {

            @Override
            protected List<String> doInBackground(String... params) {
                return dictionaryHandler.getSuggestionsStartingWith(params[0]);
            }

            @Override
            protected void onPostExecute(List<String> suggestions) {
                notifyListeners(suggestions);
                currentTask = null;
            }
        };
        currentTask.execute(word);
    }
}
