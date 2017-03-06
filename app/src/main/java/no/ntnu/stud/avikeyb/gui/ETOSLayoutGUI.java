package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.ETOSLayout;
import no.ntnu.stud.avikeyb.gui.utils.MenuAdapter;
import no.ntnu.stud.avikeyb.gui.utils.LayoutLoader;
import no.ntnu.stud.avikeyb.gui.utils.MobileDictionaryAdapter;

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
    private MobileDictionaryAdapter dictionaryAdapter;
    private View previousViewSelected;
    private LayoutLoader loader;

    public ETOSLayoutGUI(Activity activity, Keyboard keyboard, ETOSLayout layout) {
        super(keyboard, layout);
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

        menuAdapter = new MenuAdapter(activity, listItems, layout);
        dictionaryAdapter = new MobileDictionaryAdapter(activity.getApplicationContext(), R.id.listview, new ArrayList<String>());

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
        //dictionaryList.setEnabled(false);


        return (ViewGroup) loader.getLayout();
    }

    public void updateGUI() {

        // switch between the two adapters.
        /*if (layout.getMenuState().equals(ETOSLayout.State.SELECT_MENU)) {
            listview.setAdapter(menuAdapter);
            menuAdapter.clear();
            for (Symbol item : layout.getMenuOptions()) {
                listItems.add(item);
            }
            menuAdapter.notifyDataSetChanged();
        }*/  //else {
        if(layout.getCurrentDictionaryPosition() == -1 && layout.getSuggestions() != null){
            dictionaryAdapter.update(layout.getSuggestions());
        }else{
            int position = layout.getCurrentDictionaryPosition();
            dictionaryList.performItemClick(dictionaryList.getChildAt(position),
                    position,
                    dictionaryList.getItemIdAtPosition(position));
        }


        // }

        // Highlight the selected symbol
        int current = layout.getCurrentPosition();
        int index = 0;

        for (Symbol symbol : layout.getSymbols()) {

            if (symbol != null && symbolViewMap.containsKey(symbol)) {
                if (current == index) {
                    symbolViewMap.get(symbol).setBackgroundResource(R.color.purpleparty);
                } else {
                    symbolViewMap.get(symbol).setBackgroundResource(R.color.lightgrey);
                }
            }
            index++;
        }
    }

}// end of class