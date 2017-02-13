package no.ntnu.stud.avikeyb.backend.layouts;

import java.util.Arrays;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.Symbols;

/**
 * Basic binary search layout
 */
public class BinarySearchLayout extends StepLayout {


    private static Symbol[] symbols = Symbols.merge(
            Symbols.alphabet(),
            Symbols.commonPunctuations(),
            Symbols.build(Symbol.SEND));

    // The current sumbol list
    private List<Symbol> currentSymbols;
    // The symbols on the left side
    private List<Symbol> currentLeft;
    // The symbols on the right side
    private List<Symbol> currentRight;

    private Keyboard keyboard;

    public BinarySearchLayout(Keyboard keyboard) {
        this.keyboard = keyboard;
        reset();
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
     * Checks if a symbol is active
     *
     * @param symbol the symbol to check
     * @return true if the symbol is active
     */
    public boolean symbolIsActive(Symbol symbol) {
        return currentSymbols.contains(symbol);
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
            selectSymbol(currentRight);
            reset();
        } else if (currentRight.isEmpty()) {
            selectSymbol(currentLeft);
            reset();
        }
    }


    // Get the character from the list and send it to the keyboard
    private void selectSymbol(List<Symbol> list) {
        Symbol sym = list.get(0);
        if (sym == Symbol.SEND) {
            keyboard.sendCurrentBuffer();
        } else {
            keyboard.addToCurrentBuffer(sym.getContent());
        }
    }

    private void selectLeft() {
        currentSymbols = currentLeft;
    }

    private void selectRight() {
        currentSymbols = currentRight;
    }

    private void splitCurrent() {
        int middle = currentSymbols.size() / 2;
        currentLeft = currentSymbols.subList(0, middle);
        currentRight = currentSymbols.subList(middle, currentSymbols.size());
    }

    private void reset() {
        currentSymbols = Arrays.asList(symbols);
        splitCurrent();
    }
}
