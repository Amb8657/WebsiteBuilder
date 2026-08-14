package com.amb8657.websitebuilder;

/** Executes layer commands against the shared layer model. */
public final class LayerCommandExecutor {
    private final LayerManager manager;

    public LayerCommandExecutor(LayerManager manager) {
        this.manager = manager;
    }

    public boolean execute(LayerCommand command) {
        if (command == null || command.targetId == null) return false;
        switch (command.type) {
            case BRING_FORWARD:
                return manager.bringForward(command.targetId);
            case BRING_TO_FRONT:
                return manager.bringToFront(command.targetId);
            case SEND_BACKWARD:
                return manager.sendBackward(command.targetId);
            case SEND_TO_BACK:
                return manager.sendToBack(command.targetId);
            case BRING_ABOVE:
                return manager.bringAbove(command.targetId, command.referenceId);
            case SEND_BELOW:
                return manager.sendBelow(command.targetId, command.referenceId);
            default:
                return false;
        }
    }
}
