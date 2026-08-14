package com.amb8657.websitebuilder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/** First visual canvas for the modular editor: selectable, draggable, resizable elements. */
public final class EditorCanvasView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint selectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final ResizeController resizeController = new ResizeController();
    private final MoveController moveController = new MoveController();
    private final SelectionOverlayModel overlay = new SelectionOverlayModel();
    private final ResizeHandle[] handles = ResizeHandle.values();

    private int x = 80, y = 120, width = 420, height = 180;
    private float downX, downY;
    private boolean moving;
    private ResizeHandle activeHandle;
    private ResizeController.Geometry resizeStart;

    public EditorCanvasView(Context context) {
        super(context);
        paint.setColor(Color.WHITE);
        selectionPaint.setColor(Color.rgb(90, 120, 255));
        selectionPaint.setStyle(Paint.Style.STROKE);
        selectionPaint.setStrokeWidth(3f);
        setBackgroundColor(Color.rgb(245, 245, 248));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(new RectF(x, y, x + width, y + height), 18, 18, paint);
        paint.setColor(Color.DKGRAY);
        paint.setTextSize(32);
        canvas.drawText("Heading", x + 28, y + 65, paint);
        paint.setTextSize(20);
        canvas.drawText("Drag me or resize me", x + 28, y + 110, paint);

        if (overlay.visible) {
            selectionPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x, y, x + width, y + height, selectionPaint);
            selectionPaint.setStyle(Paint.Style.FILL);
            for (float[] p : handleCenters()) canvas.drawCircle(p[0], p[1], 10, selectionPaint);
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        float tx = event.getX(), ty = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = tx; downY = ty;
                activeHandle = hitHandle(tx, ty);
                if (activeHandle != null) {
                    resizeStart = new ResizeController.Geometry(x, y, width, height);
                } else if (inside(tx, ty)) {
                    moving = true;
                } else {
                    overlay.hide();
                    invalidate();
                    return true;
                }
                overlay.show("element-1", x, y, width, height);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                float dx = tx - downX, dy = ty - downY;
                if (activeHandle != null && resizeStart != null) {
                    ResizeController.Geometry g = resizeController.resize(resizeStart, activeHandle, dx, dy);
                    x = g.x; y = g.y; width = g.width; height = g.height;
                } else if (moving) {
                    MoveController.Position p = moveController.move(new MoveController.Position(x, y), dx, dy);
                    x = p.x; y = p.y;
                    downX = tx; downY = ty;
                }
                overlay.updateBounds(x, y, width, height);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                moving = false; activeHandle = null; resizeStart = null;
                return true;
            default: return true;
        }
    }

    private boolean inside(float px, float py) { return px >= x && px <= x + width && py >= y && py <= y + height; }

    private ResizeHandle hitHandle(float px, float py) {
        float r = 22;
        float[][] c = handleCenters();
        for (int i = 0; i < c.length; i++) if (Math.abs(px-c[i][0]) <= r && Math.abs(py-c[i][1]) <= r) return handles[i];
        return null;
    }

    private float[][] handleCenters() {
        return new float[][] {{x,y},{x+width/2f,y},{x+width,y},{x,y+height/2f},{x+width,y+height/2f},{x,y+height},{x+width/2f,y+height},{x+width,y+height}};
    }
}
