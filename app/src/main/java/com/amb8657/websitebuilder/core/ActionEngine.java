package com.amb8657.websitebuilder.core;

/** Central action contract. UI screens only choose an action; execution stays here. */
public final class ActionEngine {
    public static final String NONE = "None";
    public static final String OPEN_PAGE = "Open Page";
    public static final String OPEN_WEBSITE = "Open Website";
    public static final String EMAIL = "Email";
    public static final String PHONE = "Phone";
    public static final String SCROLL_TO_SECTION = "Scroll to Section";
    public static final String ANIMATION = "Animation";

    private ActionEngine() {}

    public static boolean needsTarget(String action) {
        return !NONE.equals(action);
    }
}
