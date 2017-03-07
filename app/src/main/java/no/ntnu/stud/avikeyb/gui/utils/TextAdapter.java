package no.ntnu.stud.avikeyb.gui.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import no.ntnu.stud.avikeyb.R;

/**
 * Created by Tor-Martin Holen on 27-Feb-17.
 */

public class TextAdapter extends ArrayAdapter<String> {
    private int previous;

    public TextAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position.
        String suggestionText = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.text_list_item, parent, false);
        }

        // Lookup view for data population.
        TextView description = (TextView) convertView.findViewById(R.id.text_item);
        description.setBackgroundResource(R.drawable.text_selection_colors);
        description.setTextColor(Color.BLACK);
        description.setText(suggestionText);


        return super.getView(position, convertView, parent);
    }

    public void update(List<String> updatedList) {
        clear();
        addAll(updatedList);
        notifyDataSetChanged();
    }
}
