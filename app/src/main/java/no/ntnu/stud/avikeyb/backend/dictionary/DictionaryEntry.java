package no.ntnu.stud.avikeyb.backend.dictionary;

/**
 * Help class used for Dictionary class.
 * Created by Tor-Martin Holen on 01-Feb-17 (Originally 21-Feb-17).
 */
public class DictionaryEntry{
    private String word;
    private int frequency;

    public DictionaryEntry(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}