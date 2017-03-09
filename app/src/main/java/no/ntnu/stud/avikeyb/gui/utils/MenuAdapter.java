package no.ntnu.stud.avikeyb.gui.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_list_item, parent, false);
        }

        TextView listviewText = (TextView) convertView.findViewById(R.id.text_item);
        listviewText.setText(symbol.getContent());
        listviewText.setPadding(2, 20, 2, 20);
        listviewText.setTextColor(Color.BLACK);

        listviewText.setBackgroundResource(R.drawable.text_selection_colors);
         /*  if (symbol == layout.getCurrentSymbol()) {
            listviewText.setBackgroundResource(R.color.selected);
        } else {
            listviewText.setBackgroundResource(R.color.background);
        }*/

        return convertView;
    }

    //  todo
    public void update(Symbol updatedList) { // was List<Symbol>
        clear();
        addAll(updatedList);
        notifyDataSetChanged();
    }

}
