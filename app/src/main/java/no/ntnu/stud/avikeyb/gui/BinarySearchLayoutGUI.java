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
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.BinarySearchLayout;

/**
 * Created by pitmairen on 13/02/2017.
 */
public class BinarySearchLayoutGUI extends LayoutGUI {

    private BinarySearchLayout layout;
    private Activity activity;
    private List<View> symbolViews;


    public BinarySearchLayoutGUI(Activity activity, Keyboard keyboard, BinarySearchLayout layout) {
        super(keyboard, layout);

        this.activity = activity;
        this.layout = layout;
        symbolViews = new ArrayList<>();

    }

    @Override
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

        return rootWrapper;
    }

    @Override
    public void updateGUI() {

        // Update the current buffer view
        ((TextView) activity.findViewById(R.id.currentBuffer)).setText(getKeybaord().getCurrentBuffer());

        for (int i = 0; i < layout.getSymbolCount(); i++) {

            Symbol sym = layout.getSymbolAt(i);

            if (layout.symbolIsActive(sym)) {

                if (layout.symbolIsActiveLeft(sym))
                    symbolViews.get(i).setBackgroundColor(Color.GREEN);
                else
                    symbolViews.get(i).setBackgroundColor(Color.RED);

            } else {
                symbolViews.get(i).setBackgroundColor(Color.GRAY);
            }
        }

    }
}
