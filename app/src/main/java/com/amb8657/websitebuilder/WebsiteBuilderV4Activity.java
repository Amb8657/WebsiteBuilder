package com.amb8657.websitebuilder;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Canonical V4 entry point backed by the complete Canva-style BuilderV3 engine. */
public class WebsiteBuilderV4Activity extends BuilderV3Activity {
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private GradientDrawable rounded(int color, int radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    @Override
    void splash() {
        base();
        root.setGravity(Gravity.CENTER);
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(28), dp(28), dp(28), dp(28));

        ImageView logo = new ImageView(this);
        logo.setImageResource(com.amb8657.websitebuilder.R.drawable.gmail);
        logo.setBackground(rounded(Color.WHITE, 28));
        logo.setPadding(dp(18), dp(18), dp(18), dp(18));
        card.addView(logo, new LinearLayout.LayoutParams(dp(112), dp(112)));

        TextView title = tv("Website Builder", 25, Color.WHITE);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD);
        card.addView(title, new LinearLayout.LayoutParams(-1, dp(62)));

        TextView subtitle = tv("Create  •  Design  •  Publish", 14, CanvaDesignSystem.MUTED);
        subtitle.setGravity(Gravity.CENTER);
        card.addView(subtitle, new LinearLayout.LayoutParams(-1, dp(36)));

        root.addView(card, new LinearLayout.LayoutParams(-1, -2));
        new Handler().postDelayed(this::dashboard, 1800);
    }
}
