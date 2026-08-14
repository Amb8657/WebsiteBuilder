package com.amb8657.websitebuilder;

/** Keeps selection state independent from element styling. */
public final class SelectionState {
    private String selectedId;

    public void select(String id) { selectedId = id; }
    public void clear() { selectedId = null; }
    public boolean isSelected(String id) {
        return selectedId != null && selectedId.equals(id);
    }
    public String getSelectedId() { return selectedId; }
}
