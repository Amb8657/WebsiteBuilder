package com.amb8657.websitebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** State for the optional Layers panel, shown in front-to-back order. */
public final class LayerPanelState {
    private final List<String> visibleLayers = new ArrayList<>();
    private boolean open;

    public void setLayers(List<String> backToFront) {
        visibleLayers.clear();
        if (backToFront != null) {
            for (int i = backToFront.size() - 1; i >= 0; i--) {
                visibleLayers.add(backToFront.get(i));
            }
        }
    }

    public List<String> getVisibleLayers() {
        return Collections.unmodifiableList(visibleLayers);
    }

    public void setOpen(boolean value) { open = value; }
    public boolean isOpen() { return open; }
}
