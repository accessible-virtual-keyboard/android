package no.ntnu.stud.avikeyb.backend;

/**
 * Interface representing the current state of the keyboard.
 * <p>
 * The keyboard holds an output buffer that is gradually filled as the users types characters. When the user
 * has added the desired input to the buffer the keyboard can send the current buffer to somewhere.
 */
public interface Keyboard {

    /**
     * Adds the string value to the current keyboard output buffer
     *
     * @param value
     */
    void addToCurrentBuffer(String value);

    /**
     * Should send the current buffer to some kind output destination
     */
    void sendCurrentBuffer();


    /**
     * Returns the last word typed or currently being typed in the current output buffer
     *
     * @return the last word in the output buffer, can be an empty string or the start of a partially typed word
     */
    String getCurrentWord();

    /**
     * Returns the whole content of the current output buffer
     *
     * @return the content of the current output buffer
     */
    String getCurrentBuffer();

    /**
     * Clears the current output buffer and reset it to a empty string
     */
    void clearCurrentBuffer();

}
