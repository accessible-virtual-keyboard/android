package no.ntnu.stud.avikeyb.backend.layouts;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Suggestions;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.Symbols;

import static android.content.ContentValues.TAG;

/**
 * Created by ingalill on 10/02/2017.
 */

public class ETOSLayout extends StepLayout {

    private static Symbol[] symbols = Symbols.merge(
            Symbols.build(Symbol.SEND),
            Symbols.etos(),
            Symbols.numbers(),
            Symbols.commonPunctuations()
            //Symbols.menuOptions()// test
    );

    private static Symbol[] menu = Symbols.menuOptions();

    public enum State {
        SELECT_ROW,
        SELECT_COLUMN,
        SELECT_MENU,
        SELECT_DICTIONARY, // test
    }

    // The current position of the cursor in the layout
    private int currentPosition = 0;
    // The current row of the cursor in the layout.
    private State state = State.SELECT_ROW;
    private Keyboard keyboard;

    private List<String> dictionsuggestions = new ArrayList<>();
    private Suggestions suggestionEngine;


    public ETOSLayout(Keyboard keyboard, Suggestions suggestions) {
        this.keyboard = keyboard;
        this.suggestionEngine = suggestions;

        // Listen for suggestions
        suggestionEngine.addListener(new Suggestions.Listener() {
            @Override
            public void onSuggestions(List<String> suggestions1) {
                dictionsuggestions.clear();
                dictionsuggestions.addAll(suggestions1);
                //notifyLayoutListeners();
            }
        });
    }

    public List<String> getSuggestions() {
        return dictionsuggestions;
    }

    /**
     * Get the current suggestion word
     *
     * @return return the current suggestion from its position.
     */
    public String getCurrentSuggestion() {
        if (currentPosition < dictionsuggestions.size()) {
            return dictionsuggestions.get(currentPosition);
        }
        return "";
    }


    /**
     * Returns the current active position in the layout
     *
     * @return the position of the current active symbol
     */
    public int getSymbolCount() {

        return symbols.length;
    }

    public int getDictionaryLength() {
        return dictionsuggestions.size();
    }

    /**
     * Returns the current active position in the layout
     *
     * @return the position of the current active symbol
     */
    public int getCurrentPosition() {

        return currentPosition;
    }


    public Symbol getCurrentMenu() {
        return menu[currentPosition];
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
                        nextRow();
                        break;
                    case INPUT2: // selects
                        state = State.SELECT_COLUMN;
                        break;
                }
                break;
            case SELECT_COLUMN:
                switch (input) {
                    case INPUT1: // move
                        nextColumn();
                        break;
                    case INPUT2: // selects

                        if (getCurrentSymbol().equals(Symbol.DICTIONARY)) {
                            System.out.println("I should not be here");
                            state = State.SELECT_DICTIONARY;
                        } else if (getCurrentSymbol().equals(Symbol.MENU)) {
                            state = State.SELECT_MENU;
                        } else {
                            selectCurrentSymbol();
                            state = State.SELECT_ROW;
                        }

                        currentPosition = 0;
                        break;
                }
                break;
            case SELECT_DICTIONARY: // todo make this work. it do not  work.
                switch (input) {
                    case INPUT1: // moves
                        nextDictionaryEntry();
                        System.out.println("Am I in select dictionary input 1?");
                        break;
                    case INPUT2: // selects

                        if (getCurrentSuggestion() == null) {
                            selectSuggestion(getCurrentSuggestion()); //todo
                        }
                        System.out.println("Am I in select dictionary input 2?");
                        state = State.SELECT_ROW;
                        currentPosition = 0;
                        break;
                }
                break;

            case SELECT_MENU:
                switch (input) {
                    case INPUT1:
                        nextColumn();
                        break;
                    case INPUT2:

                        getCurrentMenu();
                        break;
                }
                break;
        }
        Log.d(TAG, "onStep: currentPosition: " + currentPosition);
        notifyLayoutListeners();
    }

    /**
     * Moving cursor to the next row
     */
    public void nextRow() {
        currentPosition = (currentPosition + 6);
        if(currentPosition > symbols.length){
            currentPosition = 0;
        }
    }

    /**
     * Moving cursor to the next column.
     */
    public void nextColumn() {

        currentPosition = (currentPosition + 1);
        if (currentPosition % 6 == 0){
            currentPosition -= 6;
        }
    }

    /**
     * If the suggestion is not null, then move the curosr to the next entry.
     */
    public void nextDictionaryEntry() {
        if (getSuggestions().size() > 0) {
            currentPosition = (currentPosition + 1) % getSuggestions().size();
        }
    }

    /**
     * Adds the suggestion to the output buffer.
     *
     * @param suggestion
     */
    private void selectSuggestion(String suggestion) {
        // Remove the characters that has already been written
        String sug = suggestion.substring(keyboard.getCurrentWord().length());
        keyboard.addToCurrentBuffer(sug + Symbol.SPACE.getContent());
    }

    private void selectCurrentSymbol() {
        Symbol current = symbols[currentPosition];

        if (current == Symbol.SEND) {
            keyboard.sendCurrentBuffer();
        } else {
            keyboard.addToCurrentBuffer(current.getContent());
        }
    }

} // en of class
