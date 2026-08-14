package com.amb8657.websitebuilder;

/** Tracks an active canvas gesture without coupling gesture state to the Activity. */
public final class CanvasGestureState {
    public enum Mode { NONE, MOVE, RESIZE }

    public Mode mode = Mode.NONE;
    public String elementId;
    public ResizeHandle resizeHandle;
    public float startX;
    public float startY;

    public void beginMove(String id, float x, float y) {
        mode = Mode.MOVE;
        elementId = id;
        resizeHandle = null;
        startX = x;
        startY = y;
    }

    public void beginResize(String id, ResizeHandle handle, float x, float y) {
        mode = Mode.RESIZE;
        elementId = id;
        resizeHandle = handle;
        startX = x;
        startY = y;
    }

    public void reset() {
        mode = Mode.NONE;
        elementId = null;
        resizeHandle = null;
    }
}
