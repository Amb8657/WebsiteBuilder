package com.amb8657.websitebuilder;

/** Coordinates selection and gesture state for the visual canvas. */
public final class CanvasInteractionController {
    private final CanvasSelectionController selection;
    private final CanvasGestureState gesture = new CanvasGestureState();

    public CanvasInteractionController(CanvasSelectionController selection) {
        this.selection = selection;
    }

    public void selectAndMove(String id, float x, float y) {
        selection.select(id);
        gesture.beginMove(id, x, y);
    }

    public void selectAndResize(String id, ResizeHandle handle, float x, float y) {
        selection.select(id);
        gesture.beginResize(id, handle, x, y);
    }

    public CanvasGestureState gesture() { return gesture; }

    public void finishGesture() { gesture.reset(); }

    public void clearSelection() {
        gesture.reset();
        selection.clearSelection();
    }
}
