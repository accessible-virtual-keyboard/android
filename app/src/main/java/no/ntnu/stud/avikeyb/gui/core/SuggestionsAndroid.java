package no.ntnu.stud.avikeyb.gui.core;

import android.os.AsyncTask;

import java.util.List;

import no.ntnu.stud.avikeyb.backend.Dictionary;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Suggestions;

/**
 * WordHistory engine implementation that runs the query in a async task to prevent
 * blocking of the GUI.
 */
public class SuggestionsAndroid extends Suggestions {

    // Remember the current task so it can be canceled if a new seach is started
    // before the previous one is done

    private AsyncTask<String, Void, List<String>> currentTask;
    private Dictionary dictionary;

    public SuggestionsAndroid(Keyboard keyboard, Dictionary dictionary) {
        super(keyboard);
        this.dictionary = dictionary;
    }


    @Override
    public void executeQuery(String word) {

        if (currentTask != null) {
            currentTask.cancel(false);
        }

        currentTask = new AsyncTask<String, Void, List<String>>() {

            @Override
            protected List<String> doInBackground(String... params) {
                return dictionary.getSuggestionsStartingWith(params[0]);
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
