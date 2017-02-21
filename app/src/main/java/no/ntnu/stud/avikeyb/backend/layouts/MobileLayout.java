package no.ntnu.stud.avikeyb.backend.layouts;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;

/**
 * Created by Tor-Martin Holen on 15-Feb-17.
 */

public class MobileLayout extends StepLayout {

    private Symbol[] symbols;
    private Keyboard keyboard;
    private State state = State.SELECT_ROW;
    private ArrayList<Symbol> markedSymbols = new ArrayList<>();
    private int[] location = new int[]{-1, -1, -1};

    public MobileLayout(Keyboard keyboard) {
        this.keyboard = keyboard;

        symbols = new Symbol[]{Symbol.SPACE, Symbol.E, Symbol.O, Symbol.T, Symbol.I, Symbol.L, Symbol.S, Symbol.C, Symbol.W,
                Symbol.A, Symbol.N, Symbol.D, Symbol.R, Symbol.U, Symbol.Y, Symbol.F, Symbol.V, Symbol.Z,
                Symbol.H, Symbol.M, Symbol.B, Symbol.P, Symbol.K, Symbol.PERIOD, Symbol.J, Symbol.QUESTION_MARK, Symbol.SEND,
                Symbol.G, Symbol.X, Symbol.COMMA, Symbol.Q, Symbol.EXCLAMATION_MARK};
        nextRow();
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
                        nextRow();
                        break;
                    case INPUT2: //Select
                        state = State.SELECT_COLUMN;
                        nextColumn();
                        break;
                }
                break;
            case SELECT_COLUMN:
                switch (input) {
                    case INPUT1:
                        nextColumn();
                        break;
                    case INPUT2:
                        state = State.SELECT_LETTER;
                        nextLetter();
                        break;
                }
                break;
            case SELECT_LETTER:
                switch (input) {
                    case INPUT1:
                        nextLetter();
                        break;
                    case INPUT2:
                        state = State.SELECT_ROW;
                        selectCurrentSymbol(keyboard);
                        reset();
                        break;
                }
                break;
        }
        notifyLayoutListeners();
    }

    /**
     * Handles positional logic for rows
     */
    private void nextRow() {
        location[0]++;

        if (location[0] >= 4) {
            location[0] = 0;
        }

        markedSymbols = new ArrayList<>();
        int lowerBound = location[0] * 9;
        int upperBound = lowerBound + 9;
        for (int i = lowerBound; i < upperBound; i++) {
            addMarkedSymbol(i);
        }
        //Log.d("MobLayout", "Position: " + location[0] + ", " + location[1] + ", " + location[2]);
    }

    /**
     * Handles positional logic for columns
     */
    private void nextColumn() {
        location[1]++;
        int cols = location[0] * 9;
        int symbolsLeft = symbols.length - cols;
        //Log.d("MobLayout", "Symbols left: " + Math.ceil(symbolsLeft.length/3.0));
        if (location[1] >= Math.ceil(symbolsLeft/3.0) || location[1] >= 3) {
            location[1] = 0;
        }

        markedSymbols = new ArrayList<>();
        int lowerBound = location[0] * 9 + location[1] * 3;
        int upperBound = lowerBound + 3;
        for (int i = lowerBound; i < upperBound; i++) {
            addMarkedSymbol(i);

        }

        //Log.d("MobLayout", "Position: " + location[0] + ", " + location[1] + ", " + location[2]);
    }


    /**
     * Handles positional logic for selecting letters
     */
    private void nextLetter() {
        location[2]++;
        int lowerBound = location[0] * 9 + location[1] * 3;
        int symbolsLeft = symbols.length - lowerBound;
        //Log.d("MobLayout", "Symbols left: " + symbolsLeft.length);
        if (location[2] >= symbolsLeft || location[2] >=3) {
            location[2] = 0;
        }

        markedSymbols = new ArrayList<>();
        int index = location[0] * 9 + location[1] * 3 + location[2];
        addMarkedSymbol(index);
        //Log.d("MobLayout", "Position: " + location[0] + ", " + location[1] + ", " + location[2]);
    }

    public ArrayList<Symbol> getMarkedSymbols() {
        return markedSymbols;
    }

    /**
     * Sends the current symbol, only usable in state SELECT_LETTER
     */
    private void selectCurrentSymbol(Keyboard keyboard) {
        Symbol symbol = markedSymbols.get(0); // will only contain one symbol in SELECT_LETTER state.

        if (symbol == Symbol.SEND) {
            keyboard.sendCurrentBuffer();
        } else {
            keyboard.addToCurrentBuffer(symbol.getContent());
        }
    }

    private void addMarkedSymbol(int index){
        try{
            markedSymbols.add(symbols[index]);
        } catch (Exception e){

        }
    }

    public void reset() {
        location = new int[]{-1, -1, -1};
        markedSymbols = new ArrayList<>();
        nextRow();
    }

    public Symbol[] getSymbols() {
        return symbols;
    }
}