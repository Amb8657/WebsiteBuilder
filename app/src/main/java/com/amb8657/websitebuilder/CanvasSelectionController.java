package com.amb8657.websitebuilder;

/** Coordinates selection state with layer actions for the visual canvas. */
public final class CanvasSelectionController {
    private final SelectionState selection = new SelectionState();
    private final CanvasLayerController layers;

    public CanvasSelectionController(CanvasLayerController layers) {
        this.layers = layers;
    }

    public void select(String elementId) { selection.select(elementId); }
    public void clearSelection() { selection.clear(); }
    public String selectedId() { return selection.getSelectedId(); }
    public boolean isSelected(String id) { return selection.isSelected(id); }

    public boolean applyLayerCommand(LayerCommand.Type type, String referenceId) {
        String id = selectedId();
        if (id == null) return false;
        return layers.apply(new LayerCommand(type, id, referenceId));
    }
}
