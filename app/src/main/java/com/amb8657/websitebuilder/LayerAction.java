package com.amb8657.websitebuilder;

/** User-facing layer ordering commands for the visual editor. */
public enum LayerAction {
    BRING_FORWARD("Bring Forward"),
    BRING_TO_FRONT("Bring to Front"),
    SEND_BACKWARD("Send Backward"),
    SEND_TO_BACK("Send to Back"),
    BRING_ABOVE("Bring Above…"),
    SEND_BELOW("Send Below…");

    private final String label;
    LayerAction(String label) { this.label = label; }
    public String getLabel() { return label; }
}
