package no.ntnu.stud.avikeyb.backend.layouts;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryEntry;
import no.ntnu.stud.avikeyb.backend.dictionary.LinearEliminationDictionary;

import static android.content.ContentValues.TAG;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class MobileDictionaryLayout extends MobileLayout {

    private LinearEliminationDictionary dictionary;


    public MobileDictionaryLayout(Keyboard keyboard, LinearEliminationDictionary dictionary) {
        super();
        this.keyboard = keyboard;
        this.dictionary = dictionary;

        symbols = new Symbol[]{
                Symbol.E, Symbol.T, Symbol.A, Symbol.S, Symbol.R, Symbol.H, Symbol.L, Symbol.D, Symbol.C,
                Symbol.O, Symbol.I, Symbol.N, Symbol.U, Symbol.M, Symbol.F, Symbol.Y, Symbol.B, Symbol.V, Symbol.K,
                Symbol.P, Symbol.G, Symbol.W, Symbol.X, Symbol.J, Symbol.Q, Symbol.Z, Symbol.SEND,
                Symbol.DICTIONARY, Symbol.PERIOD, Symbol.COMMA, Symbol.QUESTION_MARK, Symbol.EXCLAMATION_MARK, Symbol.BACKSPACE};
        stepIndices = new int[]{0, 3, 6, 9, 12, 15, 19, 22, 26, 27, 28, 32, 33};
        setSuggestions(dictionary.getBaseSuggestion(nSuggestions));
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

                        System.out.println("--------------------------------------");
                        if (markedSymbols.contains(Symbol.SEND)) {
                            keyboard.sendCurrentBuffer();
                            dictionary.reset();
                            setSuggestions(dictionary.getBaseSuggestion(nSuggestions));
                        } else if (markedSymbols.contains(Symbol.DICTIONARY)) {
                            state = State.SELECT_DICTIONARY;
                            markedWord = 0;
                            softReset();
                            break;
                        } else if (markedSymbols.contains(Symbol.BACKSPACE)) {
                            //TODO handle backspace
                            Log.d(TAG, "Keyboard word: " + keyboard.getCurrentWord());

                            if (keyboard.getCurrentWord().equals("")) {
                                String currentBuffer = keyboard.getCurrentBuffer();
                                int secondLastSpace = currentBuffer.trim().lastIndexOf(" ");
                                if (secondLastSpace == -1) {
                                    currentBuffer = "";
                                } else {
                                    currentBuffer = currentBuffer.substring(0, secondLastSpace) + " ";
                                }

                                keyboard.clearCurrentBuffer();
                                keyboard.addToCurrentBuffer(currentBuffer);
                                dictionary.previousWord();
                                setSuggestions(dictionary.getSuggestions(nSuggestions));
                            } else {
                                dictionary.revertLastSuggestions();
                            }

                            dictionary.printListSuggestions(10);
                        } else if (markedSymbols.contains(Symbol.PERIOD)) {
                            if(!keyboard.getCurrentBuffer().isEmpty()){
                                state = State.SELECT_LETTER;
                                nextLetter();
                            }else{
                                reset();
                            }
                            break;
                        } else {
                            logMarked();
                            dictionary.findValidSuggestions(getStringsFromMarkedSymbols());
                            dictionary.printListSuggestions(nSuggestions);
                            setSuggestions(dictionary.getSuggestions(nSuggestions));
                        }

                        logMarked();

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
                        String keyboardInput = keyboard.getCurrentBuffer().trim();
                        Log.d(TAG, "onStep: keyboard input: " + keyboardInput);
                        keyboard.clearCurrentBuffer();
                        keyboard.addToCurrentBuffer(keyboardInput);
                        selectCurrentSymbols(keyboard);
                        keyboard.addToCurrentBuffer(" ");

                        reset();
                        break;
                }
                break;

            case SELECT_DICTIONARY:
                switch (input) {
                    case INPUT1:
                        markedWord++;
                        if (markedWord >= suggestions.size() || markedWord >= nSuggestions) {
                            markedWord = 0;
                        }
                        break;
                    case INPUT2:
                        String currentText = keyboard.getCurrentBuffer();
                        String currentWord = getSuggestions().get(markedWord);

                        currentWord = capitalizationCheck(currentText, currentWord);

                        keyboard.addToCurrentBuffer(currentWord + " ");
                        dictionary.nextWord();

                        state = State.SELECT_ROW;
                        nextRow();
                        markedWord = -1;
                        //keyboard.addToCurrentBuffer(dictionary.getSuggestionsWithFrequencies(1).get(0).getWord() + " ");
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
        location = new int[]{-1, -1, -1};
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


    private void deleteLastWord(){

    }
}
