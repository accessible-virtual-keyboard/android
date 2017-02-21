package no.ntnu.stud.avikeyb.backend.layouts;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.dictionary.DictionaryEntry;
import no.ntnu.stud.avikeyb.backend.dictionary.LinearRadixDictionary;

/**
 * Created by Tor-Martin Holen on 21-Feb-17.
 */

public class MobileDictionaryLayout extends MobileLayout {

    private Node root = new Node(null);
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
                        addRadixTreeBranch(root);
                        reset();
                        break;
                }
                break;
        }

    }

    private void addRadixTreeBranch(Node parentNode){

        for (Node node:parentNode.getChildren()){
            if(!node.isTerminated() && node.hasChildren()){
                addRadixTreeBranch(node);
            }
            else{
                for (Symbol symbol : markedSymbols) {
                    if(radixExistsInDictionary("[INSERT_RADIX_HERE]")){ //TODO implement properly
                        Node child = new Node(symbol.getContent());
                        node.addChild(child);
                    }
                }
                if(!node.hasChildren()){
                    node.terminate();
                }
            }
        }
    }

    private boolean radixExistsInDictionary(String radix) {
        dictionary.findSuggestionsWithRadix(radix);
        if(dictionary.getRadixSuggestions().isEmpty()){
            return false;
        }else{
            //TODO logic for handling suggestions
            return true;
        }


    }

    private class Node {
        String data;
        List<Node> children = new LinkedList<>();

        public Node(String data) {
            this.data = data;
        }

        public void addChild(Node node) {
            children.add(node);
        }

        public List<Node> getChildren() {
            return children;
        }

        public String getData() {
            return data;
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
