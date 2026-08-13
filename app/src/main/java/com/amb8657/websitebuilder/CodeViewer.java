package com.amb8657.websitebuilder;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;

/** Small reusable HTML viewer used by the editor's Code action. */
public final class CodeViewer {
    private CodeViewer() {}
    public static void show(Activity activity, String html) {
        LinearLayout box = new LinearLayout(activity);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(18, 10, 18, 8);
        TextView title = new TextView(activity);
        title.setText("Generated HTML for the current draft");
        title.setTextColor(Color.WHITE);
        title.setTextSize(14);
        box.addView(title, new LinearLayout.LayoutParams(-1, 48));
        EditText code = new EditText(activity);
        code.setText(html);
        code.setTextColor(Color.WHITE);
        code.setTextSize(11);
        code.setGravity(Gravity.TOP);
        code.setBackgroundColor(Color.rgb(23,22,27));
        code.setPadding(12, 10, 12, 10);
        box.addView(code, new LinearLayout.LayoutParams(-1, 0, 1));
        LinearLayout row = new LinearLayout(activity);
        Button copy = new Button(activity); copy.setText("Copy HTML");
        Button close = new Button(activity); close.setText("Close");
        row.addView(copy, new LinearLayout.LayoutParams(0, 48, 1));
        row.addView(close, new LinearLayout.LayoutParams(0, 48, 1));
        box.addView(row);
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(box);
        copy.setOnClickListener(v -> {
            ((android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE))
                    .setPrimaryClip(ClipData.newPlainText("HTML", code.getText()));
            Toast.makeText(activity, "HTML copied", Toast.LENGTH_SHORT).show();
        });
        close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        Window w = dialog.getWindow();
        if (w != null) w.setLayout(-1, (int)(activity.getResources().getDisplayMetrics().heightPixels * .8));
    }
}
