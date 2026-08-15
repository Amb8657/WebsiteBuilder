package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Canonical Website Builder editor.
 *
 * V4 owns the product UI while reusing the proven document/canvas engine in
 * BuilderV3Activity. This keeps the interaction model stable while replacing
 * the old dashboard/editor chrome with the agreed Canva-style workflow.
 */
public class WebsiteBuilderV4Activity extends BuilderV3Activity {
    private int dp(int v) { return (int) (v * getResources().getDisplayMetrics().density + .5f); }

    private TextView heading(String text, int size) {
        TextView v = tv(text, size, CanvaDesignSystem.TEXT);
        v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        v.setPadding(dp(4), 0, dp(4), 0);
        return v;
    }

    private Button pill(String text) {
        Button b = btn(text);
        b.setTextColor(CanvaDesignSystem.TEXT);
        b.setBackground(gd(CanvaDesignSystem.PANEL_2, 10));
        b.setPadding(dp(10), 0, dp(10), 0);
        return b;
    }

    @Override
    void splash() {
        base();
        root.setGravity(Gravity.CENTER);
        root.setBackgroundColor(Color.rgb(101, 0, 0));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);
        content.setPadding(dp(18), dp(18), dp(18), dp(18));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.gmail);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        content.addView(logo, new LinearLayout.LayoutParams(dp(230), dp(230)));

        TextView title = heading("Website Builder", 26);
        title.setGravity(Gravity.CENTER);
        content.addView(title, new LinearLayout.LayoutParams(-1, dp(52)));

        TextView subtitle = tv("Create  •  Design  •  Publish", 14, CanvaDesignSystem.MUTED);
        subtitle.setGravity(Gravity.CENTER);
        content.addView(subtitle, new LinearLayout.LayoutParams(-1, dp(34)));

        root.addView(content, new LinearLayout.LayoutParams(-1, -2));
        // Deliberately long enough for the supplied branding to be seen.
        new Handler().postDelayed(this::dashboard, 2200);
    }

    @Override
    void dashboard() {
        base();
        root.setBackgroundColor(CanvaDesignSystem.BG);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(18), dp(16), dp(18), dp(8));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.gmail);
        logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        top.addView(logo, new LinearLayout.LayoutParams(dp(44), dp(44)));

        LinearLayout brand = new LinearLayout(this);
        brand.setOrientation(LinearLayout.VERTICAL);
        brand.setPadding(dp(10), 0, 0, 0);
        brand.addView(heading("Website Builder", 20), new LinearLayout.LayoutParams(-1, dp(28)));
        brand.addView(tv("Design websites on your phone", 12, CanvaDesignSystem.MUTED), new LinearLayout.LayoutParams(-1, dp(22)));
        top.addView(brand, new LinearLayout.LayoutParams(0, dp(50), 1));
        root.addView(top, new LinearLayout.LayoutParams(-1, dp(68)));

        ScrollView scroll = new ScrollView(this);
        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(18), dp(6), dp(18), dp(28));
        scroll.addView(body);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(dp(18), dp(18), dp(18), dp(18));
        hero.setBackground(gd(CanvaDesignSystem.PURPLE, 16));
        hero.addView(heading("Build something beautiful", 23), new LinearLayout.LayoutParams(-1, dp(36)));
        hero.addView(tv("Start from a blank page, then add text, images, buttons, sections and shapes.", 13, 0xFFE5E1F2), new LinearLayout.LayoutParams(-1, dp(58)));
        Button create = btn("+  Create a website");
        create.setTextColor(CanvaDesignSystem.PURPLE);
        create.setBackground(gd(Color.WHITE, 12));
        hero.addView(create, new LinearLayout.LayoutParams(-1, dp(50)));
        create.setOnClickListener(v -> newProject());
        body.addView(hero, new LinearLayout.LayoutParams(-1, dp(178)));

        body.addView(heading("Your websites", 19), new LinearLayout.LayoutParams(-1, dp(52)));

        if (projects.isEmpty()) {
            LinearLayout empty = new LinearLayout(this);
            empty.setOrientation(LinearLayout.VERTICAL);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(dp(22), dp(26), dp(22), dp(26));
            empty.setBackground(gd(CanvaDesignSystem.PANEL, 14));
            TextView icon = heading("＋", 36);
            icon.setGravity(Gravity.CENTER);
            empty.addView(icon, new LinearLayout.LayoutParams(-1, dp(48)));
            empty.addView(heading("No websites yet", 17), new LinearLayout.LayoutParams(-1, dp(32)));
            TextView hint = tv("Create your first site and edit it visually.", 13, CanvaDesignSystem.MUTED);
            hint.setGravity(Gravity.CENTER);
            empty.addView(hint, new LinearLayout.LayoutParams(-1, dp(38)));
            Button c = pill("Create first website");
            c.setBackground(gd(CanvaDesignSystem.ACCENT, 10));
            empty.addView(c, new LinearLayout.LayoutParams(-1, dp(48)));
            c.setOnClickListener(v -> newProject());
            body.addView(empty, new LinearLayout.LayoutParams(-1, dp(202)));
        } else {
            for (Project p : projects) {
                LinearLayout card = new LinearLayout(this);
                card.setGravity(Gravity.CENTER_VERTICAL);
                card.setPadding(dp(14), dp(8), dp(8), dp(8));
                card.setBackground(gd(CanvaDesignSystem.PANEL, 14));

                LinearLayout info = new LinearLayout(this);
                info.setOrientation(LinearLayout.VERTICAL);
                info.addView(heading(p.name, 16), new LinearLayout.LayoutParams(-1, dp(28)));
                int count = p.pages.size();
                info.addView(tv(count + (count == 1 ? " page" : " pages"), 12, CanvaDesignSystem.MUTED), new LinearLayout.LayoutParams(-1, dp(24)));
                card.addView(info, new LinearLayout.LayoutParams(0, dp(60), 1));

                Button open = pill("Open");
                open.setBackground(gd(CanvaDesignSystem.ACCENT, 10));
                card.addView(open, new LinearLayout.LayoutParams(dp(78), dp(44)));
                open.setOnClickListener(v -> openProject(p));
                card.setOnClickListener(v -> openProject(p));
                body.addView(card, new LinearLayout.LayoutParams(-1, dp(78)));
                ((LinearLayout.LayoutParams) card.getLayoutParams()).bottomMargin = dp(10);
            }
        }

        body.addView(heading("Quick add", 19), new LinearLayout.LayoutParams(-1, dp(52)));
        LinearLayout quick = new LinearLayout(this);
        quick.setGravity(Gravity.CENTER);
        String[] quickItems = {"Text", "Image", "Button", "Section", "Shape", "Tool"};
        for (String item : quickItems) {
            Button q = pill(item);
            quick.addView(q, new LinearLayout.LayoutParams(0, dp(44), 1));
            q.setOnClickListener(v -> {
                if (projects.isEmpty()) {
                    newProject();
                    Toast.makeText(this, "Create a website first", Toast.LENGTH_SHORT).show();
                } else {
                    openProject(projects.get(0));
                    add(item);
                }
            });
        }
        body.addView(quick, new LinearLayout.LayoutParams(-1, dp(50)));
    }

    @Override
    void editor() {
        base();
        root.setBackgroundColor(CanvaDesignSystem.BG);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(8), dp(8), dp(8), dp(8));
        toolbar.setBackgroundColor(CanvaDesignSystem.PANEL);

        Button back = pill("‹");
        toolbar.addView(back, new LinearLayout.LayoutParams(dp(44), dp(44)));
        back.setOnClickListener(v -> { save(); dashboard(); });

        LinearLayout titleBox = new LinearLayout(this);
        titleBox.setOrientation(LinearLayout.VERTICAL);
        titleBox.setPadding(dp(10), 0, 0, 0);
        titleBox.addView(heading(project.name, 16), new LinearLayout.LayoutParams(-1, dp(25)));
        titleBox.addView(tv(page.name, 11, CanvaDesignSystem.MUTED), new LinearLayout.LayoutParams(-1, dp(19)));
        toolbar.addView(titleBox, new LinearLayout.LayoutParams(0, dp(44), 1));

        Button undo = pill("↶");
        Button redo = pill("↷");
        toolbar.addView(undo, new LinearLayout.LayoutParams(dp(44), dp(44)));
        toolbar.addView(redo, new LinearLayout.LayoutParams(dp(44), dp(44)));
        undo.setOnClickListener(v -> Toast.makeText(this, "Undo is available through editor history", Toast.LENGTH_SHORT).show());
        redo.setOnClickListener(v -> Toast.makeText(this, "Redo is available through editor history", Toast.LENGTH_SHORT).show());

        Button preview = pill("Preview");
        preview.setBackground(gd(CanvaDesignSystem.ACCENT, 10));
        toolbar.addView(preview, new LinearLayout.LayoutParams(dp(82), dp(44)));
        preview.setOnClickListener(v -> preview());
        root.addView(toolbar, new LinearLayout.LayoutParams(-1, dp(60)));

        HorizontalScrollView pageStrip = new HorizontalScrollView(this);
        pages = new LinearLayout(this);
        pages.setGravity(Gravity.CENTER_VERTICAL);
        pages.setPadding(dp(8), dp(5), dp(8), dp(5));
        pageStrip.addView(pages);
        root.addView(pageStrip, new LinearLayout.LayoutParams(-1, dp(52)));

        LinearLayout addBar = new LinearLayout(this);
        addBar.setGravity(Gravity.CENTER_VERTICAL);
        addBar.setPadding(dp(8), dp(5), dp(8), dp(5));
        addBar.setBackgroundColor(CanvaDesignSystem.PANEL);
        for (String item : new String[]{"Text", "Image", "Button", "Section", "Shape", "Tool"}) {
            Button b = pill("+ " + item);
            addBar.addView(b, new LinearLayout.LayoutParams(dp(92), dp(42)));
            b.setOnClickListener(v -> add(item));
        }
        ScrollView toolScroll = new ScrollView(this);
        toolScroll.setHorizontalScrollBarEnabled(false);
        HorizontalScrollView hs = new HorizontalScrollView(this);
        hs.addView(addBar);
        root.addView(hs, new LinearLayout.LayoutParams(-1, dp(52)));

        canvasScroll = new ScrollView(this);
        canvas = new android.widget.FrameLayout(this);
        canvasScroll.addView(canvas, new ScrollView.LayoutParams(-1, dp(760)));
        root.addView(canvasScroll, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout bottom = new LinearLayout(this);
        bottom.setPadding(dp(6), dp(5), dp(6), dp(5));
        bottom.setBackgroundColor(CanvaDesignSystem.PANEL);
        Button pagesButton = pill("Pages");
        Button styleButton = pill("Style");
        Button previewButton = pill("Preview");
        Button saveButton = pill("Save");
        pagesButton.setOnClickListener(v -> pageManager());
        styleButton.setOnClickListener(v -> pageStyle());
        previewButton.setOnClickListener(v -> preview());
        saveButton.setOnClickListener(v -> { save(); Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show(); });
        bottom.addView(pagesButton, new LinearLayout.LayoutParams(0, dp(50), 1));
        bottom.addView(styleButton, new LinearLayout.LayoutParams(0, dp(50), 1));
        bottom.addView(previewButton, new LinearLayout.LayoutParams(0, dp(50), 1));
        bottom.addView(saveButton, new LinearLayout.LayoutParams(0, dp(50), 1));
        root.addView(bottom, new LinearLayout.LayoutParams(-1, dp(62)));

        render();
    }
}
