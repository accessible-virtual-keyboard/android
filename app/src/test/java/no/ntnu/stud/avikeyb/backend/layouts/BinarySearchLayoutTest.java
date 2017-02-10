package no.ntnu.stud.avikeyb.backend.layouts;

import org.junit.Test;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Layout;

/**
 * Created by pitmairen on 08/02/2017.
 */
public class BinarySearchLayoutTest extends LayoutTestBase {

    @Override
    protected Layout createLayout() {
        return new BinarySearchLayout(keyboard);
    }

    @Test
    public void testSelectLetters() throws Exception {

        assertOutputBufferEquals("");
        assertLastOutputEquals("");

        // a
        goLeft();
        goLeft();
        goLeft();
        goLeft();

        assertOutputBufferEquals("a");
        assertLastOutputEquals("");

        // h
        goLeft();
        goRight();
        goLeft();
        goLeft();
        goLeft();

        assertOutputBufferEquals("ah");
        assertLastOutputEquals("");
        // a
        goLeft();
        goLeft();
        goLeft();
        goLeft();

        assertOutputBufferEquals("aha");
        assertLastOutputEquals("");

        // Send
        goRight();
        goRight();
        goRight();
        goRight();
        goRight();

        assertOutputBufferEquals("");
        assertLastOutputEquals("aha");
    }


    private void goLeft() {
        stepInput(InputType.INPUT1);
    }

    private void goRight() {
        stepInput(InputType.INPUT2);
    }


}