package com.amb8657.websitebuilder;

import java.util.List;

/** Coordinates layer commands with the order supplied to the visual canvas. */
public final class CanvasLayerController {
    private final LayerManager layerManager;
    private final LayerCommandExecutor executor;

    public CanvasLayerController(LayerManager manager) {
        layerManager = manager;
        executor = new LayerCommandExecutor(manager);
    }

    public boolean apply(LayerCommand command) {
        return executor.execute(command);
    }

    public <T> List<T> renderOrder(List<T> elements, LayerCanvasAdapter.IdProvider<T> ids) {
        return LayerCanvasAdapter.ordered(elements, layerManager.getLayers(), ids);
    }

    public LayerManager manager() { return layerManager; }
}
