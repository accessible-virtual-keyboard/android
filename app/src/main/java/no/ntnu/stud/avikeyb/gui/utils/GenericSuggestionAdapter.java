package no.ntnu.stud.avikeyb.gui.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import no.ntnu.stud.avikeyb.R;

/**
 * Created by pitmairen on 01/03/2017.
 */

public class GenericSuggestionAdapter extends ArrayAdapter<String> {

    public interface SuggestionStateInfo {
        boolean isActive(String suggestion);
    }

    private SuggestionStateInfo stateInfo;

    public GenericSuggestionAdapter(Activity context, SuggestionStateInfo stateInfo) {
        super(context, 0);
        this.stateInfo = stateInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String suggestion = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.binsearch_suggestion_item, parent, false);
        }

        TextView suggestionView = (TextView) convertView.findViewById(android.R.id.text1);

        suggestionView.setText(suggestion);

        System.out.println(suggestion);
        System.out.println(suggestionView);

        System.out.println(stateInfo.isActive(suggestion));
        if (stateInfo.isActive(suggestion)) {
            suggestionView.setBackgroundColor(Color.RED);
        } else {
            suggestionView.setBackgroundColor(Color.GRAY);

        }

        return convertView;
    }
}