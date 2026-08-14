package com.amb8657.websitebuilder;

/** Immutable-ish helpers for translating raw touch coordinates into editor deltas. */
public final class TouchGeometry {
    private TouchGeometry() {}

    public static float deltaX(float startX, float currentX) {
        return currentX - startX;
    }

    public static float deltaY(float startY, float currentY) {
        return currentY - startY;
    }

    public static boolean isResizeHandleHit(float x, float y, float left, float top,
                                            float right, float bottom, float hitRadius) {
        return Math.abs(x - left) <= hitRadius || Math.abs(x - right) <= hitRadius ||
               Math.abs(y - top) <= hitRadius || Math.abs(y - bottom) <= hitRadius;
    }
}
