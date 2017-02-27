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
import no.ntnu.stud.avikeyb.backend.layouts.MobileLayout;
import no.ntnu.stud.avikeyb.gui.utils.LayoutLoader;
import no.ntnu.stud.avikeyb.gui.utils.MobileDictionaryAdapter;

/**
 * Created by Tor-Martin Holen on 15-Feb-17.
 */

public class MobileLayoutGUI extends LayoutGUI {

    private MobileLayout layout;
    private Activity activity;
    private HashMap<Symbol, View> symbolViewMap = new HashMap<>();
    private ArrayList<Symbol> previouslyMarked = new ArrayList<>();
    private int layoutResource;
    private ListView dictionaryList;
    private MobileDictionaryAdapter dictionaryListAdapter;
    private int previousDictionaryListItem = -1;
    private View previousViewSelected;

    public MobileLayoutGUI(Activity activity, Keyboard keyboard, MobileLayout layout, int layoutResource) {
        super(keyboard, layout);
        this.activity = activity;
        this.layout = layout;
        this.layoutResource = layoutResource;
    }


    @Override
    protected View buildGUI() {
        LayoutLoader loader = new LayoutLoader(activity, layoutResource);

        dictionaryList = (ListView) loader.getViewById(R.id.listview);
        dictionaryListAdapter = new MobileDictionaryAdapter(activity.getApplicationContext(), R.id.listview, new ArrayList<>());
        dictionaryList.setAdapter(dictionaryListAdapter);
        dictionaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                if(previousViewSelected != null){
                    previousViewSelected.setSelected(false);
                }
                if(view != null){
                    view.setSelected(true);
                    previousViewSelected = view;
                }

            }
        });
/*
        dictionaryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dictionaryList.getAdapter().getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        for (Symbol symbol : layout.getSymbols()) {
            if (symbol != null && loader.hasSymbol(symbol)) {
                TextView guiTextTile = (TextView) loader.getViewForSymbol(symbol);

/*                if (symbol.equals(Symbol.SEND)) {
                    btn.setBackgroundResource(R.drawable.btn_send);
                } else if (symbol.equals(Symbol.SPACE)) {
                    btn.setBackgroundResource(R.drawable.btn_spacebar);
                } else {*/

                guiTextTile.setText(symbol.getContent());
                guiTextTile.setTextColor(Color.BLACK);
                guiTextTile.setBackgroundResource(R.drawable.mobile_selection_colors);

                if(symbol.equals(Symbol.DICTIONARY)){
                    guiTextTile.setText(Symbol.DICTIONARY_UNICODE_SYMBOL.getContent());
                }

                //}
                symbolViewMap.put(symbol, guiTextTile);
            }
        }

        return loader.getLayout();

    }

    @Override
    protected void updateGUI() {
        // Highlight the selected symbol
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
        if(layout.getMarkedWord() == -1 && layout.getSuggestions() != null){
            dictionaryListAdapter.update(layout.getSuggestions());
        }else {
            int position = layout.getMarkedWord();
            dictionaryList.performItemClick(dictionaryList.getChildAt(position),
                    position,
                    dictionaryList.getItemIdAtPosition(position));
            if(layout.getSuggestions() != null){
                int numberOfSuggestions = layout.getSuggestions().size() <= layout.getMaxPossibleSuggestions() ? layout.getSuggestions().size(): layout.getMaxPossibleSuggestions();
                if(position >= numberOfSuggestions/2){
                    dictionaryList.smoothScrollToPosition(numberOfSuggestions);
                }else{
                    dictionaryList.smoothScrollToPosition(0);
                }
            }


        }

        previouslyMarked = newlyMarked;

    }
}
