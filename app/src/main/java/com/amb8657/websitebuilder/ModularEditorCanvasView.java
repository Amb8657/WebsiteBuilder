package com.amb8657.websitebuilder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/** First real canvas surface for the independent modular editor. */
public final class ModularEditorCanvasView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF element = new RectF(80, 180, 520, 360);
    private final RectF handle = new RectF();
    private boolean selected;
    private float downX, downY;
    private boolean resizing;

    public ModularEditorCanvasView(Context context) {
        super(context);
        setBackgroundColor(Color.rgb(245, 245, 248));
        paint.setTextSize(34);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(element, 18, 18, paint);
        paint.setColor(Color.rgb(45, 45, 52));
        canvas.drawText("Heading", element.left + 32, element.centerY() + 12, paint);

        if (selected) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            paint.setColor(Color.rgb(80, 110, 255));
            canvas.drawRect(element, paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(80, 110, 255));
            float r = 10;
            canvas.drawCircle(element.left, element.top, r, paint);
            canvas.drawCircle(element.right, element.top, r, paint);
            canvas.drawCircle(element.left, element.bottom, r, paint);
            canvas.drawCircle(element.right, element.bottom, r, paint);
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = x; downY = y;
                resizing = selected && near(x, y, element.right, element.bottom);
                if (element.contains(x, y)) selected = true;
                else selected = false;
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (selected) {
                    float dx = x - downX, dy = y - downY;
                    if (resizing) {
                        element.right = Math.max(element.left + 140, element.right + dx);
                        element.bottom = Math.max(element.top + 90, element.bottom + dy);
                    } else {
                        element.offset(dx, dy);
                    }
                    downX = x; downY = y;
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resizing = false;
                return true;
            default: return true;
        }
    }

    private boolean near(float x, float y, float hx, float hy) {
        return Math.abs(x - hx) < 30 && Math.abs(y - hy) < 30;
    }
}
