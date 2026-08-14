package com.amb8657.websitebuilder;

/** Context-aware action configuration for buttons and interactive elements. */
public final class ActionConfig {
    public enum Type { NONE, OPEN_PAGE, OPEN_WEBSITE, EMAIL, PHONE, SCROLL_TO_SECTION, SHOW_HIDE, ANIMATION }

    public Type type = Type.NONE;
    public String target = "";
    public String secondary = "";

    public ActionConfig() {}
    public ActionConfig(Type type) { this.type = type; }

    public boolean needsDestination() {
        return type != Type.NONE && type != Type.ANIMATION;
    }

    public boolean isPageNavigation() { return type == Type.OPEN_PAGE; }
}
