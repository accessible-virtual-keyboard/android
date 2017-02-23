package no.ntnu.stud.avikeyb.gui.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.layouts.ETOSLayout;

/**
 * Created by ingalill on 23/02/2017.
 */

public class DictionaryAdapter extends ArrayAdapter<String> {

    ArrayList<String> dictionary;
    ETOSLayout layout;

    public DictionaryAdapter(Context context, ArrayList<String> dictionary) {
        super(context, 0, dictionary);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sidemenu, parent, false);
        }
        TextView dictionaryText = (TextView) convertView.findViewById(R.id.listviewtext);
        dictionaryText.setText("Insert dictionary options here");
        dictionaryText.setPadding(2, 20, 2, 20);

        if (0 < 1) { // CHANGE IF LOOP.
            dictionaryText.setBackgroundResource(R.color.purpleparty);
        } else {
            dictionaryText.setBackgroundResource(R.color.lightgrey);
        }

        return convertView;
    }

}
