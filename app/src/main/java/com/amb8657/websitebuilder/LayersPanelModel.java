package com.amb8657.websitebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** UI model for the Layers panel. Entries are ordered front-to-back for display. */
public final class LayersPanelModel {
    private final List<String> entries = new ArrayList<>();

    public void refresh(LayerManager manager) {
        entries.clear();
        List<String> layers = manager == null ? Collections.emptyList() : manager.getLayers();
        for (int i = layers.size() - 1; i >= 0; i--) entries.add(layers.get(i));
    }

    public List<String> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
