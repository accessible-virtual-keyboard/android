package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.Symbols;
import no.ntnu.stud.avikeyb.backend.layouts.AdaptiveLayout;
import no.ntnu.stud.avikeyb.gui.utils.GenericSuggestions;
import no.ntnu.stud.avikeyb.gui.utils.LayoutLoader;
import no.ntnu.stud.avikeyb.gui.utils.TextAdapter;

import static android.content.ContentValues.TAG;

/**
 * Created by ingalill on 10/02/2017.
 */
public class AdaptiveLayoutGUI extends LayoutGUI {


    private Activity activity;
    private AdaptiveLayout layout;
    private TextAdapter dictionaryAdapter;
    private ListView dictionaryList;
    private View previousViewSelected;


    // Store a reference to all the symbol for easy access when updating the gui
    private ArrayList<View> symbolViews = new ArrayList<>();

    public AdaptiveLayoutGUI(Activity activity, Keyboard keyboard, AdaptiveLayout layout) {
        super(keyboard, layout);

        this.layout = layout;
        this.activity = activity;
    }


    public View buildGUI() {

        // The orders of the symbols in this array must be in the same order as the symbols are
        // defined in the xml layout. This is because we need to know the position of each view
        // in the layout, as the views are reused when the adaptive layout changes by
        // replacing the content of the view with the content of the symbol in the corresponding
        // position in the backend layout.
        //
        // This means that as the layout changes the content of the views and the id of the views
        // will not match, but this does not matter as we are only interested in the position of
        // the views. The id of the views are only used initially to load the views in the correct
        // order.

        Symbol[] symbols = Symbols.merge(Symbols.alphabet(), Symbols.build(Symbol.SPACE, Symbol.SEND, Symbol.DICTIONARY));

        LayoutLoader loader = new LayoutLoader(activity, R.layout.layout_adaptive);
        for (Symbol symbol : symbols) {
            if (loader.hasSymbol(symbol)) {
                TextView view = (TextView) loader.getViewForSymbol(symbol);

                view.setText(symbol.getContent());
                view.setTextColor(Color.BLACK);
                symbolViews.add(view);
            }
        }

        dictionaryList = (ListView) loader.getViewById(R.id.listview);
        dictionaryAdapter = new TextAdapter(activity.getApplicationContext(), R.id.listview, new ArrayList<String>());

        dictionaryList.setAdapter(dictionaryAdapter);
        dictionaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                if (previousViewSelected != null) {
                    previousViewSelected.setSelected(false);
                }
                if (view != null) {
                    view.setSelected(true);
                    previousViewSelected = view;
                }

            }
        });
        dictionaryList.setEnabled(false);

        // set the listview to show the text "nothing to show".
        TextView empty = new TextView(activity);
        empty.setText("Nothing to show");
        ((ViewGroup) dictionaryList.getParent()).addView(empty);
        dictionaryList.setEmptyView(empty);


        return loader.getLayout();
    }

    public void updateGUI() {

        if (layout.getCurrentState() == AdaptiveLayout.State.SUGGESTION_SELECTION) {
            updateSuggestions();
        } else {
            updateLayout();
        }
    }

    private void updateLayout() {

        if (layout.getCurrentSuggestion() == -1 && layout.getSuggestions() != null) { // -1 on etos and mobile. -1 does not show the dictionary
            dictionaryAdapter.update(layout.getSuggestions());
        }

        Symbol[] currentLayout = layout.getSymbols();

        for (int i = 0; i < symbolViews.size(); i++) {
            TextView view = (TextView) symbolViews.get(i);

            // Because the layout of the symbols changes all the time we have to set the view
            // content each time we update.
            view.setText(currentLayout[i].getContent());

            int column = i % layout.getRowSize();
            int row = i / layout.getRowSize();
            view.setBackgroundResource(R.drawable.text_selection_colors);

            if (layout.getCurrentRow() == row) {
                view.setSelected(true);
                if (layout.getCurrentState() == AdaptiveLayout.State.COLUMN_SELECTION && layout.getCurrentColumn() == column) {
                    view.setBackgroundResource(R.color.rowselected);
                }
            } else {
                view.setSelected(false); // Default non active background color
            }
        }
    }

    private void updateSuggestions() {

        Symbol[] currentLayout = layout.getSymbols();
        for (int i = 0; i < symbolViews.size(); i++) {

            TextView view = (TextView) symbolViews.get(i);
            // Because the layout of the symbols changes all the time we have to set the view
            // content each time we update.
            view.setText(currentLayout[i].getContent());

            view.setSelected(false);
        }

        if (layout.getSuggestions() != null) {
            int position = layout.getCurrentSuggestion();

            dictionaryList.performItemClick(dictionaryList.getChildAt(position),
                    position,
                    dictionaryList.getItemIdAtPosition(position));
        }

    }


    //todo is this method necessary?
    private boolean checkIfSuggestionIsActive(String suggestion) {
        return layout.getCurrentState() == AdaptiveLayout.State.SUGGESTION_SELECTION
                && layout.getSuggestions().get(layout.getCurrentSuggestion()).equals(suggestion);
    }
}





