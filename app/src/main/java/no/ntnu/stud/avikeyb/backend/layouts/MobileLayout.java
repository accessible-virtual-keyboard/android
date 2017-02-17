package no.ntnu.stud.avikeyb.backend.layouts;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;

/**
 * Created by Tor-Martin Holen on 15-Feb-17.
 */

public class MobileLayout extends StepLayout {

    private Symbol[][][] layoutSymbols;
    private Keyboard keyboard;
    private State state = State.SELECT_ROW;
    private LayoutPosition position = new LayoutPosition();

    public MobileLayout(Keyboard keyboard) {
        this.keyboard = keyboard;
/*        layoutSymbols = new Symbol[][][]{
                {{Symbol.A, Symbol.B, Symbol.C}, {Symbol.D, Symbol.E, Symbol.F}, {Symbol.G, Symbol.H, Symbol.I}}, //Multidimensional array representing rows, columns and symbols (at a given row and column)
                {{Symbol.J, Symbol.K, Symbol.L}, {Symbol.M, Symbol.N, Symbol.O}, {Symbol.P, Symbol.Q, Symbol.R}},
                {{Symbol.S, Symbol.T, Symbol.U}, {Symbol.V, Symbol.W, Symbol.X}, {Symbol.Y, Symbol.Z, Symbol.SEND}}
        };*/

        //ETAO layout alternative
        layoutSymbols = new Symbol[][][]{
                {{Symbol.SPACE, Symbol.E, Symbol.O}, {Symbol.T, Symbol.I, Symbol.L}, {Symbol.S, Symbol.C, Symbol.W}}, //Multidimensional array representing rows, columns and symbols (at a given row and column)
                {{Symbol.A, Symbol.N, Symbol.D}, {Symbol.R, Symbol.U, Symbol.Y}, {Symbol.F, Symbol.V, Symbol.Z}},
                {{Symbol.H, Symbol.M, Symbol.B}, {Symbol.P, Symbol.K, Symbol.PERIOD}, {Symbol.J, Symbol.QUESTION_MARK, Symbol.SEND}},
                {{Symbol.G, Symbol.X, Symbol.COMMA}, {Symbol.Q, Symbol.EXCLAMATION_MARK} }
        };
    }

    public enum State {
        SELECT_ROW,
        SELECT_COLUMN,
        SELECT_LETTER
    }

    @Override
    protected void onStep(InputType input) {
        switch (state) {
            case SELECT_ROW:
                switch (input) {
                    case INPUT1: //Move
                        position.nextRow();
                        break;
                    case INPUT2: //Select
                        state = State.SELECT_COLUMN;
                        break;
                }
                break;
            case SELECT_COLUMN:
                switch (input) {
                    case INPUT1:
                        position.nextColumn();
                        break;
                    case INPUT2:
                        state = State.SELECT_LETTER;
                        break;
                }
                break;
            case SELECT_LETTER:
                switch (input) {
                    case INPUT1:
                        position.nextLetter();
                        break;
                    case INPUT2:
                        state = State.SELECT_ROW;
                        selectCurrentSymbol();
                        position.resetPosition();
                        break;
                }
                break;
        }
        notifyLayoutListeners();
    }

    /**
     * Sends the current symbol, only usable in state SELECT_LETTER
     */
    private void selectCurrentSymbol() {
        int[] pos = position.getPosition();
        Symbol symbol = layoutSymbols[pos[0]][pos[1]][pos[2]];

        if (symbol == Symbol.SEND) {
            keyboard.sendCurrentBuffer();
        } else {
            keyboard.addToCurrentBuffer(symbol.getContent());
        }
    }

    public Symbol[][][] getLayoutSymbols() {
        return layoutSymbols;
    }

    /**
     * Handles positional logic for obtaining the symbols from layoutSymbols
     */
    private class LayoutPosition {
        private int[] position;

        public LayoutPosition() {
            position = new int[]{0, 0, 0};
        }

        /**
         * Moves to the next row, if there is no more rows it returns to start row.
         */
        public void nextRow() {
            position[0] += 1;
            if (position[0] >= layoutSymbols.length) {
                position[0] = 0;
            }
        }

        /**
         * Moves to the next column, if there is no more columns it returns to start column.
         */
        public void nextColumn() {
            position[1] += 1;
            if (position[1] >= layoutSymbols[position[0]].length) {
                position[1] = 0;
            }
        }

        /**
         * Moves to the next letter, if there is no more letters it returns to start letter.
         */
        public void nextLetter() {
            position[2] += 1;
            if (position[2] >= layoutSymbols[position[0]][position[1]].length) {
                position[2] = 0;
            }
        }

        /**
         * Resets the position, so the process of selecting a letter can start anew.
         */
        public void resetPosition() {
            position = new int[]{0, 0, 0};
        }

        public int[] getPosition() {
            return position;
        }
    }

}
