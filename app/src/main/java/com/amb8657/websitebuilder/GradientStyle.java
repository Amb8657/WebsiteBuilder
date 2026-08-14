package com.amb8657.websitebuilder;

/** No-code gradient styling shared by sections, shapes, buttons and backgrounds. */
public final class GradientStyle {
    public boolean enabled = false;
    public String startColor = "#FFFFFFFF";
    public String endColor = "#000000FF";
    public float angleDegrees = 90f;
    public float startPosition = 0f;
    public float endPosition = 1f;

    public void setAngle(float angle) {
        angleDegrees = ((angle % 360f) + 360f) % 360f;
    }

    public void setPositions(float start, float end) {
        startPosition = Math.max(0f, Math.min(1f, start));
        endPosition = Math.max(startPosition, Math.min(1f, end));
    }
}
