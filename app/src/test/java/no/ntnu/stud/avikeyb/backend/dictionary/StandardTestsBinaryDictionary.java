package no.ntnu.stud.avikeyb.backend.dictionary;

import java.util.Collections;
import java.util.List;

/**
 * Created by pitmairen on 27/02/2017.
 */

public class StandardTestsBinaryDictionary extends DictionaryTester {
    @Override
    protected Dictionary createDictionary(List<DictionaryEntry> entries) {
        // The binary dictionary expects the entries to be sorted alphabetically
        Collections.sort(entries, (e1,  e2) -> e1.getWord().compareTo(e2.getWord()));
        return new Dictionary(entries);
    }
}
