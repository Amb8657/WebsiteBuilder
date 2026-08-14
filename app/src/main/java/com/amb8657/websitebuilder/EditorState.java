package com.amb8657.websitebuilder;

/** Shared editor state kept outside the screen Activity. */
public final class EditorState {
    private EditorState() {}

    public static final String SELECTION_OVERLAY = "selection_overlay";
    public static final String STRETCHABLE_CANVAS = "stretchable_canvas";
    public static final int MIN_ELEMENT_WIDTH_DP = 60;
    public static final int MIN_ELEMENT_HEIGHT_DP = 40;

    public static boolean isSupportedElement(String type) {
        return "Text".equals(type) || "Heading".equals(type) ||
               "Image".equals(type) || "Button".equals(type) ||
               "Section".equals(type) || "Spacer".equals(type) ||
               "Shape".equals(type) || "Tool".equals(type);
    }
}
