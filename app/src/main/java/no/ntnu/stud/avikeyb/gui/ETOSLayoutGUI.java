package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
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

public class ETOSLayoutGUI {

    private Activity activity;
    private List<View> symbolsView;
    private ETOSLayout layout;
    private Keyboard keyboard;
    private Resources res; // do not work


    public ETOSLayoutGUI(Activity activity, Keyboard keyboard, ETOSLayout layout) {
     //   res = Resources.getSystem();

        this.layout = layout;
        this.keyboard = keyboard;
        this.activity = activity;
        symbolsView = new ArrayList<>();


        layout.addLayoutListener(new Layout.LayoutListener() {
            @Override
            public void onLayoutChanged() {
                // oppdater gui.
            }

        });
    }


    public View buildGUI() {
      //  int paddingSize = (int) res.getDimension(R.dimen._10sdp);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setId(View.generateViewId());

        LinearLayout nestedLayout = new LinearLayout(activity);
        nestedLayout.setOrientation(LinearLayout.VERTICAL);
        nestedLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        nestedLayout.setId(View.generateViewId());

        TableLayout tableLayout = new TableLayout(activity);
        tableLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayout.setId(View.generateViewId());

        for (int i = 0; i < 7; i++) {
            TableRow tr = new TableRow(activity);
            tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < 6; j++) {
                TextView t = new TextView(activity);
                t.setText("It works");
                t.setPadding(10, 10, 10, 10); // replace with paddingSize
             //   t.setBackground();//lightgrey
                //t.setTextColor(ContextCompat.getColor(context, R.color.black));  // black
                tr.addView(t);
            }
            tableLayout.addView(tr);
        }

        nestedLayout.addView(tableLayout);

        layout.addView(nestedLayout);
        /*TableLayout tableLayout = (TableLayout)getActivity().findViewById(R.id.tablelayout);
        TableRow tablerow1 = (TableRow) getActivity().findViewById(R.id.tableRow1);

        TableRow tablerow2 = new TableRow(getActivity());
        tablerow2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        tableLayout.addView(tablerow2);

        for (int i = 0; i < adaptiveLayout.size(); i++) {
            for (Map.Entry<String, String[]> nextLetter : adaptiveLayout.entrySet()) {
                TextView textView = new TextView(getActivity());

                String key = nextLetter.getKey();
                String[] value = nextLetter.getValue();
               // for (int j = 0; j <6 ; j++) {
                    // textview

                //}

            }
        }*/

        return layout;
    }
}
