package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.BinarySearchLayout;
import no.ntnu.stud.avikeyb.gui.utils.LayoutLoader;

/**
 * Created by pitmairen on 13/02/2017.
 */
public class BinarySearchLayoutGUI extends LayoutGUI {

    private BinarySearchLayout layout;
    private Activity activity;
    private HashMap<Symbol, View> symbolViewMap;


    public BinarySearchLayoutGUI(Activity activity, Keyboard keyboard, BinarySearchLayout layout) {
        super(keyboard, layout);

        this.activity = activity;
        this.layout = layout;
        symbolViewMap = new HashMap<>();
    }

    @Override
    public ViewGroup buildGUI() {

        LayoutLoader loader = new LayoutLoader(activity, R.layout.layout_binsearch);
        for (Symbol symbol : layout.getSymbols()) {
            if (loader.hasSymbol(symbol)) {
                TextView view = (TextView) loader.getViewForSymbol(symbol);
                view.setText(symbol.getContent());
                view.setTypeface(null, Typeface.BOLD);
                symbolViewMap.put(symbol, view);
            }
        }
        return (ViewGroup) loader.getLayout();
    }

    @Override
    public void updateGUI() {

        for (Map.Entry<Symbol, View> it : symbolViewMap.entrySet()) {

            if (layout.symbolIsActive(it.getKey())) {

                if(it.getValue() instanceof TextView){
                    ((TextView)it.getValue()).setTextColor(Color.parseColor("#484848"));
                }

                if (layout.symbolIsActiveLeft(it.getKey()))
                    it.getValue().setBackgroundColor(Color.parseColor("#46A8CD"));
                else
                    it.getValue().setBackgroundColor(Color.parseColor("#F5B821"));
            } else {
                it.getValue().setBackgroundColor(Color.parseColor("#eeeeee"));
                if(it.getValue() instanceof TextView){
                    ((TextView)it.getValue()).setTextColor(Color.parseColor("#cccccc"));
                }
            }
        }
    }
}
