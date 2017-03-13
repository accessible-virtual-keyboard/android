package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.ETOSLayout;
import no.ntnu.stud.avikeyb.gui.utils.MenuAdapter;
import no.ntnu.stud.avikeyb.gui.utils.LayoutLoader;
import no.ntnu.stud.avikeyb.gui.utils.TextAdapter;

/**
 * Created by ingalill on 10/02/2017.
 */

public class ETOSLayoutGUI extends LayoutGUI {

    private Activity activity;
    private ETOSLayout layout;

    private HashMap<Symbol, View> symbolViewMap = new HashMap<>();
    private ArrayList<Symbol> listItems = new ArrayList<>();
    private ListView dictionaryList;

    private MenuAdapter menuAdapter;
    private TextAdapter dictionaryAdapter;
    private View previousViewSelected;
    private LayoutLoader loader;
    private TextView emptySuggestionsView;


    public ETOSLayoutGUI(Activity activity, ETOSLayout layout) {
        super();
        this.layout = layout;
        this.activity = activity;
        loader = new LayoutLoader(activity, R.layout.layout_etos);
    }

    // runs only one time.
    public ViewGroup buildGUI() {
        for (Symbol symbol : layout.getSymbols()) {
            if (symbol != null && loader.hasSymbol(symbol)) {
                TextView view = (TextView) loader.getViewForSymbol(symbol);
                view.setText(symbol.getContent());
                view.setTextColor(Color.BLACK);
                symbolViewMap.put(symbol, view);
            }
        }

        dictionaryList = (ListView) loader.getViewById(R.id.listview);
        menuAdapter = new MenuAdapter(activity, R.id.listview, listItems);
        dictionaryAdapter = new TextAdapter(activity.getApplicationContext(), R.id.listview, new ArrayList<String>());


        dictionaryList.setAdapter(dictionaryAdapter);
        dictionaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                if (previousViewSelected != null) {
                    previousViewSelected.setSelected(false);
                }
                if (view != null) {
                    view.setSelected(true);
                    previousViewSelected = view;
                }
            }
        });
        dictionaryList.setEnabled(false);

        emptySuggestionsView = (TextView) loader.getViewById(R.id.emptySuggestions);

        return (ViewGroup) loader.getLayout();
    }


    public void updateGUI() { //todo cursor disapers

        if (layout.getCurrentDictionaryPosition() == -1 && layout.getSuggestions() != null) {
            dictionaryAdapter.update(layout.getSuggestions());
        } else {
            int position = layout.getCurrentDictionaryPosition();
            dictionaryList.performItemClick(dictionaryList.getChildAt(position),
                    position,
                    dictionaryList.getItemIdAtPosition(position));
        }

        if (layout.getSuggestions().isEmpty()) {
            emptySuggestionsView.setVisibility(View.VISIBLE);
            dictionaryList.setVisibility(View.GONE);
        } else {
            emptySuggestionsView.setVisibility(View.GONE);
            dictionaryList.setVisibility(View.VISIBLE);
        }

        // Highlight the selected symbol
        for (Symbol symbol : layout.getSymbols()) {
            for (int i = 0; i < symbolViewMap.size(); i++) {

                int column = i % layout.getRowSize();
                int row = i / layout.getRowSize();
                symbolViewMap.get(symbol).setBackgroundResource(R.drawable.text_selection_colors);

                if (symbol != null && symbolViewMap.containsKey(symbol)) {

                    if (symbol.equals(layout.getCurrentSymbol())) { // layout.getCurrentColumn() == column && layout.getCurrentRow() == row &&
                        symbolViewMap.get(symbol).setSelected(true);
                    } else {
                        symbolViewMap.get(symbol).setSelected(false);
                    }
                } // if loop

            } // for loop
        } // for loop
    }

}// end of class


// skal inn i buildGUI
 /*if (layout.getCurrentState().equals(ETOSLayout.State.SELECT_MENU)) {
            dictionaryList.setAdapter(menuAdapter);
            menuAdapter.clear();
            for (Symbol item : layout.getMenuOptions()) {
                listItems.add(item);
            }
            // menuAdapter.notifyDataSetChanged();
        } else {
            dictionaryList.setAdapter(dictionaryAdapter);
            // dictionaryAdapter.notifyDataSetChanged();
        }*/

// skal inn i update gui

//todo fix the menu. this makes the cursor disaper on dictionary
   /*     if (layout.getCurrentState().equals(ETOSLayout.State.SELECT_MENU)) {
            dictionaryList.setAdapter(menuAdapter);
            menuAdapter.clear();
            for (Symbol item : layout.getMenuOptions()) {
                listItems.add(item);
            }
            // menuAdapter.notifyDataSetChanged();
        } else {
            dictionaryList.setAdapter(dictionaryAdapter);
            // dictionaryAdapter.notifyDataSetChanged();
        }*/

// todo
        /*if (layout.getCurrentMenuPosition() == -1 && layout.getMenuOptions() != null) {
            menuAdapter.update(layout.getMenuOptions());
        } else {
            int pos = layout.getCurrentMenuPosition();
            dictionaryList.performItemClick(dictionaryList.getChildAt(pos),
                    pos,
                    dictionaryList.getItemIdAtPosition(pos));
        } */