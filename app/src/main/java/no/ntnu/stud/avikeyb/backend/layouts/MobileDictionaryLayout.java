package no.ntnu.stud.avikeyb.backend.layouts;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.dictionary.LinearEliminationDictionary;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class MobileDictionaryLayout extends MobileLayout {

    private LinearEliminationDictionary dictionary;
    private int searchStart = 0;

    public MobileDictionaryLayout(Keyboard keyboard, LinearEliminationDictionary dictionary) {
        super();
        this.keyboard = keyboard;
        this.dictionary = dictionary;

        symbols = new Symbol[]{ //Tabs imitate corresponding layout in xml
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
                        dictionary.findValidSuggestions(searchStart, getStringsFromMarkedSymbols());
                        dictionary.printListWithNSuggestions(10);
                        searchStart++;

                        reset();
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
/*    @Override
    protected void selectCurrentSymbols(Keyboard keyboard) {

    }

    private void addNodeTreeBranch(Node parentNode) {

        updatePrefixes(parentNode);

        for (Node node : parentNode.getChildren()) {
            if(!node.isTerminated()){
                if (node.hasChildren()) {
                    addNodeTreeBranch(node);
                } else {
                    addNodeTreeChildNodes(node, prefixes, validPrefixes);
                }
            }

        }
        if (!root.hasChildren()) {
            addNodeTreeChildNodes(root, prefixes, validPrefixes);
        }
    }

    private void addNodeTreeChildNodes(Node parentNode, String[] prefixes, boolean[] validPrefixes) {
        for (int i = 0; i < prefixes.length; i++) {
            String prefix = parentNode.getPrefix() + prefixes[i];
            if (validPrefixes[i]) {
                Log.d("mobDicLayout", "Prefix: \"" + prefix + "\"");
                Node child = new Node(prefix);
                parentNode.addChild(child);
            }
        }
        if (!parentNode.hasChildren()) {
            parentNode.terminate();
        }
    }

    private void updatePrefixes(Node parentNode){
        if(!parentNode.hasChildren()){
            *//*prefixes = new String[markedSymbols.size()];
            for (int i = 0; i < markedSymbols.size(); i++) {
                prefixes[i] = markedSymbols.get(i).getContent();
            }*//*
            //validPrefixes = dictionary.findValidSuggestions(searchStart, markedSymbols);
        }
    }

    private class Node {
        String prefix;
        List<Node> children = new LinkedList<>();

        public Node(String prefix) {
            this.prefix = prefix;
        }

        public void addChild(Node node) {
            if (!isTerminated()) {
                children.add(node);
            }

        }

        public List<Node> getChildren() {
            return children;
        }

        public String getPrefix() {
            return prefix;
        }

        public boolean hasChildren() {
            if (children != null && children.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }

        public void terminate() {
            children = null;
        }

        public boolean isTerminated() {
            if (children == null) {
                return true;
            } else {
                return false;
            }
        }
    }*/
}
