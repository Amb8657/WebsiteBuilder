package com.amb8657.websitebuilder;

/**
 * MAHA Builder V2 visual/product structure.
 * Design option #2 is the source-of-truth for navigation and editor layout.
 */
public final class BuilderV2DesignSpec {
    private BuilderV2DesignSpec() {}

    public static final String DESIGN_VARIANT = "OPTION_2";

    public static final String[] APP_FLOW = {
        "Dashboard",
        "Create Project",
        "Project Setup",
        "Project Overview",
        "Visual Editor",
        "Pages / Properties / Actions",
        "Preview",
        "Publish"
    };

    public static final String[] EDITOR_PRINCIPLES = {
        "Canvas-first editing",
        "Stretchable page height",
        "Drag to move",
        "Corner and edge handles to resize",
        "Selection overlay only while selected",
        "Visual controls instead of code for common editing",
        "Context-aware actions",
        "Independent page editing"
    };
}
