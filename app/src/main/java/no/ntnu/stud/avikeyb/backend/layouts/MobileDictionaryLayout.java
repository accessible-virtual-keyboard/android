package no.ntnu.stud.avikeyb.backend.layouts;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryEntry;
import no.ntnu.stud.avikeyb.backend.dictionary.LinearEliminationDictionary;

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
                            //TODO send typed text
                            keyboard.sendCurrentBuffer();
                        } else if (markedSymbols.contains(Symbol.DICTIONARY)) {
                            //TODO go to dictionary
                            state = State.SELECT_DICTIONARY;
                            markedWord = 0;
                            softReset();

                            break;
                        } else if (markedSymbols.contains(Symbol.BACKSPACE)) {
                            //TODO handle backspace
                            dictionary.revertLastSuggestions();
                            dictionary.printListSuggestions(10);
                        } else if (markedSymbols.contains(Symbol.PERIOD)) {
                            state = State.SELECT_LETTER;
                            nextLetter();
                            break;
                        } else {
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
                        selectCurrentSymbols(keyboard);
                        reset();
                        break;
                }
                break;

            case SELECT_DICTIONARY:
                switch (input) {
                    case INPUT1:
                        markedWord++;
                        if(markedWord >= suggestions.size() || markedWord >= nSuggestions){
                            markedWord = 0;
                        }
                        break;
                    case INPUT2:
                        //TODO selection logic

                        keyboard.addToCurrentBuffer(getSuggestions().get(markedWord)+ " ");
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

    private List<String> getStringsFromMarkedSymbols() {
        List<String> stringList = new ArrayList<>();
        for (Symbol sym : markedSymbols) {
            stringList.add(sym.getContent());
        }
        return stringList;
    }

    private void logMarked(){
        String result = "";
        for (Symbol sym:markedSymbols) {
            result += sym.getContent() + " ";
        }
        Log.d("MobLayout", "Marked symbols: " + result);
    }

    public void softReset() {
        location = new int[]{-1,-1,-1};
        markedSymbols = new ArrayList<>();
    }

}
