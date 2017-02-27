package no.ntnu.stud.avikeyb.backend.dictionary;

import java.util.List;

import no.ntnu.stud.avikeyb.backend.Dictionary;

/**
 * Created by pitmairen on 27/02/2017.
 */

public class StandardTestsBinaryDictionary extends DictionaryTester {
    @Override
    protected Dictionary createDictionary(List<DictionaryEntry> entries) {
        return new BinaryDictionary(entries);
    }
}
