package no.ntnu.stud.avikeyb.gui.core;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryEntry;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryLoader;

/**
 * Loads a dictionary from a file on android
 * <p>
 * The file must be in the following format:
 * word1 frequency1
 * word2 frequency2
 * ...
 *
 */
public class DictionaryFileLoader implements DictionaryLoader {

    private Context context;
    private int dictionaryResourceId;

    public DictionaryFileLoader(Context context, int dictionaryResourceId) {
        this.context = context;
        this.dictionaryResourceId = dictionaryResourceId;
    }

    @Override
    public List<DictionaryEntry> loadDictionary() {
        InputStream inputStream = context.getResources().openRawResource(dictionaryResourceId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        ArrayList<DictionaryEntry> dictionary = new ArrayList<>();
        String currentLine;
        while (true) {
            try {
                currentLine = bufferedReader.readLine().toLowerCase();//Produces null pointer when no more lines
                int separatingIndex = currentLine.indexOf(" ");
                String currentWord = currentLine.substring(0, separatingIndex);
                //System.out.println(currentLine);
                int frequency = Integer.parseInt(currentLine.substring(++separatingIndex).trim());
                int reducedFrequency = Math.round(frequency / 77);
                dictionary.add(new DictionaryEntry(currentWord, reducedFrequency));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                break;
            }
        }
        return dictionary;
    }
}
