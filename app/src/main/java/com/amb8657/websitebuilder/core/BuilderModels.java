package com.amb8657.websitebuilder.core;

import java.util.ArrayList;
import java.util.List;

/** Shared data model. UI screens should depend on this model rather than each other. */
public final class BuilderModels {
    private BuilderModels() {}

    public static final class Project {
        public String name;
        public final List<Page> pages = new ArrayList<>();
        public Project(String name) { this.name = name; }
    }

    public static final class Page {
        public String name;
        public int backgroundColor;
        public final List<Element> elements = new ArrayList<>();
        public Page(String name, int backgroundColor) {
            this.name = name;
            this.backgroundColor = backgroundColor;
        }
    }

    public static final class Element {
        public String id;
        public String type;
        public String text = "";
        public String imageUri = "";
        public String actionType = "None";
        public String actionTarget = "";
        public int x = 24, y = 24, width = 300, height = 80;
        public int backgroundColor;
        public int textColor;
        public int fontSize = 18;
        public float opacity = 1f;
        public float cornerRadius = 0f;
        public Element(String id, String type) {
            this.id = id;
            this.type = type;
        }
    }
}
