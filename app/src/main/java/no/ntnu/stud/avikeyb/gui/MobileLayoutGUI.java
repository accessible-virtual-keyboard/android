package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Layout;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.MobileDictionaryLayout;
import no.ntnu.stud.avikeyb.backend.layouts.MobileLayout;
import no.ntnu.stud.avikeyb.gui.utils.LayoutLoader;

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

        for (Symbol symbol : layout.getSymbols()) {
            if (symbol != null && loader.hasSymbol(symbol)) {
                TextView btn = (TextView) loader.getViewForSymbol(symbol);

/*                if (symbol.equals(Symbol.SEND)) {
                    btn.setBackgroundResource(R.drawable.btn_send);
                } else if (symbol.equals(Symbol.SPACE)) {
                    btn.setBackgroundResource(R.drawable.btn_spacebar);
                } else {*/

                btn.setText(symbol.getContent());
                btn.setTextColor(Color.BLACK);
                btn.setBackgroundResource(R.color.selected_button);

                if(symbol.equals(Symbol.DICTIONARY)){
                    btn.setText(Symbol.DICTIONARY_UNICODE_SYMBOL.getContent());
                }

                //}
                symbolViewMap.put(symbol, btn);
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
        if(layout.getSuggestions() != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity.getApplicationContext(),
                    android.R.layout.simple_list_item_1, layout.getSuggestions());
            dictionaryList.setAdapter(adapter);
            /*dictionaryList.getItemAtPosition(layout.getMarkedWord());
            dictionaryList.getSelectedView().setBackgroundResource(R.color.colorPrimary);*/
        }


        previouslyMarked = newlyMarked;

    }
}
