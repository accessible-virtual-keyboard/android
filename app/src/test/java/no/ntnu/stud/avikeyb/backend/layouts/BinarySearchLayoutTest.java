package no.ntnu.stud.avikeyb.backend.layouts;

import org.junit.Before;
import org.junit.Test;

import no.ntnu.stud.avikeyb.backend.InputInterface;
import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.core.CoreKeyboard;
import no.ntnu.stud.avikeyb.backend.outputs.OutputLogger;

import static org.junit.Assert.assertEquals;

/**
 * Created by pitmairen on 08/02/2017.
 */
public class BinarySearchLayoutTest {

    private CoreKeyboard keyboard;
    private OutputLogger output;
    private InputInterface layout;

    @Before
    public void setUp() throws Exception {

        keyboard = new CoreKeyboard();
        output = new OutputLogger();
        keyboard.addOutputDevice(output);
        layout = new BinarySearchLayout(keyboard);
    }


    @Test
    public void testSelectLetters() throws Exception {

        assertEquals("", keyboard.getCurrentBuffer());
        assertEquals("", output.getLastOutput());

        // a
        goLeft();
        goLeft();
        goLeft();
        goLeft();

        assertEquals("a", keyboard.getCurrentBuffer());
        assertEquals("", output.getLastOutput());

        // h
        goLeft();
        goRight();
        goLeft();
        goLeft();
        goLeft();

        assertEquals("ah", keyboard.getCurrentBuffer());
        assertEquals("", output.getLastOutput());

        // a
        goLeft();
        goLeft();
        goLeft();
        goLeft();

        assertEquals("aha", keyboard.getCurrentBuffer());
        assertEquals("", output.getLastOutput());

        // Send
        goRight();
        goRight();
        goRight();
        goRight();
        goRight();

        assertEquals("aha", output.getLastOutput());
        assertEquals("", keyboard.getCurrentBuffer());
    }


    private void goLeft() {
        layout.setInputState(InputType.INPUT1, true);
        layout.setInputState(InputType.INPUT1, false);
    }

    private void goRight() {
        layout.setInputState(InputType.INPUT2, true);
        layout.setInputState(InputType.INPUT2, false);
    }
}