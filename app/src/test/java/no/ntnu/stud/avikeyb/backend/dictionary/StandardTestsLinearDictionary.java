package no.ntnu.stud.avikeyb.backend.dictionary;


import java.util.List;

import no.ntnu.stud.avikeyb.backend.Dictionary;

/**
 * Created by pitmairen on 22/02/2017.
 */
public class StandardTestsLinearDictionary extends DictionaryTester {

    @Override
    protected Dictionary createDictionary(List<DictionaryEntry> entries) {
        return new LinearDictionary(entries);
    }
}