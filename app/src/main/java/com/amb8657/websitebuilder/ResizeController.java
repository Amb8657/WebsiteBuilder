package com.amb8657.websitebuilder;

/** Converts handle movement into bounded element geometry changes. */
public final class ResizeController {
    public static final int MIN_DIMENSION_DP = 24;
    public static final class Geometry {
        public int x, y, width, height;
        public Geometry(int x, int y, int width, int height) {
            this.x = x; this.y = y;
            this.width = Math.max(MIN_DIMENSION_DP, width);
            this.height = Math.max(MIN_DIMENSION_DP, height);
        }
    }

    public Geometry resize(Geometry original, ResizeHandle handle, float dx, float dy) {
        if (original == null || handle == null) return original;
        Geometry g = new Geometry(original.x, original.y, original.width, original.height);
        boolean left = handle == ResizeHandle.LEFT || handle == ResizeHandle.TOP_LEFT || handle == ResizeHandle.BOTTOM_LEFT;
        boolean right = handle == ResizeHandle.RIGHT || handle == ResizeHandle.TOP_RIGHT || handle == ResizeHandle.BOTTOM_RIGHT;
        boolean top = handle == ResizeHandle.TOP || handle == ResizeHandle.TOP_LEFT || handle == ResizeHandle.TOP_RIGHT;
        boolean bottom = handle == ResizeHandle.BOTTOM || handle == ResizeHandle.BOTTOM_LEFT || handle == ResizeHandle.BOTTOM_RIGHT;

        if (left) {
            int delta = Math.round(dx);
            int next = Math.max(MIN_DIMENSION_DP, g.width - delta);
            g.x += g.width - next;
            g.width = next;
        } else if (right) {
            g.width = Math.max(MIN_DIMENSION_DP, g.width + Math.round(dx));
        }
        if (top) {
            int delta = Math.round(dy);
            int next = Math.max(MIN_DIMENSION_DP, g.height - delta);
            g.y += g.height - next;
            g.height = next;
        } else if (bottom) {
            g.height = Math.max(MIN_DIMENSION_DP, g.height + Math.round(dy));
        }
        g.x = Math.max(0, g.x);
        g.y = Math.max(0, g.y);
        return g;
    }
}
