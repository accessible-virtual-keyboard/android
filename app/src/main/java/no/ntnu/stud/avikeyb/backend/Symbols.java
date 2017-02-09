package no.ntnu.stud.avikeyb.backend;

/**
 * Helper methods to build symbol arrays
 */
public class Symbols {


    /**
     * Builds a symbol array from the passed in symbols
     *
     * @param symbols variable number of symbols
     * @return an array of the passed in symbols
     */
    public static Symbol[] build(Symbol... symbols) {
        return symbols;
    }


    /**
     * Merge multiple arrays of symbols into a single array
     *
     * @param symbolCollections a variable number of symbol arrays to be merged
     * @return a symbol array with containing all the symbols from the passed in arrays
     */
    public static Symbol[] merge(Symbol[]... symbolCollections) {

        // Find the total number of symbols
        int numSymbols = 0;
        for (Symbol[] symbols : symbolCollections) {
            numSymbols += symbols.length;
        }


        Symbol[] mergedSymbols = new Symbol[numSymbols];
        int index = 0;
        // Add each symbol to the new array
        for (Symbol[] symbols : symbolCollections) {
            for (int i = 0; i < symbols.length; i++) {
                mergedSymbols[index++] = symbols[i];
            }
        }

        return mergedSymbols;
    }

    /**
     * Returns the symbols in the english alphabet
     *
     * @return a list of symbols in the english alphabet
     */
    public static Symbol[] alphabet() {
        return _alphabet;
    }


    /**
     * Returns the symbols representing the numbers from 0 - 9
     *
     * @return a list of number symbols from 0-9
     */
    public static Symbol[] numbers() {
        return _numbers;
    }

    /**
     * Returs common punctuation symbols
     *
     * @return a list of punctuation symbols
     */
    public static Symbol[] commonPunctuations() {
        return _commonPunctuations;
    }


    // Common symbol collections:

    private static Symbol[] _alphabet = {
            Symbol.A,
            Symbol.B,
            Symbol.C,
            Symbol.D,
            Symbol.E,
            Symbol.F,
            Symbol.G,
            Symbol.H,
            Symbol.I,
            Symbol.J,
            Symbol.K,
            Symbol.L,
            Symbol.M,
            Symbol.N,
            Symbol.O,
            Symbol.P,
            Symbol.Q,
            Symbol.R,
            Symbol.S,
            Symbol.T,
            Symbol.U,
            Symbol.V,
            Symbol.W,
            Symbol.X,
            Symbol.Y,
            Symbol.Z,
    };

    private static Symbol[] _numbers = {
            Symbol.NUM_0,
            Symbol.NUM_1,
            Symbol.NUM_2,
            Symbol.NUM_3,
            Symbol.NUM_4,
            Symbol.NUM_5,
            Symbol.NUM_6,
            Symbol.NUM_7,
            Symbol.NUM_8,
            Symbol.NUM_9,
    };

    private static Symbol[] _commonPunctuations = {
            Symbol.PERIOD,
            Symbol.COMMA,
            Symbol.QUESTION_MARK,
            Symbol.EXCLAMATION_MARK,
    };
}