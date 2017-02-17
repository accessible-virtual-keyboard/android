package no.ntnu.stud.avikeyb.backend.layouts;

import android.util.Log;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.Symbols;
import no.ntnu.stud.avikeyb.backend.layouts.util.LayoutPosition;

/**
 * Created by ingalill on 10/02/2017.
 */

public class ETOSLayout extends StepLayout {

    private static Symbol[] symbols = Symbols.merge(
            Symbols.build(Symbol.SEND),
            Symbols.etos(),
            Symbols.numbers(),
            Symbols.commonPunctuations());

    private Symbol[][][] layoutSymbols;

    public enum State {
        SELECT_ROW,
        SELECT_COLUMN,
        SELECT_LETTER
    }

    // The current position of the cursor in the layout
    private int currentPosition = 0;
    // The current row of the cursor in the layout.
    private State state = State.SELECT_ROW;
    private Keyboard keyboard;
    private LayoutPosition position;

    public ETOSLayout(Keyboard keyboard) {
        this.keyboard = keyboard;
        layoutSymbols = new Symbol[8][6][1];
        int row = 0;
        int tempIndex = 0;
        for (Symbol sym : symbols) {
            // Log.d("LayoutDebug","Row: " + row + " Column: " + tempIndex);
            if (row < 8) {
                if (tempIndex < 6) {
                    layoutSymbols[row][tempIndex][0] = sym;
                    tempIndex++;
                } else {
                    row++;
                    tempIndex = 0;
                }

            }
        }
        position = new LayoutPosition(layoutSymbols);
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
     * Returns all symbols in the layout
     *
     * @return the symbols in the layout
     */
    public Symbol[] getSymbols() {
        return symbols;
    }

    @Override
    protected void onStep(InputType input) {

        switch (state) {
            case SELECT_ROW:
                switch (input) {
                    case INPUT1: // move
                        position.nextRow();
                        break;
                    case INPUT2: // selects
                        state = State.SELECT_COLUMN;
                        break;
                }
                break;
            case SELECT_COLUMN:
                switch (input) {
                    case INPUT1: // move
                        position.nextColumn();
                        break;
                    case INPUT2: // selects
                        position.selectCurrentSymbol(keyboard);
                        state = State.SELECT_ROW;
                        position.resetPosition();
                        break;
                }
                break;
        }
        notifyLayoutListeners();
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


} // en of class
