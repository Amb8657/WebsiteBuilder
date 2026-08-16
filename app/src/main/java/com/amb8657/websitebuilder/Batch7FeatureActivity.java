package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;

/** Batch 7: project-management and local backup controls, layered over the Batch 6 editor. */
public class Batch7FeatureActivity extends Batch6FeatureActivity {
    private static final String PREFS = "batch7_project";
    private SharedPreferences projectPrefs;

    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }

    @Override public void onCreate(android.os.Bundle state) {
        projectPrefs = getSharedPreferences(PREFS, 0);
        super.onCreate(state);
    }

    @Override void editor() {
        super.editor();
        android.view.ViewGroup toolbar = (android.view.ViewGroup) root.getChildAt(0);
        android.widget.Button manage = btn("Manage");
        manage.setTextColor(CanvaDesignSystem.TEXT);
        manage.setBackground(gd(CanvaDesignSystem.PANEL_2, 10));
        manage.setOnClickListener(v -> managementPanel());
        toolbar.addView(manage, new LinearLayout.LayoutParams(d(92), d(44)));
    }

    private String now() { return DateFormat.getDateTimeInstance().format(new Date()); }
    private String projectName() { return project == null ? "Untitled project" : project.name; }

    /** 1: Rename project with persistent local metadata. */
    private void renameProject() {
        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setText(projectName());
        new AlertDialog.Builder(this).setTitle("Rename Project").setView(input)
                .setNegativeButton("Cancel", null).setPositiveButton("Save", (d, w) -> {
                    String name = input.getText().toString().trim();
                    if (project != null && !name.isEmpty()) project.name = name;
                    projectPrefs.edit().putString("name", name.isEmpty() ? projectName() : name)
                            .putString("modified", now()).apply();
                    save();
                    Toast.makeText(this, "Project renamed", Toast.LENGTH_SHORT).show();
                }).show();
    }

    /** 2: Project description. */
    private void description() {
        EditText input = new EditText(this);
        input.setMinLines(4);
        input.setText(projectPrefs.getString("description", ""));
        new AlertDialog.Builder(this).setTitle("Project Description").setView(input)
                .setNegativeButton("Cancel", null).setPositiveButton("Save", (d, w) -> {
                    projectPrefs.edit().putString("description", input.getText().toString()).putString("modified", now()).apply();
                    save();
                }).show();
    }

    /** 3: Created timestamp. */
    private void createdInfo() {
        if (!projectPrefs.contains("created")) projectPrefs.edit().putString("created", now()).apply();
        Toast.makeText(this, "Created: " + projectPrefs.getString("created", "Unknown"), Toast.LENGTH_LONG).show();
    }

    /** 4: Last-modified timestamp. */
    private void modifiedInfo() {
        projectPrefs.edit().putString("modified", now()).apply();
        Toast.makeText(this, "Last modified: " + projectPrefs.getString("modified", "Unknown"), Toast.LENGTH_LONG).show();
    }

    /** 5: Local document size estimate. */
    private long documentSize() {
        String raw = getSharedPreferences("builder_v3", 0).getString("data", "");
        return raw.getBytes(StandardCharsets.UTF_8).length;
    }

    private void sizeInfo() {
        Toast.makeText(this, "Project data: " + documentSize() + " bytes", Toast.LENGTH_SHORT).show();
    }

    /** 6: Simple progress/status indicator derived from editor content. */
    private void progressInfo() {
        int pages = project == null ? 0 : project.pages.size();
        int blocks = page == null ? 0 : page.blocks.size();
        int score = Math.min(100, (pages > 0 ? 50 : 0) + (blocks > 0 ? 50 : 0));
        new AlertDialog.Builder(this).setTitle("Project Progress")
                .setMessage(score + "%\nPages: " + pages + "\nCurrent-page elements: " + blocks)
                .setPositiveButton("OK", null).show();
    }

    /** 7: Local privacy flag for project metadata. */
    private void privacyToggle() {
        boolean next = !projectPrefs.getBoolean("private", true);
        projectPrefs.edit().putBoolean("private", next).apply();
        Toast.makeText(this, "Project privacy: " + (next ? "Private" : "Standard"), Toast.LENGTH_SHORT).show();
    }

    /** 8: Local form/submission data placeholder with safe counters; no network is introduced. */
    private void submissionInfo() {
        int submissions = projectPrefs.getInt("submissions", 0);
        new AlertDialog.Builder(this).setTitle("Form & Submission Data")
                .setMessage("Stored local submissions: " + submissions + "\nNetwork connection: none")
                .setPositiveButton("OK", null).show();
    }

    /** 9: Export the canonical local document as a private app-internal backup. */
    private void backup() {
        try {
            String raw = getSharedPreferences("builder_v3", 0).getString("data", "");
            File out = new File(getFilesDir(), "websitebuilder-project-backup.json");
            try (FileOutputStream stream = new FileOutputStream(out)) {
                stream.write(raw.getBytes(StandardCharsets.UTF_8));
            }
            projectPrefs.edit().putString("backup", out.getAbsolutePath()).putString("modified", now()).apply();
            Toast.makeText(this, "Local backup saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Backup failed", Toast.LENGTH_SHORT).show();
        }
    }

    /** 10: Restore the last local backup without adding server/network dependencies. */
    private void restoreBackup() {
        try {
            File in = new File(projectPrefs.getString("backup", ""));
            if (!in.exists()) { Toast.makeText(this, "No local backup found", Toast.LENGTH_SHORT).show(); return; }
            byte[] bytes = new byte[(int) in.length()];
            try (FileInputStream stream = new FileInputStream(in)) { int read = stream.read(bytes); if (read < 0) throw new Exception("empty"); }
            getSharedPreferences("builder_v3", 0).edit().putString("data", new String(bytes, StandardCharsets.UTF_8)).apply();
            load();
            Toast.makeText(this, "Local backup restored", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Restore failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void managementPanel() {
        String[] items = {
                "Rename Project", "Project Description", "Created: " + projectPrefs.getString("created", "set on first use"),
                "Last Modified", "Project Size", "Progress / Status",
                "Privacy: " + (projectPrefs.getBoolean("private", true) ? "Private" : "Standard"),
                "Form & Submission Data", "Save Local Backup", "Restore Local Backup"
        };
        new AlertDialog.Builder(this).setTitle("Project Management").setItems(items, (d, which) -> {
            switch (which) {
                case 0: renameProject(); break;
                case 1: description(); break;
                case 2: createdInfo(); break;
                case 3: modifiedInfo(); break;
                case 4: sizeInfo(); break;
                case 5: progressInfo(); break;
                case 6: privacyToggle(); break;
                case 7: submissionInfo(); break;
                case 8: backup(); break;
                case 9: restoreBackup(); break;
                default: break;
            }
        }).show();
    }
}
