package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/** Batch 10: focused visual styling controls using the existing V4 Block model. */
public class Batch10FeatureActivity extends Batch9FeatureActivity {
    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private void msg(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }

    @Override void editor() {
        super.editor();
        Button style = btn("Style");
        style.setTextColor(CanvaDesignSystem.TEXT);
        style.setBackground(gd(CanvaDesignSystem.PANEL_2, 10));
        style.setOnClickListener(v -> styleMenu());
        ViewGroup toolbar = (ViewGroup) root.getChildAt(0);
        toolbar.addView(style, new LinearLayout.LayoutParams(d(78), d(44)));
    }

    private boolean selectedOk() {
        if (selected == null) { msg("Select an element first"); return false; }
        return true;
    }
    private void changed(String text) { save(); render(); msg(text); }
    private void opacity100() { if(!selectedOk())return; selected.opacity=1f; changed("Opacity 100%"); }
    private void opacity75() { if(!selectedOk())return; selected.opacity=.75f; changed("Opacity 75%"); }
    private void opacity50() { if(!selectedOk())return; selected.opacity=.50f; changed("Opacity 50%"); }
    private void radius0() { if(!selectedOk())return; selected.radius=0f; changed("Corner radius 0dp"); }
    private void radius12() { if(!selectedOk())return; selected.radius=d(12); changed("Corner radius 12dp"); }
    private void radius24() { if(!selectedOk())return; selected.radius=d(24); changed("Corner radius 24dp"); }
    private void fontUp() { if(!selectedOk())return; selected.font=Math.min(96,selected.font+2); changed("Font size +2sp"); }
    private void fontDown() { if(!selectedOk())return; selected.font=Math.max(8,selected.font-2); changed("Font size -2sp"); }
    private void toggleFill() { if(!selectedOk())return; selected.fill="solid".equalsIgnoreCase(selected.fill)?"outline":"solid"; changed("Fill: "+selected.fill); }
    private void resetStyle() { if(!selectedOk())return; selected.opacity=1f;selected.radius=0f;selected.fill="solid";selected.font=18;changed("Style reset"); }

    private void styleMenu() {
        String[] items={"Opacity 100%","Opacity 75%","Opacity 50%","Corner radius 0dp","Corner radius 12dp","Corner radius 24dp","Font size +2sp","Font size -2sp","Toggle solid / outline","Reset visual style"};
        new AlertDialog.Builder(this).setTitle("Style").setItems(items,(d,w)->{
            switch(w){case 0:opacity100();break;case 1:opacity75();break;case 2:opacity50();break;case 3:radius0();break;case 4:radius12();break;case 5:radius24();break;case 6:fontUp();break;case 7:fontDown();break;case 8:toggleFill();break;case 9:resetStyle();break;default:break;}
        }).show();
    }
}
