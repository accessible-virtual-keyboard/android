package no.ntnu.stud.avikeyb.backend.layouts;


import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Suggestions;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.Symbols;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryLoader;
import no.ntnu.stud.avikeyb.backend.dictionary.LinearDictionary;

/**
 * Created by ingalill on 10/02/2017.
 */

public class ETOSLayout extends StepLayout {

    private static Symbol[] symbols = Symbols.merge(
            Symbols.build(Symbol.SEND),
            Symbols.etos(),
            Symbols.numbers(),
            Symbols.commonPunctuations(),
            Symbols.menuOptions()// test
    );

    private static Symbol[] menu = Symbols.menuOptions();

    public enum State {
        SELECT_ROW,
        SELECT_COLUMN,
        MENU_STATE,
        DICTIONARY_STATE,
    }

    // The current position of the cursor in the layout
    private int currentPosition = 0;
    // The current row of the cursor in the layout.
    private State state = State.SELECT_ROW;
    private Keyboard keyboard;
    private State menuOptions = State.DICTIONARY_STATE;

    List<String> dictionsuggestions = new ArrayList<>();


    public ETOSLayout(Keyboard keyboard, Suggestions suggestions) {
        this.keyboard = keyboard;

        suggestions.addListener(suggestions1 -> {
            dictionsuggestions.addAll(suggestions1);
            //notifyLayoutListeners();
        });
    }


    public List<String> getSuggestions() {
        return dictionsuggestions;
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


    public Symbol getCurrentSymbol() {
        return symbols[currentPosition];
    }

    /**
     * Returns all symbols in the layout
     *
     * @return the symbols in the layout
     */
    public Symbol[] getSymbols() {

        return symbols;
    }

    /**
     * Returns all the menu options in the layout
     *
     * @return the menu options in the layout
     */
    public Symbol[] getMenuOptions() {
        return menu;

    }

    @Override
    protected void onStep(InputType input) {

        switch (state) {
            case SELECT_ROW:
                switch (input) {
                    case INPUT1: // move
                        currentPosition = (currentPosition + 6) % symbols.length;
                        break;
                    case INPUT2: // selects
                        state = State.SELECT_COLUMN;
                        break;
                }
                break;
            case SELECT_COLUMN:
                switch (input) {
                    case INPUT1: // move
                        currentPosition = (currentPosition + 1) % symbols.length;
                        break;
                    case INPUT2: // selects
                        selectCurrentSymbol();
                        state = State.SELECT_ROW;
                        currentPosition = 0;
                        break;
                }
                break;
        }
        notifyLayoutListeners();
    }

    private void SwitchMenu() {

        Symbol current = symbols[currentPosition];

        if (current == Symbol.SWITCH) {
            menuOptions = State.MENU_STATE;
        } else {
            menuOptions = State.DICTIONARY_STATE;
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
