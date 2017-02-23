package no.ntnu.stud.avikeyb.backend.layouts;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryLoader;
import no.ntnu.stud.avikeyb.backend.dictionary.LinearEliminationDictionary;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class MobileDictionaryLayout extends MobileLayout {

    //private Node root = new Node("");
    private LinearEliminationDictionary dictionary;
    private int searchStart = 0;
    private String[] prefixes = null;
    private boolean[] validPrefixes = null;

    public MobileDictionaryLayout(Keyboard keyboard, DictionaryLoader dictionaryLoader, int layoutResource) {
        super(keyboard, layoutResource);
        dictionary = new LinearEliminationDictionary(dictionaryLoader);
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
                        dictionary.findValidSuggestions(searchStart, markedSymbols);
                        dictionary.printListWithNSuggestions(10);
                        searchStart++;

                        reset();
                        break;
                }
                break;
        }
        notifyLayoutListeners();
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
