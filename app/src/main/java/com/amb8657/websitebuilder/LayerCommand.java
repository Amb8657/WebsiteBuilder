package com.amb8657.websitebuilder;

/** UI-neutral commands exposed by the layer controls. */
public final class LayerCommand {
    public enum Type { BRING_FORWARD, BRING_TO_FRONT, SEND_BACKWARD, SEND_TO_BACK, BRING_ABOVE, SEND_BELOW }
    public final Type type;
    public final String targetId;
    public final String referenceId;

    public LayerCommand(Type type, String targetId, String referenceId) {
        this.type = type;
        this.targetId = targetId;
        this.referenceId = referenceId;
    }
}
