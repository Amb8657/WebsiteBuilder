package com.amb8657.websitebuilder;

/** Visual image properties exposed by the no-code editor. */
public final class ImageStyle {
    public float opacity = 1f;
    public float cornerRadiusDp = 0f;
    public boolean circular = false;
    public boolean cropEnabled = false;
    public String fit = "cover";
    public float rotation = 0f;

    public void setOpacity(float value) {
        opacity = Math.max(0f, Math.min(1f, value));
    }

    public void setCornerRadius(float value) {
        cornerRadiusDp = Math.max(0f, value);
        if (cornerRadiusDp > 0f) circular = false;
    }

    public void setCircular(boolean value) {
        circular = value;
        if (value) cornerRadiusDp = 0f;
    }
}
