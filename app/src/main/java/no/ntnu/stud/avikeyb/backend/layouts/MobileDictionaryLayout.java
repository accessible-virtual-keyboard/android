package no.ntnu.stud.avikeyb.backend.layouts;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.dictionary.LinearEliminationDictionaryHandler;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class MobileDictionaryLayout extends MobileLayout {

    private LinearEliminationDictionaryHandler dictionary;


    public MobileDictionaryLayout(Keyboard keyboard, LinearEliminationDictionaryHandler dictionary) {
        super();
        this.keyboard = keyboard;
        this.dictionary = dictionary;

        symbols = new Symbol[]{
                Symbol.E, Symbol.T, Symbol.A, Symbol.S, Symbol.R, Symbol.H, Symbol.L, Symbol.D, Symbol.C,
                Symbol.O, Symbol.I, Symbol.N, Symbol.U, Symbol.M, Symbol.F, Symbol.Y, Symbol.B, Symbol.V, Symbol.K,
                Symbol.P, Symbol.G, Symbol.W, Symbol.X, Symbol.J, Symbol.Q, Symbol.Z, Symbol.SEND,
                Symbol.DICTIONARY, Symbol.PERIOD, Symbol.COMMA, Symbol.QUESTION_MARK, Symbol.EXCLAMATION_MARK, Symbol.DELETE_WORD, Symbol.CORRECT_LAST_INPUT, Symbol.CORRECT_WORD, Symbol.DELETION_DONE};
        stepIndices = new int[]{0, 3, 6, 9, 12, 15, 19, 22, 26, 27, 28, 32, 36};
        setBaseSuggestions();
        nextRow();
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
                        state = State.SELECT_ROW;
                        if (markedSymbols.contains(Symbol.SEND)) {
                            keyboard.sendCurrentBuffer();
                            dictionary.reset();
                            setBaseSuggestions();
                        } else if (markedSymbols.contains(Symbol.DICTIONARY)) {
                            state = State.SELECT_DICTIONARY;
                            softReset();
                            nextDictionaryRow();
                            break;
                        } else if (markedSymbols.contains(Symbol.DELETE_WORD)) {
                            state = State.SELECT_LETTER;
                            nextLetter();
                            break;
                        } else if (markedSymbols.contains(Symbol.PERIOD)) {
                            if (!keyboard.getCurrentBuffer().isEmpty()) { // We don't want the user to input punctuation symbols when no words has been entered
                                state = State.SELECT_LETTER;
                                nextLetter();
                            }
                            break;
                        } else {
                            dictionary.findValidSuggestions(getStringsFromMarkedSymbols());
                            setSuggestions(dictionary.getSuggestions(nSuggestions));
                        }

                        logMarked();
                        //dictionary.printListSuggestions(nSuggestions);
                        reset();
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
                        if (markedSymbols.contains(Symbol.DELETE_WORD)) {
                            if (!keyboard.getCurrentBuffer().isEmpty()) {
                                deleteLastWord();
                                dictionary.previousWord();
                                setBaseSuggestions();
                            }
                        } else if (markedSymbols.contains(Symbol.CORRECT_LAST_INPUT)) {
                            if(dictionary.hasWordHistory()){
                                dictionary.removeLastWordHistoryElement();
                                getPreviousSuggestions();

                            }
                        } else if (markedSymbols.contains(Symbol.CORRECT_WORD)) {
                            if (!keyboard.getCurrentBuffer().isEmpty()) {
                                deleteLastWord();
                                getPreviousSuggestions();
                                state = State.SELECT_DICTIONARY;
                                nextDictionaryRow();
                                break;
                            }
                        } else {
                            addPunctuationSymbol();
                        }

                        reset();
                        break;
                }
                break;

            case SELECT_DICTIONARY:
                switch (input) {
                    case INPUT1:
                        nextDictionaryRow();
                        break;
                    case INPUT2:
                        addWord();
                        state = State.SELECT_ROW;
                        setSuggestions(dictionary.getBaseSuggestion(nSuggestions));
                        reset();
                        break;
                }
                break;
        }

        notifyLayoutListeners();

    }

    /**
     * @return list of strings
     */
    private List<String> getStringsFromMarkedSymbols() {
        List<String> stringList = new ArrayList<>();
        for (Symbol sym : markedSymbols) {
            stringList.add(sym.getContent());
        }
        return stringList;
    }

    private void logMarked() {
        String result = "";
        for (Symbol sym : markedSymbols) {
            result += sym.getContent() + " ";
        }
        Log.d("MobLayout", "Marked symbols: " + result);
    }

    public void softReset() {
        location = new int[]{-1, -1, -1, -1};
        markedSymbols = new ArrayList<>();
    }

    /**
     * Capitalizes a word if it starts with an lowercase letter [a-z], returns original word if not.
     *
     * @param currentWord
     * @return
     */
    private String capitalizeWord(String currentWord) {
        String capitalizedLetter = currentWord.substring(0, 1).toUpperCase();
        if (currentWord.substring(0, 1).matches("[a-z]")) {
            return currentWord.replaceFirst("[a-z]", capitalizedLetter);
        } else {
            return currentWord;
        }

    }

    /**
     * Capitalizes a word that should be added to a text, if deemed appropriate.
     * Should be called every time a word is added to current text, if not all appropriate cases won't be found.
     *
     * @param currentText the text to check, for occurrences where a capital letter should be present.
     * @param nextWord    the word to check, if it should be modified to have a capital letter
     * @return
     */
    private String capitalizationCheck(String currentText, String nextWord) {
        if (currentText.isEmpty()) {
            nextWord = capitalizeWord(nextWord);
        }

        //RegExp if end matches ". ", "! " or "? " nextWord should be capitalized.
        if (currentText.matches("^[A-Za-z,.!?'\"\\s]+[.!?][ ]$")) {
            nextWord = capitalizeWord(nextWord);
        }
        return nextWord;
    }


    private void deleteLastWord() {
        String currentBuffer = keyboard.getCurrentBuffer();
        int secondLastSpace = currentBuffer.trim().lastIndexOf(" ");
        if (secondLastSpace == -1) {
            currentBuffer = "";
        } else {
            currentBuffer = currentBuffer.substring(0, secondLastSpace) + " ";
        }

        keyboard.clearCurrentBuffer();
        keyboard.addToCurrentBuffer(currentBuffer);
    }


    private void addWord() {
        String currentText = keyboard.getCurrentBuffer();
        String currentWord = getSuggestions().get(location[3]);

        currentWord = capitalizationCheck(currentText, currentWord);

        keyboard.addToCurrentBuffer(currentWord + " ");
        dictionary.nextWord();

    }

    private void getPreviousSuggestions() {
        dictionary.previousWord();
        setSuggestions(dictionary.getSuggestions(nSuggestions));
    }

    private void addPunctuationSymbol() {
        String keyboardInput = keyboard.getCurrentBuffer().trim();
        keyboard.clearCurrentBuffer();
        keyboard.addToCurrentBuffer(keyboardInput);
        selectCurrentSymbols(keyboard);
        keyboard.addToCurrentBuffer(" ");
    }

    private void setBaseSuggestions() {
        setSuggestions(dictionary.getBaseSuggestion(nSuggestions));
    }

}
