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
    // The current row of the cursor in the layout.
    private int currentRow = 0;

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

    /**
     * Returns the current active position in the layout
     *
     * @return the position of the current active symbol
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Return the current row in the layout
     *
     * @return the position of the current row.
     */
    public int getCurrentRow() {
        return currentRow;
    }

    /**
     * Returns all symbols in the layout
     *
     * @return the symbols in the layout
     */
    public Symbol[] getSymbols() {
        return symbols;
    }

    @Override
    protected void onStep(InputType input) {

        switch (input) {
            case INPUT1:
                selectCurrentSymbol();
                reset();
                break;
            case INPUT2: // velge rad så colonne.
                //currentPosition = (currentPosition + 1) % getSymbolCount();
                selectCurrentRow();
        }
        notifyLayoutListeners();
    }

    /**
     * Select a row and then
     */
    public void selectCurrentRow() {

        Symbol currentRow = symbols[getCurrentRow()];
        //System.out.println("The current row is: " + currentRow);

        if (currentRow == Symbol.SEND) {
            currentPosition = (currentPosition + 1) % getSymbolCount();
        }
        if (currentRow == Symbol.A) {
            currentPosition = (currentPosition + 1) % getSymbolCount();
        }
        if (currentRow == Symbol.N) {
            currentPosition = (currentPosition + 1) % getSymbolCount();

        }
        if (currentRow == Symbol.D) {
            currentPosition = (currentPosition + 1) % getSymbolCount();
        }
        if (currentRow == Symbol.W) {
            currentPosition = (currentPosition + 1) % getSymbolCount();
        }
        if (currentRow == Symbol.NUM_3) {
            currentPosition = (currentPosition + 1) % getSymbolCount();
        }
        if (currentRow == Symbol.NUM_9) {
            currentPosition = (currentPosition + 1) % getSymbolCount();
        }
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
        currentRow = 0;
    }
} // en of class
