package no.ntnu.stud.avikeyb.backend.layouts;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.util.LayoutPosition;

/**
 * Created by Tor-Martin Holen on 15-Feb-17.
 */

public class MobileLayout extends StepLayout {

    private Symbol[][][] layoutSymbols;
    private Keyboard keyboard;
    private State state = State.SELECT_ROW;
    private LayoutPosition position;

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
                {{Symbol.G, Symbol.X, Symbol.COMMA}, {Symbol.Q, Symbol.EXCLAMATION_MARK}}
        };
        position = new LayoutPosition(layoutSymbols);
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
                        position.selectCurrentSymbol(keyboard);
                        position.resetPosition();
                        break;
                }
                break;
        }
        notifyLayoutListeners();
    }

    public Symbol[][][] getLayoutSymbols() {
        return layoutSymbols;
    }


}
