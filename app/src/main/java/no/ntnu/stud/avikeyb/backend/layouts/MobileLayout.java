package no.ntnu.stud.avikeyb.backend.layouts;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Layout;

/**
 * Created by Tor-Martin Holen on 15-Feb-17.
 */

public class MobileLayout extends StepLayout {

    private Keyboard keyboard;

    @Override
    protected void onStep(InputType input) {
        keyboard.addToCurrentBuffer("Symbol chosen");
        keyboard.sendCurrentBuffer();
    }

    public MobileLayout(Keyboard keyboard) {
        this.keyboard = keyboard;
    }
}
