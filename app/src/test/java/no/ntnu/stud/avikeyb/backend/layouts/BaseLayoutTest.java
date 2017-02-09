package no.ntnu.stud.avikeyb.backend.layouts;

import org.junit.Before;
import org.junit.Test;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Layout;

import static org.junit.Assert.assertEquals;

/**
 * Created by pitmairen on 08/02/2017.
 */
public class BaseLayoutTest {


    private Layout layout;

    @Before
    public void setUp() throws Exception {
        layout = new TestLayout();
    }


    @Test
    public void testAddListeners() throws Exception {

        StateListener listener = new StateListener();
        layout.addLayoutListener(listener);

        assertEquals(0, listener.getChangeCounter());
        layout.setInputState(InputType.INPUT1, true);
        assertEquals(1, listener.getChangeCounter());
        layout.setInputState(InputType.INPUT1, false);
        assertEquals(2, listener.getChangeCounter());

    }

    @Test
    public void testRemoveListeners() throws Exception {

        StateListener listener = new StateListener();
        layout.addLayoutListener(listener);

        assertEquals(0, listener.getChangeCounter());
        layout.setInputState(InputType.INPUT1, true);
        assertEquals(1, listener.getChangeCounter());
        layout.removeLayoutListener(listener);
        layout.setInputState(InputType.INPUT1, false);

        // Should still be 1
        assertEquals(1, listener.getChangeCounter());

    }

    private static class TestLayout extends BaseLayout {
        @Override
        public void setInputState(InputType input, boolean value) {
            notifyLayoutListeners();
        }
    }

    private static class StateListener implements Layout.LayoutListener {

        private int changeCounter = 0;

        @Override
        public void onLayoutChanged() {
            changeCounter++;
        }

        public int getChangeCounter() {
            return changeCounter;
        }
    }

}