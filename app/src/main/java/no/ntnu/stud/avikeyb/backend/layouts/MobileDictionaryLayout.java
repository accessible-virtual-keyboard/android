package no.ntnu.stud.avikeyb.backend.layouts;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.dictionary.LinearRadixDictionary;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class MobileDictionaryLayout extends MobileLayout {

    private Node root = new Node("");
    private LinearRadixDictionary dictionary;

    public MobileDictionaryLayout(Keyboard keyboard, Context context) {
        super(keyboard);
        dictionary = new LinearRadixDictionary(context);
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
                        dictionary.resetRadixSuggestions();
                        addRadixTreeBranch(root);
                        System.out.println("--------------------------------------");
                        dictionary.printList(dictionary.getSortedRadixSuggestions());
                        reset();
                        break;
                }
                break;
        }
        notifyLayoutListeners();
    }

    @Override
    protected void selectCurrentSymbols(Keyboard keyboard) {

    }

    private void addRadixTreeBranch(Node parentNode){
        for (Node node:parentNode.getChildren()){
            if(!node.isTerminated() && node.hasChildren()){
                addRadixTreeBranch(node);
            }
            else{
                addRadixTreeChildNodes(node);
            }
        }
        if(!root.hasChildren()){
            addRadixTreeChildNodes(root);
        }
    }

    private void addRadixTreeChildNodes(Node parent){
        for (Symbol symbol : markedSymbols) {
            String radix = parent.getRadix() + symbol.getContent();
            dictionary.findSuggestionsWithRadix(radix);
            if(radixExists()){
                Node child = new Node(radix);
                parent.addChild(child);
                dictionary.getRadixSuggestions();
            }
        }
        if(!parent.hasChildren()){
            parent.terminate();
        }
    }

    private boolean radixExists() {
        if(dictionary.getRadixSuggestions().isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    private class Node {
        String radix;
        List<Node> children = new LinkedList<>();

        public Node(String radix) {
            this.radix = radix;
        }

        public void addChild(Node node) {
            children.add(node);
        }

        public List<Node> getChildren() {
            return children;
        }

        public String getRadix() {
            return radix;
        }

        public boolean hasChildren(){
            if(children.isEmpty()){
                return false;
            }else {
                return true;
            }
        }

        public void terminate(){
            children = null;
        }

        public boolean isTerminated(){
            if(children == null){
                return true;
            } else{
                return false;
            }
        }
    }
}
