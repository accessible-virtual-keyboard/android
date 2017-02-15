package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.view.View;

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

    public MobileLayoutGUI(Keyboard keyboard, Layout layout) {
        super(keyboard, layout);
    }

    @Override
    protected View buildGUI() {
        return null;
    }

    @Override
    protected void updateGUI() {

    }
}
