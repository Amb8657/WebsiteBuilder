package com.amb8657.websitebuilder;

import java.util.List;

/** Resolves page destinations without coupling the editor screen to storage. */
public final class PageNavigator {
    private PageNavigator() {}

    public static int findPage(List<String> pageNames, String destination) {
        if (pageNames == null || destination == null) return -1;
        for (int i = 0; i < pageNames.size(); i++) {
            if (destination.equals(pageNames.get(i))) return i;
        }
        return -1;
    }
}
