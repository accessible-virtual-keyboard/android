package no.ntnu.stud.avikeyb.backend.layouts;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.Symbols;

/**
 * Created by ingalill on 10/02/2017.
 */

public class ETOSLayout extends StepLayout {

    private static Symbol[] symbols = Symbols.merge(
            Symbols.build(Symbol.SEND),
            Symbols.etos(),
            Symbols.numbers(),
            Symbols.commonPunctuations());

    // The current position of the cursor in the layout
    private int currentPosition = 0;

    private Keyboard keyboard;

    public ETOSLayout(Keyboard keyboard) {

        this.keyboard = keyboard;
    }

    /**
     * Returns the current active position in the layout
     *
     * @return the position of the current active symbol
     */
    public int getSymbolCount() {
        return symbols.length;
    }


    @Override
    protected void onStep(InputType input) {

        switch (input) {
            case INPUT1:
                selectCurrentSymbol();
                reset();
                break;
            case INPUT2:
                currentPosition = (currentPosition +1) % getSymbolCount();

        }

    }

    private void selectCurrentSymbol() {

        Symbol current = symbols[currentPosition];

        if (current == Symbol.SEND) {
            keyboard.sendCurrentBuffer();
        } else {
            keyboard.addToCurrentBuffer(current.getContent());
        }
    }

    private void reset() {
        currentPosition = 0;
    }
}
