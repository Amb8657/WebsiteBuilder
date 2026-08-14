package com.amb8657.websitebuilder;

/** Unified visual color mode: solid, gradient, or no color/transparent. */
public final class ColorStyle {
    public enum Mode { NONE, SOLID, GRADIENT }

    public Mode mode = Mode.NONE;
    public String solidColor = "#FFFFFFFF";
    public GradientStyle gradient = new GradientStyle();

    public void clearColor() {
        mode = Mode.NONE;
        gradient.enabled = false;
    }

    public void useSolid(String color) {
        mode = Mode.SOLID;
        solidColor = color;
        gradient.enabled = false;
    }

    public void useGradient() {
        mode = Mode.GRADIENT;
        gradient.enabled = true;
    }

    public boolean hasVisibleColor() {
        return mode != Mode.NONE;
    }
}
