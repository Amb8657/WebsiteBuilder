package com.amb8657.websitebuilder;

import java.util.UUID;

/** Serializable-friendly model for an element on a page. */
public final class EditorElementModel {
    public enum Type { HEADING, TEXT, IMAGE, BUTTON, SPACER, SECTION }
    public final String id = UUID.randomUUID().toString();
    public Type type;
    public String text = "";
    public int x, y, width = 320, height = 120;
    public float opacity = 1f;
    public int color = 0xFFFFFFFF;
    public int cornerRadius = 0;

    public EditorElementModel(Type type) { this.type = type; }
}
