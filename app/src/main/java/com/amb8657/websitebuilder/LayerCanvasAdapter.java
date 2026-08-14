package com.amb8657.websitebuilder;

import java.util.ArrayList;
import java.util.List;

/** Bridges logical layer order to the render order used by the visual canvas. */
public final class LayerCanvasAdapter {
    private LayerCanvasAdapter() {}

    public static <T> List<T> ordered(List<T> elements, List<String> ids, IdProvider<T> provider) {
        ArrayList<T> result = new ArrayList<>();
        if (elements == null) return result;
        for (String id : ids) {
            for (T element : elements) {
                if (id.equals(provider.idOf(element))) {
                    result.add(element);
                    break;
                }
            }
        }
        for (T element : elements) {
            if (!result.contains(element)) result.add(element);
        }
        return result;
    }

    public interface IdProvider<T> { String idOf(T value); }
}
