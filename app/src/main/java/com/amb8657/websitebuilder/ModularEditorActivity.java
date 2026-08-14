package com.amb8657.websitebuilder;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Modular editor shell: toolbar, extendable page canvas, palette and page controls. */
public class ModularEditorActivity extends Activity {
    private EditorProjectModel project;
    private EditorPageModel page;
    private TextView pageName;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        project = new EditorProjectModel("Untitled Project");
        page = new EditorPageModel("page-1", "Page 1");
        project.addPage(page);
        buildUi();
    }

    private TextView label(String text, int size) {
        TextView v = new TextView(this);
        v.setText(text); v.setTextColor(Color.WHITE); v.setTextSize(size);
        v.setGravity(Gravity.CENTER_VERTICAL); v.setPadding(18, 10, 18, 10);
        return v;
    }

    private Button action(String text) { Button b = new Button(this); b.setText(text); return b; }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(Color.rgb(18,18,22));

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        pageName = label(page.name, 18);
        toolbar.addView(pageName, new LinearLayout.LayoutParams(0, 64, 1));
        toolbar.addView(action("Undo")); toolbar.addView(action("Redo"));
        toolbar.addView(action("Preview")); toolbar.addView(action("Save"));
        root.addView(toolbar);

        ScrollView scroll = new ScrollView(this);
        ModularEditorCanvasView canvas = new ModularEditorCanvasView(this);
        scroll.addView(canvas, new ScrollView.LayoutParams(-1, 1600));
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        HorizontalScrollView paletteScroll = new HorizontalScrollView(this);
        LinearLayout palette = new LinearLayout(this);
        palette.setPadding(8, 4, 8, 4);
        String[] tools = {"Heading", "Text", "Image", "Button", "Section", "Spacer", "Pages", "Layers"};
        for (String tool : tools) {
            Button b = action(tool);
            palette.addView(b, new LinearLayout.LayoutParams(-2, 58));
            if (tool.equals("Pages")) b.setOnClickListener(v -> addPage());
            if (tool.equals("Heading")) b.setOnClickListener(v -> addHeading());
        }
        paletteScroll.addView(palette);
        root.addView(paletteScroll, new LinearLayout.LayoutParams(-1, 70));
        setContentView(root);
    }

    private void addHeading() {
        EditorElementModel e = new EditorElementModel(EditorElementModel.Type.HEADING);
        e.text = "New Heading"; e.x = 80; e.y = 420; page.add(e);
    }

    private void addPage() {
        int n = project.pages().size() + 1;
        page = new EditorPageModel("page-" + n, "Page " + n);
        project.addPage(page); pageName.setText(page.name);
    }
}
