package no.ntnu.stud.avikeyb.backend.layouts;


import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.Symbols;

/**
 * Simple layout for testing
 */
public class SimpleExampleLayout extends StepLayout {

    // The symbols available in the layout.
    private static Symbol[] symbols = Symbols.merge(
            Symbols.build(Symbol.SEND),
            Symbols.alphabet(),
            Symbols.numbers(),
            Symbols.commonPunctuations());

    // The current position of the cursor in the layout
    private int currentPosition = 0;

    private Keyboard keyboard;

    public SimpleExampleLayout(Keyboard keyboard) {

        this.keyboard = keyboard;
    }

    /**
     * Returns the total number of symbols in the layout
     *
     * @return the total number of symbols in the layout
     */
    public int getSymbolCount() {
        return symbols.length;
    }

    /**
     * Returns the symbol at the given index
     *
     * @param index the index of the symbol to return
     * @return a symbol
     */
    public Symbol getSymbolAt(int index) {

        return symbols[index];
    }

    /**
     * Returns the current active position in the layout
     *
     * @return the position of the current active symbol
     */
    public int getCurrentPosition() {
        return currentPosition;
    }


    @Override
    protected void onStep(InputType input) {

        switch (input) {
            case INPUT1: // Select the current symbol
                selectCurrentSymbol();
                reset();
                break;
            case INPUT2: // Step to the right in the layout
                currentPosition = (currentPosition + 1) % getSymbolCount();
                break;
        }

        notifyLayoutListeners();
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
