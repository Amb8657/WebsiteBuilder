package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/** Batch 12: responsive design foundation from the master specification. */
public class Batch12FeatureActivity extends Batch11FeatureActivity {
    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private void msg(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
    private android.content.SharedPreferences rp() { return getSharedPreferences("responsive_v1", 0); }
    private String key(String suffix) { return "b:" + (selected == null ? 0 : selected.id) + ":" + suffix; }
    private boolean ok() { if (project == null || page == null) { msg("Open a website first"); return false; } return true; }
    private boolean selectedOk() { if (!ok()) return false; if (selected == null) { msg("Select an element first"); return false; } return true; }
    private void changed(String s) { save(); render(); msg(s); }

    @Override void editor() {
        super.editor();
        Button responsive = btn("Responsive");
        responsive.setTextColor(CanvaDesignSystem.TEXT);
        responsive.setBackground(gd(CanvaDesignSystem.PANEL_2, 10));
        responsive.setOnClickListener(v -> responsiveMenu());
        ViewGroup toolbar = (ViewGroup) root.getChildAt(0);
        toolbar.addView(responsive, new LinearLayout.LayoutParams(d(104), d(44)));
    }

    private void desktopPreview() { if(!ok())return; rp().edit().putString("preview","desktop").apply(); changed("Desktop preview"); }
    private void tabletPreview() { if(!ok())return; rp().edit().putString("preview","tablet").apply(); changed("Tablet preview"); }
    private void mobilePreview() { if(!ok())return; rp().edit().putString("preview","mobile").apply(); changed("Mobile preview"); }
    private void breakpointPreset() { if(!ok())return; String[] a={"Desktop ≥ 1024dp","Tablet 600–1023dp","Mobile < 600dp"}; new AlertDialog.Builder(this).setTitle("Breakpoints").setItems(a,(d,w)->{rp().edit().putInt("breakpoint",w).apply();msg(a[w]);}).show(); }
    private void hideOnDevice(String device) { if(!selectedOk())return; String k=key("hide_"+device); boolean n=!rp().getBoolean(k,false);rp().edit().putBoolean(k,n).apply();msg((n?"Hidden":"Visible")+" on "+device); }
    private void responsivePosition() { if(!selectedOk())return; rp().edit().putBoolean(key("position"),true).apply(); changed("Responsive positioning enabled"); }
    private void responsiveSize() { if(!selectedOk())return; rp().edit().putBoolean(key("size"),true).apply(); changed("Responsive sizing enabled"); }
    private void responsiveType() { if(!selectedOk())return; rp().edit().putBoolean(key("type"),true).apply(); changed("Responsive typography enabled"); }
    private void responsiveSection() { if(!ok())return; rp().edit().putBoolean("section_behavior",true).apply(); changed("Responsive section behavior enabled"); }
    private void resetResponsive() { if(!ok())return; rp().edit().clear().apply();changed("Responsive settings reset"); }

    private void responsiveMenu() {
        String[] items={"Desktop preview","Tablet preview","Mobile preview","Breakpoint presets","Toggle selected: hide on Desktop","Toggle selected: hide on Tablet","Toggle selected: hide on Mobile","Enable responsive positioning","Enable responsive sizing","Enable responsive typography","Enable responsive sections","Reset responsive settings"};
        new AlertDialog.Builder(this).setTitle("Responsive design").setItems(items,(d,w)->{switch(w){case 0:desktopPreview();break;case 1:tabletPreview();break;case 2:mobilePreview();break;case 3:breakpointPreset();break;case 4:hideOnDevice("desktop");break;case 5:hideOnDevice("tablet");break;case 6:hideOnDevice("mobile");break;case 7:responsivePosition();break;case 8:responsiveSize();break;case 9:responsiveType();break;case 10:responsiveSection();break;case 11:resetResponsive();break;default:break;}}).show();
    }
}
