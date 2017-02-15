package no.ntnu.stud.avikeyb.backend.layouts;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.avikeyb.backend.Layout;

/**
 * Base implementation that implement the layout listeners
 */
public abstract class BaseLayout implements Layout {

    private List<LayoutListener> listeners;

    public BaseLayout() {
        listeners = new ArrayList<>();
    }

    @Override
    public void addLayoutListener(LayoutListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLayoutListener(LayoutListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyLayoutListeners() {
        for (Layout.LayoutListener listener : listeners) {
            listener.onLayoutChanged();
        }
    }
}
