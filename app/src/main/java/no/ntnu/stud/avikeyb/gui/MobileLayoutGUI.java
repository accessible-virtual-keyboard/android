package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.MobileLayout;
import no.ntnu.stud.avikeyb.gui.utils.LayoutLoader;
import no.ntnu.stud.avikeyb.gui.utils.TextAdapter;

/**
 * Created by Tor-Martin Holen on 15-Feb-17.
 */

public class MobileLayoutGUI extends LayoutGUI {

    private MobileLayout layout;
    private Activity activity;
    private HashMap<Symbol, View> symbolViewMap = new HashMap<>();
    private ArrayList<Symbol> previouslyMarked = new ArrayList<>();
    private int layoutResource1;
    private int layoutResource2;

    private ListView dictionaryList;
    private TextAdapter dictionaryListAdapter;
    private View lastDictionaryItemSelected;

    private TextAdapter historyListAdapter;

    private LayoutLoader loader;
    private MobileLayout.Mode previousLayoutState;
    private List<String> previousSuggestions = new ArrayList();


    public MobileLayoutGUI(Activity activity, MobileLayout layout, int layoutResource1, int layoutResource2) {
        super();
        this.activity = activity;
        this.layout = layout;
        this.layoutResource1 = layoutResource1;
        this.layoutResource2 = layoutResource2;
        layout.logMarked();
    }


    @Override
    protected View buildGUI() {
        //TODO return right layout resource
        if (layout.getMode() == MobileLayout.Mode.TILE_SELECTION_MODE) {
            loader = new LayoutLoader(activity, layoutResource1);
        } else if (layout.getMode() == MobileLayout.Mode.LETTER_SELECTION_MODE) {
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

        ListView historyList = (ListView) loader.getViewById(R.id.historylist);
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
    public void updateGUI() {
        if (layout.getMode() != previousLayoutState) {
            onLayoutActivated();
            layout.logMarked();
        }
        previousLayoutState = layout.getMode();
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
            if (symbolViewMap.containsKey(symbol)) {
                symbolViewMap.get(symbol).setSelected(false);
            }
        }
        for (Symbol symbol : newlyMarked) {
            if (symbolViewMap.containsKey(symbol)) {
                symbolViewMap.get(symbol).setSelected(true);
            }
        }
        previouslyMarked = newlyMarked;
    }

    private void updateDictionaryPart() {
        /*if (layout.getMarkedWord() == -1 && layout.getSuggestions() != null) {*/
        if (previousSuggestions != layout.getSuggestions()) {
            historyListAdapter.update(layout.getHistory());
            dictionaryListAdapter.update(layout.getSuggestions());
            Log.d("MobileLayout", "Size: " + layout.getSuggestions().size());
            dictionaryList.smoothScrollToPosition(0);
        } else {
            final int position = layout.getMarkedWord();
            Log.d("MobileGUI", "updateDictionaryPart: position: " + position);
            if (layout.getSuggestions() != null) {
                dictionaryList.setSelection(position);
            }

                /*View view  = dictionaryList.getChildAt(position);
                Log.d("MobileGui", "PosX: " + view.getHeight() + " - PosY" + view.getWidth());
                dictionaryList.scro(view.getHeight());*/


                /*dictionaryList.performItemClick(dictionaryList.getChildAt(position),
                        position,
                        dictionaryList.getItemIdAtPosition(position));*/
                /*

                dictionaryList.post(new Runnable() {
                    @Override
                    public void run() {
                        int numberOfSuggestions = layout.getSuggestions().size() <= layout.getMaxPossibleSuggestions() ? layout.getSuggestions().size() : layout.getMaxPossibleSuggestions();
                        if (position >= numberOfSuggestions / 2) {
                            dictionaryList.smoothScrollToPosition(numberOfSuggestions);
                        } else {
                            dictionaryList.smoothScrollToPosition(0);
                        }*/

                    /*}
                });*/


        }
    }

}
