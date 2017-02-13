package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Layout;
import no.ntnu.stud.avikeyb.backend.layouts.SimpleExampleLayout;

/**
 * Created by pitmairen on 09/02/2017.
 */

public class SimpleExampleLayoutGUI {

    private Activity activity;

    // Store a reference to all the symbol for easy access when updating the gui
    private List<View> symbolViews;
    private SimpleExampleLayout layout;
    private Keyboard keyboard;

    public SimpleExampleLayoutGUI(Activity activity, Keyboard keyboard, SimpleExampleLayout layout) {

        this.layout = layout;
        this.activity = activity;
        this.keyboard = keyboard;

        symbolViews = new ArrayList<>();

        // Update the gui when the layout state changes
        layout.addLayoutListener(new Layout.LayoutListener() {
            @Override
            public void onLayoutChanged() {
                updateGUI();
            }
        });


    }

    // Build the gui programmatically
    public ViewGroup buildGUI() {

        ScrollView rootWrapper = new ScrollView(activity); // Make the keyboard symbols scrollable

        LinearLayout root = new LinearLayout(activity); // The root layout containing all the symbols
        root.setOrientation(LinearLayout.VERTICAL);

        rootWrapper.addView(root);

        for (int i = 0; i < layout.getSymbolCount(); ) {

            LinearLayout row = new LinearLayout(activity);
            row.setOrientation(LinearLayout.HORIZONTAL);

            // Create rows with 10 symbols on each row

            for (int j = 0; i < layout.getSymbolCount() && j < 10; i++, j++) {

                TextView view = new TextView(activity);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100, 100);
                lp.setMargins(5, 5, 5, 5);
                view.setLayoutParams(lp);
                view.setGravity(Gravity.CENTER);
                view.setText(layout.getSymbolAt(i).getContent());

                symbolViews.add(view);
                row.addView(view);
            }
            root.addView(row);
        }

        updateGUI();
        return rootWrapper;
    }


    private void updateGUI() {

        // Update the current buffer view
        ((TextView) activity.findViewById(R.id.currentBuffer)).setText(keyboard.getCurrentBuffer());

        // Highlight the selected symbol
        int current = layout.getCurrentPosition();
        for (int i = 0; i < symbolViews.size(); i++) {

            if (current == i) {
                symbolViews.get(i).setBackgroundColor(Color.GREEN);
            } else {
                symbolViews.get(i).setBackgroundColor(Color.GRAY);
            }

        }


    }

}
