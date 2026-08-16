package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/** Compact HSV picker used by the canonical V4 editor. */
public final class PhotoshopColorPickerDialog {
    public interface Done { void done(int color); }
    private PhotoshopColorPickerDialog() {}

    public static void show(Context context, int initialColor, Done done) {
        float[] hsv = new float[3];
        Color.colorToHSV(initialColor, hsv);
        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = (int)(16 * context.getResources().getDisplayMetrics().density + .5f);
        box.setPadding(pad, pad, pad, pad);

        ColorSquare square = new ColorSquare(context, hsv);
        box.addView(square, new LinearLayout.LayoutParams(-1, (int)(220 * context.getResources().getDisplayMetrics().density + .5f)));
        SeekBar hue = new SeekBar(context);
        hue.setMax(360);
        hue.setProgress(Math.round(hsv[0]));
        box.addView(hue, new LinearLayout.LayoutParams(-1, -2));
        TextView preview = new TextView(context);
        preview.setText("  Selected colour  ");
        preview.setGravity(17);
        box.addView(preview, new LinearLayout.LayoutParams(-1, (int)(44 * context.getResources().getDisplayMetrics().density + .5f)));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Colour")
                .setView(box)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Done", null)
                .create();

        Runnable refresh = () -> preview.setBackgroundColor(Color.HSVToColor(hsv));
        square.setOnChanged(() -> { refresh.run(); });
        hue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { hsv[0] = progress; square.invalidate(); refresh.run(); }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        dialog.setOnShowListener(d -> {
            Button ok = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            ok.setOnClickListener(v -> { done.done(Color.HSVToColor(hsv)); dialog.dismiss(); });
            refresh.run();
        });
        dialog.show();
    }

    private interface CallbackColor { void changed(); }

    private static final class ColorSquare extends View {
        final float[] hsv;
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float markerX, markerY;
        CallbackColor onChanged;
        ColorSquare(Context context, float[] hsv) { super(context); this.hsv = hsv; setLayerType(View.LAYER_TYPE_SOFTWARE, null); }
        void setOnChanged(CallbackColor callback) { onChanged = callback; }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int hueColor = Color.HSVToColor(new float[]{hsv[0], 1f, 1f});
            Shader horizontal = new LinearGradient(0, 0, getWidth(), 0, Color.WHITE, hueColor, Shader.TileMode.CLAMP);
            Shader vertical = new LinearGradient(0, 0, 0, getHeight(), 0x00FFFFFF, Color.BLACK, Shader.TileMode.CLAMP);
            paint.setShader(new ComposeShader(horizontal, vertical, PorterDuff.Mode.MULTIPLY));
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            paint.setShader(null);
            markerX = hsv[1] * getWidth();
            markerY = (1f - hsv[2]) * getHeight();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(markerX, markerY, 9, paint);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(2);
            canvas.drawCircle(markerX, markerY, 10, paint);
            paint.setStyle(Paint.Style.FILL);
        }

        @Override public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
                hsv[1] = clamp(event.getX() / Math.max(1f, getWidth()));
                hsv[2] = 1f - clamp(event.getY() / Math.max(1f, getHeight()));
                invalidate();
                if (onChanged != null) onChanged.changed();
                return true;
            }
            return true;
        }
        private float clamp(float v) { return Math.max(0f, Math.min(1f, v)); }
    }
}
