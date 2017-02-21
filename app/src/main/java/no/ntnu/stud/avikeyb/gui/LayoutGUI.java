package no.ntnu.stud.avikeyb.gui;

import android.view.View;

import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Layout;

/**
 * Base class for gui layouts
 */
public abstract class LayoutGUI {

    private Keyboard keyboard;
    private View view;

    public LayoutGUI(Keyboard keyboard, Layout layout) {
        this.keyboard = keyboard;

        // Update the gui when the layout state changes
        layout.addLayoutListener(new Layout.LayoutListener() {
            @Override
            public void onLayoutChanged() {
                updateGUI();
            }
        });
    }

    protected Keyboard getKeyboard() {
        return keyboard;
    }


    /**
     * creates the keyboard layout view
     *
     * @return a view of the layout
     */
    public View createGUI() {
        if(view == null){
            view = buildGUI(); // cache the view
        }
        updateGUI(); // The GUI must be updated after it has been created
        return view;
    }

    /**
     * Returns the keyboard layout view
     *
     * @return a view of the layout
     */
    protected abstract View buildGUI();

    /**
     * Called whenever the layout must be updated to reflect any changes in the layout backend
     */
    protected abstract void updateGUI();

}
