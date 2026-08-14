package com.amb8657.websitebuilder;

/** Describes the transient selection UI around the currently selected element. */
public final class SelectionOverlayModel {
    public boolean visible;
    public String elementId;
    public int x;
    public int y;
    public int width;
    public int height;

    public void show(String id, int x, int y, int width, int height) {
        visible = true;
        elementId = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void updateBounds(int x, int y, int width, int height) {
        if (!visible) return;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void hide() {
        visible = false;
        elementId = null;
    }
}
