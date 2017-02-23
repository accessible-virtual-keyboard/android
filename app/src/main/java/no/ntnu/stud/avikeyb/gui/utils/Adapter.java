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

/**
 * Created by ingalill on 21/02/2017.
 */

public class Adapter extends ArrayAdapter<Symbol> {


    /**
     * Constructor
     *
     * @param context
     * @param symbols
     */
    public Adapter(Context context, ArrayList<Symbol> symbols) {
        super(context, 0, symbols);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position.
        Symbol symbol = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sidemenu, parent, false);
        }

        TextView setting = (TextView) convertView.findViewById(R.id.sym_setting);
        setting.setText(symbol.getContent());
        setting.setPadding(2,20,2,20);

        return convertView;
    }

}
