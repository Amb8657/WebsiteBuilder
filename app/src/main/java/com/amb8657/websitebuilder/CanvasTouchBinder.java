package com.amb8657.websitebuilder;

import android.view.MotionEvent;
import android.view.View;

/** Binds the reusable touch dispatcher to a real Android View. */
public final class CanvasTouchBinder {
    private final CanvasTouchDispatcher dispatcher;
    private final CanvasLayoutApplier applier = new CanvasLayoutApplier();

    public CanvasTouchBinder(CanvasTouchDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public boolean move(View view, MotionEvent event, String id, MoveController.Position start) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            dispatcher.beginMove(id, event.getRawX(), event.getRawY(), start);
            return true;
        }
        if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            MoveController.Position next = dispatcher.updateMove(event.getRawX(), event.getRawY());
            applier.applyMove(new AndroidCanvasElementAdapter(view), next);
            return true;
        }
        if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            dispatcher.end();
            return true;
        }
        return true;
    }

    public boolean resize(View view, MotionEvent event, String id, ResizeHandle handle,
                          ResizeController.Geometry start) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            dispatcher.beginResize(id, handle, event.getRawX(), event.getRawY(), start);
            return true;
        }
        if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            ResizeController.Geometry next = dispatcher.updateResize(event.getRawX(), event.getRawY());
            applier.applyResize(new AndroidCanvasElementAdapter(view), next);
            return true;
        }
        if (event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            dispatcher.end();
            return true;
        }
        return true;
    }
}
