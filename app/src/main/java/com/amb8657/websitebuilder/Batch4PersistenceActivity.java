package com.amb8657.websitebuilder;

import android.graphics.Color;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Keeps Batch 4 metadata inside the canonical builder_v3 document envelope.
 * Batch 5 extends this proven layer without replacing the V4 editor engine.
 */
public class Batch4PersistenceActivity extends Batch5FeatureActivity {
    private static final String DATA = "builder_v3";
    private static final String META = "batch4Meta";

    @Override void load() {
        super.load();
        restoreBatch4Metadata();
    }

    @Override void save() {
        super.save();
        persistBatch4Metadata();
    }

    private SharedPreferences prefs() { return getSharedPreferences("v4_editor_controls", 0); }
    private String key(Block b, String suffix) { return "b:" + b.id + ":" + suffix; }

    private void persistBatch4Metadata() {
        try {
            SharedPreferences doc = getSharedPreferences(DATA, 0);
            String raw = doc.getString("data", "");
            if (raw.isEmpty()) return;
            JSONObject all = new JSONObject(raw);
            JSONArray ps = all.optJSONArray("projects");
            if (ps == null) return;
            JSONObject meta = new JSONObject();
            SharedPreferences cp = prefs();
            for (int pi = 0; pi < ps.length(); pi++) {
                JSONObject po = ps.getJSONObject(pi);
                JSONArray pages = po.optJSONArray("pages");
                if (pages == null) continue;
                for (int qi = 0; qi < pages.length(); qi++) {
                    JSONArray blocks = pages.getJSONObject(qi).optJSONArray("blocks");
                    if (blocks == null) continue;
                    for (int bi = 0; bi < blocks.length(); bi++) {
                        JSONObject bo = blocks.getJSONObject(bi);
                        int id = bo.optInt("id", 0);
                        if (id <= 0) continue;
                        JSONObject m = new JSONObject();
                        m.put("locked", cp.getBoolean("b:" + id + ":locked", false));
                        m.put("hidden", cp.getBoolean("b:" + id + ":hidden", false));
                        String defaultName = bo.optString("type", "Element") + " " + id;
                        m.put("name", cp.getString("b:" + id + ":name", defaultName));
                        meta.put(String.valueOf(id), m);
                    }
                }
            }
            all.put(META, meta);
            doc.edit().putString("data", all.toString()).apply();
        } catch (Exception ignored) { }
    }

    private void restoreBatch4Metadata() {
        try {
            SharedPreferences doc = getSharedPreferences(DATA, 0);
            String raw = doc.getString("data", "");
            if (raw.isEmpty()) return;
            JSONObject meta = new JSONObject(raw).optJSONObject(META);
            if (meta == null) return;
            SharedPreferences.Editor e = prefs().edit();
            if (project != null) {
                for (Page p : project.pages) for (Block b : p.blocks) {
                    JSONObject m = meta.optJSONObject(String.valueOf(b.id));
                    if (m == null) continue;
                    e.putBoolean(key(b, "locked"), m.optBoolean("locked", false));
                    e.putBoolean(key(b, "hidden"), m.optBoolean("hidden", false));
                    e.putString(key(b, "name"), m.optString("name", b.type + " " + b.id));
                }
            }
            e.apply();
        } catch (Exception ignored) { }
    }
}
