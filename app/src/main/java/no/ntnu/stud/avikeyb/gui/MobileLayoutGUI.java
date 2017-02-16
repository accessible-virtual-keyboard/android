package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;

import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Layout;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.MobileLayout;

/**
 * Created by Tor-Martin Holen on 15-Feb-17.
 */

public class MobileLayoutGUI extends LayoutGUI {

    private MobileLayout layout;
    private Activity activity;
    private HashMap<Symbol, View> symbolViewMap;

    public MobileLayoutGUI(Activity activity, Keyboard keyboard, Layout layout) {
        super(keyboard, layout);
        this.activity = activity;
    }

    @Override
    protected View buildGUI() {
        return new LinearLayout(activity); // Empty stub view
    }

    @Override
    protected void updateGUI() {

    }
}
