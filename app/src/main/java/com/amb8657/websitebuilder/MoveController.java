package com.amb8657.websitebuilder;

/** Converts drag gestures into bounded element movement. */
public final class MoveController {
    public static final class Position {
        public int x;
        public int y;
        public Position(int x, int y) { this.x = x; this.y = y; }
    }

    public Position move(Position original, float dx, float dy) {
        return move(original, dx, dy, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
    }

    /** Moves an element while keeping its top-left corner inside the supplied canvas bounds. */
    public Position move(Position original, float dx, float dy, int canvasWidth, int canvasHeight,
                         int elementWidth, int elementHeight) {
        if (original == null) return null;
        int minX = 0;
        int minY = 0;
        int maxX = Math.max(minX, canvasWidth - Math.max(24, elementWidth));
        int maxY = Math.max(minY, canvasHeight - Math.max(24, elementHeight));
        return new Position(
            clamp(original.x + Math.round(dx), minX, maxX),
            clamp(original.y + Math.round(dy), minY, maxY)
        );
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }
}
