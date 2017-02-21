package no.ntnu.stud.avikeyb.gui.utils;

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

    Context context;
    Symbol[] data = null;
    int layoutResourceId;

    public Adapter(Context context, ArrayList<Symbol> symbols) {
        super(context, 0, symbols);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Symbol symbol = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sidemenu, parent, false);
        }
        Symbol item = data[position];
        TextView textview = (TextView) convertView.findViewById(R.id.sym_setting);
        TextView textview2 = (TextView) convertView.findViewById(R.id.sym_profile);
        TextView textview3 = (TextView) convertView.findViewById(R.id.sym_option1);
        TextView textview4 = (TextView) convertView.findViewById(R.id.sym_option2);
        TextView textview5 = (TextView) convertView.findViewById(R.id.sym_option3);


        return convertView;
    }

}
