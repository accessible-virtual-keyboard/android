package no.ntnu.stud.avikeyb.gui.core;

import android.content.Context;

import java.util.List;

import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryEntry;
import no.ntnu.stud.avikeyb.backend.dictionary.ResourceLoader;

/**
 * Android specific resource loader
 */
public class AndroidResourceLoader {

    /**
     * Loads a dictionary from the provided file resource id
     *
     * @param context    a context object
     * @param resourceId the resource id of the file to load
     * @return a list of dictionary entries
     */
    public static List<DictionaryEntry> loadDictionaryFromResource(Context context, int resourceId) {
        return ResourceLoader.loadDictionaryFromStream(context.getResources().openRawResource(resourceId));
    }
}
