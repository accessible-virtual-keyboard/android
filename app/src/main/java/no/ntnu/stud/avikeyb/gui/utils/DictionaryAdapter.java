package no.ntnu.stud.avikeyb.gui.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.ArrayList;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.layouts.ETOSLayout;

/**
 * Created by ingalill on 23/02/2017.
 */

public class DictionaryAdapter extends ArrayAdapter<String> {

    private ArrayList<String> dictionary;
    private ETOSLayout layout;

    public DictionaryAdapter(Activity context, ArrayList<String> dictionary, ETOSLayout layout) {
        super(context, 0, dictionary);
        this.dictionary = dictionary;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String suggestion;
        // if (layout.getCurrentSuggestion().length() < layout.getDictionaryLength()) {
        //     //  suggestion = layout.getSuggestions().get(position); //before

        suggestion = getItem(position);
        //}

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sidemenu, parent, false);
        }
        TextView dictionaryText = (TextView) convertView.findViewById(R.id.listviewtext);
        dictionaryText.setText(suggestion);
        dictionaryText.setPadding(2, 20, 2, 20);

        if (layout.suggestionIsAcvtive(suggestion) && layout.getCurrentSuggestion().equals(suggestion)) {

            dictionaryText.setBackgroundResource(R.color.purpleparty);

        } else {
            dictionaryText.setBackgroundResource(R.color.lightgrey);
        }
        return convertView;
    }
}
