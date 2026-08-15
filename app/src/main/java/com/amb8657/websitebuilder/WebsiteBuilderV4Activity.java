package com.amb8657.websitebuilder;

import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Canonical V4 entry point backed by the complete Canva-style BuilderV3 engine. */
public class WebsiteBuilderV4Activity extends BuilderV3Activity {
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }

    @Override
    void splash() {
        base();
        root.setGravity(Gravity.CENTER);
        root.setBackgroundColor(Color.rgb(101, 0, 0));

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(20), dp(20), dp(20), dp(20));

        ImageView logo = new ImageView(this);
        logo.setImageResource(com.amb8657.websitebuilder.R.drawable.gmail);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        card.addView(logo, new LinearLayout.LayoutParams(dp(220), dp(220)));

        TextView title = tv("Website Builder", 25, Color.WHITE);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD);
        card.addView(title, new LinearLayout.LayoutParams(-1, dp(58)));

        TextView subtitle = tv("Create  •  Design  •  Publish", 14, CanvaDesignSystem.MUTED);
        subtitle.setGravity(Gravity.CENTER);
        card.addView(subtitle, new LinearLayout.LayoutParams(-1, dp(34)));

        root.addView(card, new LinearLayout.LayoutParams(-1, -2));
        // Keep the branded splash visible long enough to be seen, then enter the builder.
        new Handler().postDelayed(this::dashboard, 2200);
    }
}
