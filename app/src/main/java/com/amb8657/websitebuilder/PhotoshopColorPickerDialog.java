package com.amb8657.websitebuilder;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/** Compact Photoshop-style HSV picker used by the canonical V4 editor. */
public final class PhotoshopColorPickerDialog {
    public interface Callback { void onColor(int color); }

    private PhotoshopColorPickerDialog() {}

    public static void show(Context context, int initialColor, Callback callback) {
        final float[] hsv = new float[3];
        Color.colorToHSV(initialColor, hsv);
        final Dialog dialog = new Dialog(context);
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 18, 24, 14);

        TextView title = new TextView(context);
        title.setText("Choose a colour");
        title.setTextSize(20);
        title.setTextColor(Color.DKGRAY);
        root.addView(title, new LinearLayout.LayoutParams(-1, 48));

        ColorSquare square = new ColorSquare(context, hsv);
        square.setContentDescription("Saturation and brightness colour picker");
        root.addView(square, new LinearLayout.LayoutParams(-1, 320));

        SeekBar hue = new SeekBar(context);
        hue.setMax(360);
        hue.setProgress(Math.round(hsv[0]));
        hue.setContentDescription("Hue slider");
        root.addView(hue, new LinearLayout.LayoutParams(-1, 52));

        EditText hex = new EditText(context);
        hex.setSingleLine(true);
        hex.setHint("HEX #RRGGBB");
        hex.setText(toHex(initialColor));
        root.addView(hex, new LinearLayout.LayoutParams(-1, 52));

        LinearLayout buttons = new LinearLayout(context);
        Button cancel = new Button(context);
        cancel.setText("Cancel");
        Button apply = new Button(context);
        apply.setText("Apply");
        buttons.addView(cancel, new LinearLayout.LayoutParams(0, 50, 1));
        buttons.addView(apply, new LinearLayout.LayoutParams(0, 50, 1));
        root.addView(buttons);

        hue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hsv[0] = progress;
                square.invalidate();
                hex.setText(toHex(Color.HSVToColor(hsv)));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        square.onChanged = color -> hex.setText(toHex(color));
        hex.setOnFocusChangeListener((v, focused) -> { if (!focused) applyHex(hex, hsv, square); });
        cancel.setOnClickListener(v -> dialog.dismiss());
        apply.setOnClickListener(v -> {
            applyHex(hex, hsv, square);
            callback.onColor(Color.HSVToColor(hsv));
            dialog.dismiss();
        });

        dialog.setContentView(root);
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.white);
            window.setLayout(-1, -2);
        }
    }

    private static void applyHex(EditText hex, float[] hsv, ColorSquare square) {
        try {
            String value = hex.getText().toString().trim();
            if (value.startsWith("#")) value = value.substring(1);
            if (value.length() == 6) {
                int color = Color.parseColor("#" + value);
                Color.colorToHSV(color, hsv);
                square.invalidate();
            }
        } catch (IllegalArgumentException ignored) { }
    }

    private static String toHex(int color) {
        return String.format("#%06X", 0xFFFFFF & color);
    }

    private static final class ColorSquare extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final float[] hsv;
        private CallbackColor onChanged;
        private float markerX, markerY;

        ColorSquare(Context context, float[] hsv) {
            super(context);
            this.hsv = hsv;
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        void setOnChanged(CallbackColor callback) { onChanged = callback; }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float hueColor = Color.HSVToColor(new float[]{hsv[0], 1f, 1f});
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
            canvas.drawCircle(markerX, markerY, 11, paint);
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            canvas.drawCircle(markerX, markerY, 11, paint);
            paint.setStyle(Paint.Style.FILL);
        }

        @Override public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
                hsv[1] = Math.max(0f, Math.min(1f, event.getX() / Math.max(1f, getWidth())));
                hsv[2] = Math.max(0f, Math.min(1f, 1f - event.getY() / Math.max(1f, getHeight())));
                invalidate();
                if (onChanged != null) onChanged.changed(Color.HSVToColor(hsv));
                return true;
            }
            return true;
        }

        interface CallbackColor { void changed(int color); }
    }
}
