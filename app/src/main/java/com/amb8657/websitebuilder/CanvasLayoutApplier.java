package com.amb8657.websitebuilder;

/** Applies calculated geometry to a generic canvas element abstraction. */
public final class CanvasLayoutApplier {
    public interface Element {
        void setPosition(int x, int y);
        void setSize(int width, int height);
    }

    public void applyMove(Element element, MoveController.Position position) {
        if (element == null || position == null) return;
        element.setPosition(position.x, position.y);
    }

    public void applyResize(Element element, ResizeController.Geometry geometry) {
        if (element == null || geometry == null) return;
        element.setPosition(geometry.x, geometry.y);
        element.setSize(geometry.width, geometry.height);
    }
}
