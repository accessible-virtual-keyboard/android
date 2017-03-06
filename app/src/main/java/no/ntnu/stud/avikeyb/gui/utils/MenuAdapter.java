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

public class MenuAdapter extends ArrayAdapter<Symbol> {

    private ArrayList<Symbol> symbols;
    private ETOSLayout layout;

    /**
     * Constructor
     *
     * @param context
     * @param symbols
     */
    public MenuAdapter(Activity context, ArrayList<Symbol> symbols, ETOSLayout layout) {
        super(context, 0, symbols);
        this.symbols = symbols;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position.
        Symbol symbol = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sidemenu, parent, false);
        }

        TextView listviewText = (TextView) convertView.findViewById(R.id.listviewtext);
        listviewText.setText(symbol.getContent());
        listviewText.setPadding(2, 20, 2, 20);

        if (symbol == layout.getCurrentSymbol()) {
            listviewText.setBackgroundResource(R.color.marked_button);
        } else {
            listviewText.setBackgroundResource(R.color.background_button);
        }
        return convertView;
    }

}
