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
public class SimpleExampleLayoutTest {


    private CoreKeyboard keyboard;
    private OutputLogger output;
    private InputInterface layout;

    @Before
    public void setUp() throws Exception {

        keyboard = new CoreKeyboard();

        output = new OutputLogger();

        keyboard.addOutputDevice(output);
        layout = new SimpleExampleLayout(keyboard);
    }

    @Test
    public void testTyping() throws Exception {


        assertEquals("", keyboard.getCurrentBuffer());

        // Move to letter "c" and select
        toggleInput(InputType.INPUT2);
        toggleInput(InputType.INPUT2);
        toggleInput(InputType.INPUT2);
        toggleInput(InputType.INPUT1);


        assertEquals("c", keyboard.getCurrentBuffer());


        // Move to letter "a" and select
        toggleInput(InputType.INPUT2);
        toggleInput(InputType.INPUT1);

        assertEquals("ca", keyboard.getCurrentBuffer());

        // Send button
        toggleInput(InputType.INPUT1);

        assertEquals("ca", output.getLastOutput());
        assertEquals("", keyboard.getCurrentBuffer());
    }


    private void toggleInput(InputType input) {
        layout.setInputState(input, true);
        layout.setInputState(input, false);
    }
}