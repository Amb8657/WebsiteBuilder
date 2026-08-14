package com.amb8657.websitebuilder;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Independent editor screen foundation; kept separate from the legacy BuilderV2Activity. */
public class ModularEditorActivity extends Activity {
    private CanvasLayerController canvasLayers;
    private CanvasSelectionController selection;
    private CanvasInteractionController interaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayerManager manager = new LayerManager();
        canvasLayers = new CanvasLayerController(manager);
        selection = new CanvasSelectionController(canvasLayers);
        interaction = new CanvasInteractionController(selection);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setBackgroundColor(Color.rgb(19, 18, 23));
        TextView status = new TextView(this);
        status.setText("MAHA Builder\nModular Editor");
        status.setTextColor(Color.WHITE);
        status.setTextSize(22);
        status.setGravity(Gravity.CENTER);
        root.addView(status, new LinearLayout.LayoutParams(-1, -2));
        setContentView(root);
    }
}
