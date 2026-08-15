package com.amb8657.websitebuilder;

/** Immutable-ish helpers for translating raw touch coordinates into bounded editor deltas. */
public final class TouchGeometry {
    private TouchGeometry() {}

    public static float deltaX(float startX, float currentX) { return currentX - startX; }
    public static float deltaY(float startY, float currentY) { return currentY - startY; }

    public static int clampX(int x, int canvasWidth, int elementWidth) {
        int safeWidth = Math.max(24, elementWidth);
        return clamp(x, 0, Math.max(0, canvasWidth - safeWidth));
    }

    public static int clampY(int y, int canvasHeight, int elementHeight) {
        int safeHeight = Math.max(24, elementHeight);
        return clamp(y, 0, Math.max(0, canvasHeight - safeHeight));
    }

    public static boolean isResizeHandleHit(float x, float y, float left, float top,
                                            float right, float bottom, float hitRadius) {
        if (hitRadius < 0) return false;
        return Math.abs(x - left) <= hitRadius || Math.abs(x - right) <= hitRadius ||
               Math.abs(y - top) <= hitRadius || Math.abs(y - bottom) <= hitRadius;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }
}
