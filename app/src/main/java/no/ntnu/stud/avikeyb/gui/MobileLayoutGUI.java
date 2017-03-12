package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.MobileDictionaryLayout;
import no.ntnu.stud.avikeyb.gui.utils.LayoutLoader;
import no.ntnu.stud.avikeyb.gui.utils.TextAdapter;

/**
 * Created by Tor-Martin Holen on 15-Feb-17.
 */

public class MobileLayoutGUI extends LayoutGUI {

    private MobileDictionaryLayout layout;
    private Activity activity;
    private HashMap<Symbol, View> symbolViewMap = new HashMap<>();
    private ArrayList<Symbol> previouslyMarked = new ArrayList<>();
    private int layoutResource1;
    private int layoutResource2;

    private ListView dictionaryList;
    private TextAdapter dictionaryListAdapter;
    private View lastDictionaryItemSelected;

    private ListView historyList;
    private TextAdapter historyListAdapter;

    private MobileDictionaryLayout.State lastState;
    private LayoutLoader loader;
    private MobileDictionaryLayout.DictionaryState previousLayoutState;

    public MobileLayoutGUI(Activity activity, Keyboard keyboard, MobileDictionaryLayout layout, int layoutResource1, int layoutResource2) {
        super(keyboard);
        this.activity = activity;
        this.layout = layout;
        this.layoutResource1 = layoutResource1;
        this.layoutResource2 = layoutResource2;
        layout.logMarked();
    }


    @Override
    protected View buildGUI() {
        //TODO return right layout resource
        if (layout.getDictionaryState() == MobileDictionaryLayout.DictionaryState.DICTIONARY_ON) {
            loader = new LayoutLoader(activity, layoutResource1);
        } else if (layout.getDictionaryState() == MobileDictionaryLayout.DictionaryState.DICTIONARY_OFF) {
            loader = new LayoutLoader(activity, layoutResource2);
        }


        dictionaryList = (ListView) loader.getViewById(R.id.listview);
        dictionaryListAdapter = new TextAdapter(activity.getApplicationContext(), R.id.listview, new ArrayList<String>());
        dictionaryList.setAdapter(dictionaryListAdapter);
        dictionaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                if (lastDictionaryItemSelected != null) {
                    lastDictionaryItemSelected.setSelected(false);
                }
                if (view != null) {
                    view.setSelected(true);
                    lastDictionaryItemSelected = view;
                }

            }
        });
        dictionaryList.setEnabled(false);

        historyList = (ListView) loader.getViewById(R.id.historylist);
        historyListAdapter = new TextAdapter(activity.getApplicationContext(), R.id.listview, new ArrayList<String>());
        historyList.setAdapter(historyListAdapter);
        historyList.setEnabled(false);

        for (Symbol symbol : layout.getSymbols()) {
            if (symbol != null && loader.hasSymbol(symbol)) {
                TextView guiTextTile = (TextView) loader.getViewForSymbol(symbol);

                guiTextTile.setText(symbol.getContent());
                guiTextTile.setTextColor(Color.BLACK);
                guiTextTile.setBackgroundResource(R.drawable.text_selection_colors);

                if (symbol.equals(Symbol.DICTIONARY)) {
                    guiTextTile.setText(Symbol.DICTIONARY_UNICODE_SYMBOL.getContent());
                }
                symbolViewMap.put(symbol, guiTextTile);
            }
        }
        return loader.getLayout();
    }

    @Override
    public void onLayoutActivated() {
        layoutContainer.removeAllViews();
        layoutContainer.addView(buildGUI());
    }

    @Override
    public void updateGUI() {
        if (layout.getDictionaryState() != previousLayoutState) {
            onLayoutActivated();
            layout.logMarked();
        }
        previousLayoutState = layout.getDictionaryState();
        updateKeyboardPart();
        updateDictionaryPart();
    }

    /**
     * Used to update the gui so the default marked elements are marked properly
     */
    public void firstUpdate() {
        updateGUI();
    }

    private void updateKeyboardPart() {
        ArrayList<Symbol> newlyMarked = new ArrayList<>(layout.getMarkedSymbols());
        for (Symbol symbol : previouslyMarked) {
            if (symbol != null && symbolViewMap.containsKey(symbol)) {
                symbolViewMap.get(symbol).setSelected(false);
            }
        }
        for (Symbol symbol : newlyMarked) {
            if (symbol != null && symbolViewMap.containsKey(symbol)) {
                symbolViewMap.get(symbol).setSelected(true);
            }
        }
        previouslyMarked = newlyMarked;
    }

    private void updateDictionaryPart() {
        MobileDictionaryLayout.State newState = layout.getState();


        if (layout.getMarkedWord() == -1 && layout.getSuggestions() != null) {
            historyListAdapter.update(layout.getHistory());
            dictionaryListAdapter.update(layout.getSuggestions());
            dictionaryList.smoothScrollToPosition(0);
        } else {
            /*if( newState == MobileLayout.State.SELECT_DICTIONARY && lastState == MobileLayout.State.SELECT_LETTER ){
                dictionaryListAdapter.update(layout.getSuggestions());
                dictionaryList.smoothScrollToPosition(0);
            }*/
            int position = layout.getMarkedWord();
            //Log.d(TAG, "updateDictionaryPart: position: " + position);
            dictionaryList.performItemClick(dictionaryList.getChildAt(position),
                    position,
                    dictionaryList.getItemIdAtPosition(position));
            if (layout.getSuggestions() != null) {
                int numberOfSuggestions = layout.getSuggestions().size() <= layout.getMaxPossibleSuggestions() ? layout.getSuggestions().size() : layout.getMaxPossibleSuggestions();
                if (position >= numberOfSuggestions / 2) {
                    dictionaryList.smoothScrollToPosition(numberOfSuggestions);
                } else {
                    dictionaryList.smoothScrollToPosition(0);
                }
            }
        }



        lastState = newState;
    }

}
