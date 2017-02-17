package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.HashMap;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Layout;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.MobileLayout;

/**
 * Created by Tor-Martin Holen on 15-Feb-17.
 */

public class MobileLayoutGUI extends LayoutGUI {

    private MobileLayout layout;
    private Activity activity;
    private HashMap<Symbol, View> symbolViewMap;

    public MobileLayoutGUI(Activity activity, Keyboard keyboard, Layout layout) {
        super(keyboard, layout);
        this.activity = activity;
        this.layout = (MobileLayout) layout;
    }

    @Override
    protected View buildGUI() {
        TableLayout tableLayout = new TableLayout(activity);
        tableLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tableLayout.setStretchAllColumns(true);

        int rows = layout.getLayoutSymbols().length;
        for (int i = 0; i < rows; i++) {
            int columns = layout.getLayoutSymbols()[i].length;

            TableRow row = new TableRow(activity);
            row.setWeightSum(3);
            row.setBackgroundResource(R.color.mobileLayoutRowBackground);
            tableLayout.addView(row);
            TableLayout.LayoutParams  rowParams = (TableLayout.LayoutParams) row.getLayoutParams();
            rowParams.setMargins(10,10,10,10);
            row.setLayoutParams(rowParams);

            for (int j = 0; j < 3; j++) {

                LinearLayout mobileContainer = new LinearLayout(activity);
                row.addView(mobileContainer);
                mobileContainer.setWeightSum(3);
                LinearLayout.LayoutParams mobContParams = (LinearLayout.LayoutParams) mobileContainer.getLayoutParams();
                mobContParams.weight = 1;
                mobContParams.setMargins(15,10,15,10);
                mobileContainer.setBackgroundResource(R.color.mobileLayoutButtonGroupBackground);

                if(j < columns){
                    int symbols = layout.getLayoutSymbols()[i][j].length;

                    for (int k = 0; k < 3; k++) {
                        final Button textButton = new Button(activity);
                        mobileContainer.addView(textButton);

                        //Must be done after adding btn to container
                        LinearLayout.LayoutParams btnParams = (LinearLayout.LayoutParams) textButton.getLayoutParams();
                        btnParams.weight = 1;
                        btnParams.width = 0;
                        btnParams.height = 110;
                        btnParams.setMargins(15, 10, 15, 10); // llp.setMargins(left, top, right, bottom);
                        textButton.setLayoutParams(btnParams);
                        textButton.setPadding(0,0,0,1);
                        if(k < symbols){
                            String text = layout.getLayoutSymbols()[i][j][k].getContent();
                            if (text.toLowerCase().equals("send")){
                                textButton.setBackgroundResource(R.drawable.btn_send);
                            }else if(text.equals(" ")){
                                textButton.setBackgroundResource(R.drawable.btn_spacebar);
                            }
                            else{
                                textButton.setText(text);
                                textButton.setBackgroundResource(R.color.mobileLayoutButtonBackground);
                            }
                        }else{
                            textButton.setBackgroundResource(R.color.mobileLayoutUnusedButtonBackground);
                        }
                    }
                }
            }
        }

        return tableLayout;
    }

    @Override
    protected void updateGUI() {

    }
}
