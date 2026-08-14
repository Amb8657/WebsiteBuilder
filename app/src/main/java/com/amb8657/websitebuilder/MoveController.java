package com.amb8657.websitebuilder;

/** Converts drag gestures into bounded element movement. */
public final class MoveController {
    public static final class Position {
        public int x;
        public int y;
        public Position(int x, int y) { this.x = x; this.y = y; }
    }

    public Position move(Position original, float dx, float dy) {
        if (original == null) return null;
        return new Position(
            Math.max(0, original.x + Math.round(dx)),
            Math.max(0, original.y + Math.round(dy))
        );
    }
}
