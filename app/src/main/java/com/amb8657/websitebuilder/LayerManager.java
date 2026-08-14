package com.amb8657.websitebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Maintains visual stacking order independently from the editor UI. */
public final class LayerManager {
    private final List<String> layers = new ArrayList<>(); // back -> front

    public void setLayers(List<String> ids) {
        layers.clear();
        if (ids != null) layers.addAll(ids);
    }

    public List<String> getLayers() {
        return Collections.unmodifiableList(layers);
    }

    public boolean bringForward(String id) { return moveBy(id, 1); }
    public boolean sendBackward(String id) { return moveBy(id, -1); }

    public boolean bringToFront(String id) {
        int index = layers.indexOf(id);
        if (index < 0 || index == layers.size() - 1) return false;
        layers.remove(index);
        layers.add(id);
        return true;
    }

    public boolean sendToBack(String id) {
        int index = layers.indexOf(id);
        if (index <= 0) return false;
        layers.remove(index);
        layers.add(0, id);
        return true;
    }

    public boolean bringAbove(String id, String referenceId) {
        return moveRelative(id, referenceId, true);
    }

    public boolean sendBelow(String id, String referenceId) {
        return moveRelative(id, referenceId, false);
    }

    private boolean moveBy(String id, int delta) {
        int from = layers.indexOf(id);
        int to = from + delta;
        if (from < 0 || to < 0 || to >= layers.size()) return false;
        Collections.swap(layers, from, to);
        return true;
    }

    private boolean moveRelative(String id, String referenceId, boolean above) {
        if (id == null || referenceId == null || id.equals(referenceId)) return false;
        int from = layers.indexOf(id);
        int target = layers.indexOf(referenceId);
        if (from < 0 || target < 0) return false;
        layers.remove(from);
        target = layers.indexOf(referenceId);
        layers.add(above ? target + 1 : target, id);
        return true;
    }
}
