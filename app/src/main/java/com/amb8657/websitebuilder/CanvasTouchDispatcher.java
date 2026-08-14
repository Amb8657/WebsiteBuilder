package com.amb8657.websitebuilder;

/** UI-neutral dispatcher for ACTION_DOWN/MOVE/UP style canvas gestures. */
public final class CanvasTouchDispatcher {
    private final CanvasInteractionController interaction;
    private final ElementGestureEngine engine = new ElementGestureEngine();
    private float startX;
    private float startY;
    private MoveController.Position moveStart;
    private ResizeController.Geometry resizeStart;

    public CanvasTouchDispatcher(CanvasInteractionController interaction) {
        this.interaction = interaction;
    }

    public void beginMove(String id, float x, float y, MoveController.Position start) {
        startX = x;
        startY = y;
        moveStart = start;
        resizeStart = null;
        interaction.selectAndMove(id, x, y);
    }

    public MoveController.Position updateMove(float x, float y) {
        if (moveStart == null) return null;
        return engine.move(moveStart, TouchGeometry.deltaX(startX, x), TouchGeometry.deltaY(startY, y));
    }

    public void beginResize(String id, ResizeHandle handle, float x, float y, ResizeController.Geometry start) {
        startX = x;
        startY = y;
        resizeStart = start;
        moveStart = null;
        interaction.selectAndResize(id, handle, x, y);
    }

    public ResizeController.Geometry updateResize(float x, float y) {
        if (resizeStart == null) return null;
        return engine.resize(resizeStart, interaction.gesture().resizeHandle,
                TouchGeometry.deltaX(startX, x), TouchGeometry.deltaY(startY, y));
    }

    public void end() {
        moveStart = null;
        resizeStart = null;
        interaction.finishGesture();
    }
}
