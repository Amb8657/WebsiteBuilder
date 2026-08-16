package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/** Batch 8: canvas arrangement, snapping and zoom controls layered over the existing editor. */
public class Batch8FeatureActivity extends Batch7FeatureActivity {
    private static final String PREFS = "batch8_canvas";
    private android.content.SharedPreferences canvasPrefs;
    private float zoom = 1f;

    private int d(int v) { return (int) (v * getResources().getDisplayMetrics().density + .5f); }

    @Override public void onCreate(android.os.Bundle state) {
        canvasPrefs = getSharedPreferences(PREFS, 0);
        super.onCreate(state);
    }

    @Override void editor() {
        super.editor();
        android.widget.Button arrange = btn("Canvas");
        arrange.setTextColor(CanvaDesignSystem.TEXT);
        arrange.setBackground(gd(CanvaDesignSystem.PANEL_2, 10));
        arrange.setOnClickListener(v -> canvasMenu());
        android.view.ViewGroup toolbar = (android.view.ViewGroup) root.getChildAt(0);
        toolbar.addView(arrange, new LinearLayout.LayoutParams(d(82), d(44)));
    }

    private void message(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }

    private Block selectedBlock() { return selected; }

    private int canvasWidth() {
        int width = root == null ? 1080 : root.getWidth();
        if (width <= 0) width = d(1080);
        return Math.max(d(320), (int) (width / Math.max(.5f, zoom)));
    }

    private int canvasHeight() {
        int height = root == null ? 1920 : root.getHeight();
        if (height <= 0) height = d(1920);
        return Math.max(d(480), (int) (height / Math.max(.5f, zoom)));
    }

    /** 1: align selected element to the left canvas guide. */
    private void alignLeft() { if (!requireSelection()) return; selected.x = d(24); changed("Aligned left"); }

    /** 2: center selected element horizontally. */
    private void alignCenter() { if (!requireSelection()) return; selected.x = Math.max(d(24), (canvasWidth() - selected.w) / 2); changed("Centered horizontally"); }

    /** 3: align selected element to the right canvas guide. */
    private void alignRight() { if (!requireSelection()) return; selected.x = Math.max(d(24), canvasWidth() - selected.w - d(24)); changed("Aligned right"); }

    /** 4: align selected element to the top canvas guide. */
    private void alignTop() { if (!requireSelection()) return; selected.y = d(24); changed("Aligned top"); }

    /** 5: center selected element vertically. */
    private void alignMiddle() { if (!requireSelection()) return; selected.y = Math.max(d(24), (canvasHeight() - selected.h) / 2); changed("Centered vertically"); }

    /** 6: align selected element to the bottom canvas guide. */
    private void alignBottom() { if (!requireSelection()) return; selected.y = Math.max(d(24), canvasHeight() - selected.h - d(24)); changed("Aligned bottom"); }

    /** 7: snap selected element to an 8dp grid. */
    private void snapGrid() {
        if (!requireSelection()) return;
        int grid = d(8);
        selected.x = Math.max(0, Math.round(selected.x / (float) grid) * grid);
        selected.y = Math.max(0, Math.round(selected.y / (float) grid) * grid);
        canvasPrefs.edit().putBoolean("snap_enabled", true).apply();
        changed("Snapped to 8dp grid");
    }

    /** 8: bring selected top-level element to the front of its layer stack. */
    private void bringFront() {
        if (!requireSelection()) return;
        if (page != null && selected.parent == 0) { page.blocks.remove(selected); page.blocks.add(selected); changed("Moved to front"); }
    }

    /** 9: send selected top-level element to the back of its layer stack. */
    private void sendBack() {
        if (!requireSelection()) return;
        if (page != null && selected.parent == 0) { page.blocks.remove(selected); page.blocks.add(0, selected); changed("Moved to back"); }
    }

    /** 10: reset editor canvas zoom to 100%. */
    private void resetZoom() {
        zoom = 1f;
        if (root != null) { root.setScaleX(zoom); root.setScaleY(zoom); }
        canvasPrefs.edit().putFloat("zoom", zoom).apply();
        message("Canvas zoom: 100%");
    }

    private boolean requireSelection() {
        if (selected == null) { message("Select an element first"); return false; }
        return true;
    }

    private void changed(String text) {
        save();
        render();
        message(text);
    }

    private void zoomBy(float factor) {
        zoom = Math.max(.5f, Math.min(1.5f, zoom * factor));
        if (root != null) { root.setScaleX(zoom); root.setScaleY(zoom); }
        canvasPrefs.edit().putFloat("zoom", zoom).apply();
        message("Canvas zoom: " + Math.round(zoom * 100) + "%");
    }

    private void canvasMenu() {
        String[] actions = {
                "Align left", "Center horizontally", "Align right",
                "Align top", "Center vertically", "Align bottom",
                "Snap to 8dp grid", "Bring to front", "Send to back",
                "Zoom / Reset"
        };
        new AlertDialog.Builder(this).setTitle("Canvas & Arrange").setItems(actions, (dialog, which) -> {
            switch (which) {
                case 0: alignLeft(); break;
                case 1: alignCenter(); break;
                case 2: alignRight(); break;
                case 3: alignTop(); break;
                case 4: alignMiddle(); break;
                case 5: alignBottom(); break;
                case 6: snapGrid(); break;
                case 7: bringFront(); break;
                case 8: sendBack(); break;
                case 9: zoomMenu(); break;
                default: break;
            }
        }).show();
    }

    private void zoomMenu() {
        String[] actions = {"Zoom in 10%", "Zoom out 10%", "Reset to 100%"};
        new AlertDialog.Builder(this).setTitle("Canvas zoom · " + Math.round(zoom * 100) + "%")
                .setItems(actions, (dialog, which) -> {
                    if (which == 0) zoomBy(1.10f);
                    else if (which == 1) zoomBy(.90f);
                    else resetZoom();
                }).show();
    }
}
