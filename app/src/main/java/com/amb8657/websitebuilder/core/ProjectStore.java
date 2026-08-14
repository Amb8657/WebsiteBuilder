package com.amb8657.websitebuilder.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

/**
 * Single persistence boundary for projects. Screens should not manipulate
 * SharedPreferences directly; this keeps storage changes isolated from UI.
 */
public final class ProjectStore {
    private static final String PREFS = "builder_v2";
    private static final String PROJECTS = "projects";
    private final SharedPreferences prefs;

    public ProjectStore(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public List<String> listProjectNames() {
        List<String> result = new ArrayList<>();
        String raw = prefs.getString(PROJECTS, "");
        if (raw == null || raw.isEmpty()) return result;
        for (String item : raw.split("\\|")) {
            if (!item.isEmpty()) result.add(decode(item));
        }
        return result;
    }

    public void saveProjectNames(List<String> names) {
        StringBuilder out = new StringBuilder();
        for (String name : names) {
            if (out.length() > 0) out.append('|');
            out.append(encode(name));
        }
        prefs.edit().putString(PROJECTS, out.toString()).apply();
    }

    public void deleteProject(String name) {
        List<String> names = listProjectNames();
        names.remove(name);
        prefs.edit().clear().apply();
        saveProjectNames(names);
    }

    private static String encode(String value) {
        return Base64.encodeToString(value.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
    }

    private static String decode(String value) {
        try {
            return new String(Base64.decode(value, Base64.NO_WRAP | Base64.URL_SAFE));
        } catch (Exception ignored) {
            return value;
        }
    }
}
