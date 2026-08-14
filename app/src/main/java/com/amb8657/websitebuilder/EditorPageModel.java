package com.amb8657.websitebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Independent page model used by the modular editor. */
public final class EditorPageModel {
    public final String id;
    public String name;
    public int width = 1080;
    public int height = 1600;
    private final ArrayList<EditorElementModel> elements = new ArrayList<>();

    public EditorPageModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void add(EditorElementModel element) { if (element != null) elements.add(element); }
    public void remove(String elementId) { elements.removeIf(e -> e.id.equals(elementId)); }
    public List<EditorElementModel> elements() { return Collections.unmodifiableList(elements); }
}
