package no.ntnu.stud.avikeyb.backend.layouts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Suggestions;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.Symbols;

/**
 * Basic binary search layout
 */
public class BinarySearchLayout extends StepLayout {


    private static Symbol[] symbols = Symbols.merge(
            Symbols.alphabet(),
            Symbols.numbers(),
            Symbols.commonPunctuations(),
            Symbols.build(Symbol.SEND));

    // The current items list
    private List<Object> currentItems;
    // The items on the left side
    private List<Object> currentLeft;
    // The items on the right side
    private List<Object> currentRight;

    private List<String> suggestions = new ArrayList<>();
    private Keyboard keyboard;
    private Suggestions suggestionsEngine;

    public BinarySearchLayout(Keyboard keyboard, Suggestions suggestionsEngine) {
        this.keyboard = keyboard;
        this.suggestionsEngine = suggestionsEngine;
        listenForSuggestions();
        reset();
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
     * Checks if a symbol is active
     *
     * @param symbol the symbol to check
     * @return true if the symbol is active
     */
    public boolean symbolIsActive(Symbol symbol) {
        return currentItems.contains(symbol);
    }

    /**
     * Checks if a symbol is active
     *
     * @param symbol the symbol to check
     * @return true if the symbol is active
     */
    public boolean symbolIsActiveLeft(Symbol symbol) {
        return currentLeft.contains(symbol);
    }

    /**
     * Checks if a symbol is active
     *
     * @param symbol the symbol to check
     * @return true if the symbol is active
     */
    public boolean symbolIsActiveRight(Symbol symbol) {
        return currentRight.contains(symbol);
    }


    /**
     * Check if a suggestion is active in the left bucket
     *
     * @param suggestion the suggestion to check
     * @return true if the suggestion is in the left bucket
     */
    public boolean suggestionIsLeft(String suggestion) {
        return currentLeft.contains(suggestion);
    }

    /**
     * Check if a suggestion is active in the right bucket
     *
     * @param suggestion the suggestion to check
     * @return true if the suggestion is in the right bucket
     */
    public boolean suggestionIsRight(String suggestion) {
        return currentRight.contains(suggestion);
    }

    /**
     * Check if a suggestion is active
     *
     * @param suggestion the suggestion to check
     * @return true if the suggestion is currently selectable
     */
    public boolean suggestionIsActive(String suggestion) {
        return currentItems.contains(suggestion);
    }


    /**
     * Returns the list of current suggested words
     *
     * @return a list of words
     */
    public List<String> getSuggestions() {
        return suggestions;
    }


    @Override
    protected void onStep(InputType input) {

        // Select the correct side and split it into the new left and right side
        if (input == InputType.INPUT1) {
            selectLeft();
        } else if (input == InputType.INPUT2) {
            selectRight();
        }
        splitCurrent();

        // Check if we have reached a final selection
        checkCompleted();

        notifyLayoutListeners();
    }

    private void checkCompleted() {
        // If one of the sides are empty the other one will contain the selected symbol
        if (currentLeft.isEmpty()) {
            selectCurrent(currentRight);
            reset();
        } else if (currentRight.isEmpty()) {
            selectCurrent(currentLeft);
            reset();
        }
    }

    // Get the current item from the list and send it to the keyboard
    private void selectCurrent(List<Object> list) {
        Object item = list.get(0);
        if (item instanceof Symbol) {
            selectSymbol((Symbol) item);
        } else if (item instanceof String) {
            selectSuggestion((String) item);
        }
    }


    // Send symbol value to keybaord
    private void selectSymbol(Symbol symbol) {
        if (symbol == Symbol.SEND) {
            keyboard.sendCurrentBuffer();
        } else {
            keyboard.addToCurrentBuffer(symbol.getContent());
        }
    }

    private void selectSuggestion(String suggestion) {
        // Remove the characters that has already been written
        String sug = suggestion.substring(keyboard.getCurrentWord().length());
        keyboard.addToCurrentBuffer(sug + Symbol.SPACE.getContent());
    }

    private void selectLeft() {
        currentItems = currentLeft;
    }

    private void selectRight() {
        currentItems = currentRight;
    }

    // Split the current items bucket into a left and a right bucket
    private void splitCurrent() {
        int middle = currentItems.size() / 2;
        currentLeft = currentItems.subList(0, middle);
        currentRight = currentItems.subList(middle, currentItems.size());
    }

    private void reset() {
        currentItems = new ArrayList<>(Arrays.asList(symbols));
        splitCurrent();
    }

    // Listen for suggestions and add new suggestions to the layout
    private void listenForSuggestions() {

        suggestionsEngine.addListener(suggestions1 -> {

            // Take only the 5 first suggestions. A new list is needed to copy the items because
            // the sub list will keep a reference to the underlying list an keep it from
            // getting garbage collected.
            suggestions = new ArrayList<>(suggestions1.subList(0, Math.min(5, suggestions1.size())));

            currentItems.addAll(suggestions);
            splitCurrent();
            notifyLayoutListeners();
        });

    }

}
