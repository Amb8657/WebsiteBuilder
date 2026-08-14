package com.amb8657.websitebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/** Independent project container; pages remain isolated editing units. */
public final class EditorProjectModel {
    public final String id = UUID.randomUUID().toString();
    public String name;
    private final ArrayList<EditorPageModel> pages = new ArrayList<>();

    public EditorProjectModel(String name) { this.name = name; }
    public void addPage(EditorPageModel page) { if (page != null) pages.add(page); }
    public void removePage(String pageId) { pages.removeIf(p -> p.id.equals(pageId)); }
    public List<EditorPageModel> pages() { return Collections.unmodifiableList(pages); }
}
