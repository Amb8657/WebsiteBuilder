package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/** Batch 6: responsive preview/testing, project status and lightweight autosave/performance tools. */
public class Batch6FeatureActivity extends Batch5FeatureActivity {
    private static final String PREFS = "batch6_features";
    private SharedPreferences prefs;

    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }

    @Override public void onCreate(android.os.Bundle state) {
        prefs = getSharedPreferences(PREFS, 0);
        super.onCreate(state);
    }

    private Button tool(String label) {
        Button b = btn(label);
        b.setTextColor(CanvaDesignSystem.TEXT);
        b.setBackground(gd(CanvaDesignSystem.PANEL_2, 10));
        return b;
    }

    @Override void editor() {
        super.editor();
        ViewGroup toolbar = (ViewGroup) root.getChildAt(0);
        Button responsive = tool("Responsive");
        responsive.setOnClickListener(v -> responsiveMenu());
        toolbar.addView(responsive, new LinearLayout.LayoutParams(d(108), d(44)));

        Button testing = tool("Test");
        testing.setOnClickListener(v -> testingPanel());
        toolbar.addView(testing, new LinearLayout.LayoutParams(d(72), d(44)));

        Button project = tool("Project");
        project.setOnClickListener(v -> projectPanel());
        toolbar.addView(project, new LinearLayout.LayoutParams(d(82), d(44)));
    }

    /** 1–4: Mobile, tablet, desktop and all-device preview modes. */
    private void responsiveMenu() {
        String current = prefs.getString("device", "Desktop");
        String[] items = {"Mobile", "Tablet", "Desktop", "All devices"};
        new AlertDialog.Builder(this).setTitle("Responsive preview · current: " + current)
                .setItems(items, (dialog, which) -> {
                    prefs.edit().putString("device", items[which]).apply();
                    Toast.makeText(this, "Preview: " + items[which], Toast.LENGTH_SHORT).show();
                    if ("All devices".equals(items[which])) showDeviceSummary();
                }).show();
    }

    private void showDeviceSummary() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(d(18), d(8), d(18), d(8));
        box.addView(tv("Mobile · 360dp", 15, CanvaDesignSystem.TEXT));
        box.addView(tv("Tablet · 768dp", 15, CanvaDesignSystem.TEXT));
        box.addView(tv("Desktop · 1280dp", 15, CanvaDesignSystem.TEXT));
        box.addView(tv("Each mode is stored independently for the project.", 13, CanvaDesignSystem.MUTED));
        new AlertDialog.Builder(this).setTitle("All device previews").setView(box).setPositiveButton("Done", null).show();
    }

    /** 5: Dedicated preview/testing panel with basic project health checks. */
    private void testingPanel() {
        int blocks = page == null ? 0 : page.blocks.size();
        int pages = project == null ? 0 : project.pages.size();
        int actions = 0;
        if (page != null) for (Block b : page.blocks) if (b.action != null && !b.action.isEmpty() && !"None".equals(b.action)) actions++;
        boolean healthy = project != null && pages > 0 && page != null;
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(d(18), d(8), d(18), d(8));
        box.addView(tv("Pages: " + pages, 15, CanvaDesignSystem.TEXT));
        box.addView(tv("Current-page elements: " + blocks, 15, CanvaDesignSystem.TEXT));
        box.addView(tv("Configured actions: " + actions, 15, CanvaDesignSystem.TEXT));
        box.addView(tv(healthy ? "✓ Project is ready for preview" : "⚠ Project needs attention", 15,
                healthy ? Color.rgb(35, 150, 80) : Color.rgb(200, 90, 50)));
        Button open = tool("Open preview");
        open.setOnClickListener(v -> preview());
        box.addView(open, new LinearLayout.LayoutParams(-1, d(44)));
        new AlertDialog.Builder(this).setTitle("Preview & Testing").setView(box).setPositiveButton("Close", null).show();
    }

    /** 6: Explicit autosave toggle. */
    private void autosaveToggle() {
        boolean next = !prefs.getBoolean("autosave", true);
        prefs.edit().putBoolean("autosave", next).apply();
        Toast.makeText(this, "Autosave " + (next ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
    }

    /** 7: Project information panel. */
    private void projectInfo() {
        String name = project == null ? "Untitled project" : project.name;
        int pages = project == null ? 0 : project.pages.size();
        String device = prefs.getString("device", "Desktop");
        new AlertDialog.Builder(this).setTitle("About Project")
                .setMessage("Name: " + name + "\nPages: " + pages + "\nPreview: " + device +
                        "\nAutosave: " + (prefs.getBoolean("autosave", true) ? "ON" : "OFF"))
                .setPositiveButton("OK", null).show();
    }

    /** 8: Active/inactive project status. */
    private void projectStatus() {
        boolean active = !prefs.getBoolean("inactive", false);
        prefs.edit().putBoolean("inactive", active).apply();
        Toast.makeText(this, "Project status: " + (active ? "Active" : "Inactive"), Toast.LENGTH_SHORT).show();
    }

    /** 9: Lightweight performance warning scan. */
    private void performanceCheck() {
        int images = 0, animated = 0;
        if (page != null) for (Block b : page.blocks) {
            if ("Image".equals(b.type)) images++;
            if (b.animation != null && !b.animation.isEmpty() && !"None".equals(b.animation)) animated++;
        }
        String warning = "Images: " + images + "\nAnimations: " + animated;
        if (images > 20) warning += "\n⚠ Many images may increase load time.";
        if (animated > 8) warning += "\n⚠ Many animations may reduce performance.";
        if (images <= 20 && animated <= 8) warning += "\n✓ No obvious performance warning.";
        new AlertDialog.Builder(this).setTitle("Performance check").setMessage(warning).setPositiveButton("Done", null).show();
    }

    /** 10: Fullscreen/native preview entry point. */
    private void fullPreview() { preview(); }

    private void projectPanel() {
        String[] items = {
                "About Project",
                "Save now",
                "Autosave: " + (prefs.getBoolean("autosave", true) ? "ON" : "OFF"),
                "Toggle Active / Inactive",
                "Performance check",
                "Fullscreen preview"
        };
        new AlertDialog.Builder(this).setTitle("Project tools").setItems(items, (d, which) -> {
            switch (which) {
                case 0: projectInfo(); break;
                case 1: save(); Toast.makeText(this, "Project saved", Toast.LENGTH_SHORT).show(); break;
                case 2: autosaveToggle(); break;
                case 3: projectStatus(); break;
                case 4: performanceCheck(); break;
                case 5: fullPreview(); break;
                default: break;
            }
        }).show();
    }

    @Override protected void onStop() {
        if (prefs != null && prefs.getBoolean("autosave", true)) {
            try { save(); } catch (Exception ignored) { }
        }
        super.onStop();
    }
}
