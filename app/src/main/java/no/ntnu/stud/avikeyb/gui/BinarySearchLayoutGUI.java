package no.ntnu.stud.avikeyb.gui;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.stud.avikeyb.R;
import no.ntnu.stud.avikeyb.backend.Keyboard;
import no.ntnu.stud.avikeyb.backend.Symbol;
import no.ntnu.stud.avikeyb.backend.layouts.BinarySearchLayout;
import no.ntnu.stud.avikeyb.gui.utils.LayoutLoader;

/**
 * Created by pitmairen on 13/02/2017.
 */
public class BinarySearchLayoutGUI extends LayoutGUI {

    private BinarySearchLayout layout;
    private Activity activity;
    private HashMap<Symbol, View> symbolViewMap;
    private SuggestionsAdapter suggestionsAdapter;


    public BinarySearchLayoutGUI(Activity activity, Keyboard keyboard, BinarySearchLayout layout) {
        super(keyboard, layout);

        this.activity = activity;
        this.layout = layout;
        symbolViewMap = new HashMap<>();
        suggestionsAdapter = new SuggestionsAdapter();
    }

    @Override
    public ViewGroup buildGUI() {

        LayoutLoader loader = new LayoutLoader(activity, R.layout.layout_binsearch);
        for (Symbol symbol : layout.getSymbols()) {
            if (loader.hasSymbol(symbol)) {
                TextView view = (TextView) loader.getViewForSymbol(symbol);
                view.setText(symbol.getContent());
                view.setTypeface(null, Typeface.BOLD);
                symbolViewMap.put(symbol, view);
            }
        }

        RecyclerView suggestionsList = (RecyclerView) loader.getViewById(R.id.suggestionsList);
        suggestionsList.setLayoutManager(new LinearLayoutManager(activity));
        suggestionsList.setItemAnimator(new DefaultItemAnimator());
        suggestionsList.setAdapter(suggestionsAdapter);

        return (ViewGroup) loader.getLayout();
    }

    @Override
    public void updateGUI() {

        suggestionsAdapter.notifyDataSetChanged();

        for (Map.Entry<Symbol, View> it : symbolViewMap.entrySet()) {

            if (layout.symbolIsActive(it.getKey())) {

                if (it.getValue() instanceof TextView) {
                    ((TextView) it.getValue()).setTextColor(ContextCompat.getColor(activity, R.color.binsearch_active_fg));
                }

                if (layout.symbolIsActiveLeft(it.getKey()))
                    it.getValue().setBackgroundColor(ContextCompat.getColor(activity, (R.color.binsearch_active_left_bg)));
                else
                    it.getValue().setBackgroundColor(ContextCompat.getColor(activity, R.color.binsearch_active_right_bg));
            } else {
                it.getValue().setBackgroundColor(ContextCompat.getColor(activity, R.color.binsearch_inactive_bg));
                if (it.getValue() instanceof TextView) {
                    ((TextView) it.getValue()).setTextColor(ContextCompat.getColor(activity, R.color.binsearch_inactive_fg));
                }
            }
        }
    }


    // Recycler view adapter
    private class SuggestionsAdapter extends RecyclerView.Adapter<ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.binsearch_suggestion_item, parent, false);
            ViewHolder myViewHolder = new ViewHolder((TextView) view.findViewById(android.R.id.text1));
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String suggestion = layout.getSuggestions().get(position);

            if (layout.suggestionIsActive(suggestion)) {
                holder.item.setTextColor(ContextCompat.getColor(activity, R.color.binsearch_active_fg));
                if (layout.suggestionIsLeft(suggestion)) {
                    holder.item.setBackgroundColor(ContextCompat.getColor(activity, R.color.binsearch_active_left_bg));
                } else {
                    holder.item.setBackgroundColor(ContextCompat.getColor(activity, R.color.binsearch_active_right_bg));
                }
            } else {
                holder.item.setBackgroundColor(ContextCompat.getColor(activity, R.color.binsearch_inactive_bg));
                holder.item.setTextColor(ContextCompat.getColor(activity, R.color.binsearch_inactive_fg));
            }
            holder.item.setText(suggestion);
        }

        @Override
        public int getItemCount() {
            return layout.getSuggestions().size();
        }
    }

    // View holder for the recycler view
    private static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item;

        public ViewHolder(TextView view) {
            super(view);
            view.setTypeface(null, Typeface.BOLD);
            this.item = view;
        }
    }
}
