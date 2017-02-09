package no.ntnu.stud.avikeyb.backend.core;

import org.junit.Before;
import org.junit.Test;

import no.ntnu.stud.avikeyb.backend.outputs.OutputLogger;

import static org.junit.Assert.assertEquals;

/**
 * Created by pitmairen on 08/02/2017.
 */
public class CoreKeyboardTest {

    private CoreKeyboard keyboard;

    @Before
    public void setUp() throws Exception {
        keyboard = new CoreKeyboard();
    }


    @Test
    public void testCurrentBuffer() throws Exception {

        assertEquals("", keyboard.getCurrentBuffer());

        keyboard.addToCurrentBuffer("h");
        assertEquals("h", keyboard.getCurrentBuffer());

        keyboard.addToCurrentBuffer("ello");
        assertEquals("hello", keyboard.getCurrentBuffer());

        keyboard.addToCurrentBuffer(", test");
        assertEquals("hello, test", keyboard.getCurrentBuffer());

    }


    @Test
    public void testClearCurrentBuffer() throws Exception {
        assertEquals("", keyboard.getCurrentBuffer());
        keyboard.addToCurrentBuffer("hello");
        assertEquals("hello", keyboard.getCurrentBuffer());
        keyboard.clearCurrentBuffer();
        assertEquals("", keyboard.getCurrentBuffer());
    }

    @Test
    public void testSendCurrentBuffer() throws Exception {

        OutputLogger output = new OutputLogger();

        keyboard.addOutputDevice(output);

        assertEquals("", keyboard.getCurrentBuffer());
        keyboard.addToCurrentBuffer("hello");
        assertEquals("hello", keyboard.getCurrentBuffer());
        keyboard.sendCurrentBuffer();
        assertEquals("", keyboard.getCurrentBuffer());
        assertEquals("hello", output.getLastOutput());

    }


    @Test
    public void testGetCurrentWord() throws Exception {

        assertEquals("", keyboard.getCurrentBuffer());
        assertEquals("", keyboard.getCurrentWord());

        keyboard.addToCurrentBuffer("hello");
        assertEquals("hello", keyboard.getCurrentBuffer());
        assertEquals("hello", keyboard.getCurrentWord());

        keyboard.addToCurrentBuffer(" my na");
        assertEquals("hello my na", keyboard.getCurrentBuffer());
        assertEquals("na", keyboard.getCurrentWord());

        keyboard.addToCurrentBuffer("me");
        assertEquals("hello my name", keyboard.getCurrentBuffer());
        assertEquals("name", keyboard.getCurrentWord());


        keyboard.sendCurrentBuffer();
        assertEquals("", keyboard.getCurrentBuffer());
        assertEquals("", keyboard.getCurrentWord());

    }


    @Test
    public void testAddOutputDevice() throws Exception {

        OutputLogger output = new OutputLogger();
        keyboard.addOutputDevice(output);
        keyboard.addToCurrentBuffer("hello");
        keyboard.sendCurrentBuffer();
        assertEquals("hello", output.getLastOutput());

    }

    @Test
    public void testAddMultipleOutputDevices() throws Exception {

        OutputLogger output1 = new OutputLogger();
        OutputLogger output2 = new OutputLogger();
        keyboard.addOutputDevice(output1);
        keyboard.addOutputDevice(output2);
        keyboard.addToCurrentBuffer("hello");
        keyboard.sendCurrentBuffer();
        assertEquals("hello", output1.getLastOutput());
        assertEquals("hello", output1.getLastOutput());

    }


    @Test
    public void testRemoveOutputDevice() throws Exception {

        OutputLogger output1 = new OutputLogger();
        OutputLogger output2 = new OutputLogger();
        keyboard.addOutputDevice(output1);
        keyboard.addOutputDevice(output2);
        keyboard.removeOutputDevice(output1);
        keyboard.addToCurrentBuffer("hello");
        keyboard.sendCurrentBuffer();
        assertEquals("", output1.getLastOutput());
        assertEquals("hello", output2.getLastOutput());

    }


}