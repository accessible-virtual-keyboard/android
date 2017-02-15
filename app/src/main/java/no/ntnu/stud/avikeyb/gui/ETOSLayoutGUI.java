package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Layout;
import no.ntnu.stud.avikeyb.backend.layouts.ETOSLayout;

/**
 * Created by ingalill on 10/02/2017.
 */

public class ETOSLayoutGUI extends LayoutGUI {

    private Activity activity;
    private List<View> symbolsView;
    private ETOSLayout layout;
    private Resources res; // do not work


    public ETOSLayoutGUI(Activity activity, Keyboard keyboard, ETOSLayout layout) {
        super(keyboard, layout);

        this.layout = layout;
        this.activity = activity;
        symbolsView = new ArrayList<>();

        layout.addLayoutListener(new Layout.LayoutListener() {
            @Override
            public void onLayoutChanged() {
                updateGUI();
            }
        });
    }

    // Build the GUI programmatically.
    public View buildGUI() {
        //  int paddingSize = (int) res.getDimension(R.dimen._10sdp);

        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.setId(View.generateViewId());

        LinearLayout nestedLayout = new LinearLayout(activity);
        nestedLayout.setOrientation(LinearLayout.VERTICAL);
        nestedLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        nestedLayout.setId(View.generateViewId());

        TableLayout tableLayout = new TableLayout(activity);
        tableLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayout.setWeightSum(4); // Should it be 6?
        tableLayout.setStretchAllColumns(true);


        for (int i = 0; i < layout.getSymbolCount(); ) {

            TableRow tableRow = new TableRow(activity);
            tableRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0.35f));
            for (int j = 0; i < layout.getSymbolCount() && j <= 6; i++, j++) {

                TextView view = new TextView(activity);

                view.setText(layout.getSymbolAt(i).getContent());
                view.setPadding(20, 20, 20, 20); // replace with paddingSize
                view.setBackgroundResource(R.color.lightgrey);
                view.setTextColor(Color.BLACK);
                view.setGravity(Gravity.CENTER);
                symbolsView.add(view);
                tableRow.addView(view); // Add the views to the tablerow.
            }
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        }
        nestedLayout.addView(tableLayout);
        root.addView(nestedLayout); // add the nested linear layout to the root linear layout.

        return root;
    }

    @Override
    protected void updateGUI() {
        // should update the buffer. Should highlighet the selected symbol.
    }
}