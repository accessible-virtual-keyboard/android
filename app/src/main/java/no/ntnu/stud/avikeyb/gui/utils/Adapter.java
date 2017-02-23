package no.ntnu.stud.avikeyb.gui.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.ETOSLayout;

/**
 * Created by ingalill on 21/02/2017.
 */

public class Adapter extends ArrayAdapter<Symbol> {

    Activity context;
    ArrayList<Symbol> symbols;
    ETOSLayout layout;

    /**
     * Constructor
     *
     * @param context
     * @param symbols
     */
    public Adapter(Activity context, ArrayList<Symbol> symbols, ETOSLayout layout) {
        super(context, 0, symbols);
        this.context = context;
        this.symbols = symbols;
        this.layout = layout;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutLoader loader = new LayoutLoader(context, R.layout.sidemenu);
        // Get the data item for this position.
        Symbol symbol = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sidemenu, parent, false);
        }

        TextView setting = (TextView) convertView.findViewById(R.id.listviewtext);
        setting.setText(symbol.getContent());
        setting.setPadding(2, 20, 2, 20);

        System.out.println("Symbol is " + symbol);
        System.out.println("The current position " + layout.getCurrentSymbol());

        if (symbol == layout.getCurrentSymbol()) {
            setting.setBackgroundResource(R.color.purpleparty);
        } else {
            setting.setBackgroundResource(R.color.lightgrey);
        }

        return convertView;
    }

}
