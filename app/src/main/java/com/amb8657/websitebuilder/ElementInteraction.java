package com.amb8657.websitebuilder;

/** Pure interaction rules used by the visual editor. */
public final class ElementInteraction {
    private ElementInteraction() {}

    public static int moveX(int original, float deltaDp) {
        return Math.max(0, original + Math.round(deltaDp));
    }

    public static int moveY(int original, float deltaDp) {
        return Math.max(0, original + Math.round(deltaDp));
    }

    public static int resizeWidth(int original, float deltaDp, boolean fromRight) {
        int next = fromRight ? original + Math.round(deltaDp) : original - Math.round(deltaDp);
        return Math.max(EditorState.MIN_ELEMENT_WIDTH_DP, next);
    }

    public static int resizeHeight(int original, float deltaDp, boolean fromBottom) {
        int next = fromBottom ? original + Math.round(deltaDp) : original - Math.round(deltaDp);
        return Math.max(EditorState.MIN_ELEMENT_HEIGHT_DP, next);
    }
}
