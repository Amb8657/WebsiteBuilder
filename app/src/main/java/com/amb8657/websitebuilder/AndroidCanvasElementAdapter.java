package com.amb8657.websitebuilder;

import android.view.View;
import android.view.ViewGroup;

/** Adapter that applies editor geometry to a real Android View. */
public final class AndroidCanvasElementAdapter implements CanvasLayoutApplier.Element {
    private final View view;

    public AndroidCanvasElementAdapter(View view) {
        this.view = view;
    }

    @Override
    public void setPosition(int x, int y) {
        view.setX(x);
        view.setY(y);
    }

    @Override
    public void setSize(int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) return;
        params.width = Math.max(1, width);
        params.height = Math.max(1, height);
        view.setLayoutParams(params);
    }
}
