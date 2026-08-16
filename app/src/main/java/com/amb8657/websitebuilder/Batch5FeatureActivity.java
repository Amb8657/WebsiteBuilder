package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Batch 5 editor layer: contextual properties and element-specific controls.
 *  It deliberately sits above the proven V4 engine so Batch 1-4 behavior remains intact.
 */
public class Batch5FeatureActivity extends EnhancedWebsiteBuilderV4Activity {
    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private EditText field(String hint, String value, boolean number) {
        EditText e = new EditText(this); e.setHint(hint); e.setText(value); e.setSingleLine(true);
        if (number) e.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        return e;
    }
    private void applyCommon(EditText x, EditText y, EditText w, EditText h) {
        try { if (selected == null) return; selected.x=Math.max(0,Integer.parseInt(x.getText().toString())); selected.y=Math.max(0,Integer.parseInt(y.getText().toString())); selected.w=Math.max(24,Integer.parseInt(w.getText().toString())); selected.h=Math.max(24,Integer.parseInt(h.getText().toString())); } catch(Exception ignored) {}
    }
    @Override void editor() {
        super.editor();
        ViewGroup toolbar=(ViewGroup)root.getChildAt(0);
        Button properties=btn("Properties");
        properties.setTextColor(CanvaDesignSystem.TEXT);
        properties.setBackground(gd(CanvaDesignSystem.PANEL_2,10));
        properties.setOnClickListener(v->showBatch5Properties());
        toolbar.addView(properties,new LinearLayout.LayoutParams(d(96),d(44)));
    }
    private void showBatch5Properties() {
        if (selected == null) { android.widget.Toast.makeText(this,"Select an element first",android.widget.Toast.LENGTH_SHORT).show(); return; }
        Block b=selected;
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(d(16),d(4),d(16),d(4));
        TextView type=tv("Selected: "+b.type+"  •  ID "+b.id,14,CanvaDesignSystem.MUTED); box.addView(type,new LinearLayout.LayoutParams(-1,d(36)));
        EditText x=field("X",String.valueOf(b.x),true), y=field("Y",String.valueOf(b.y),true), w=field("Width",String.valueOf(b.w),true), h=field("Height",String.valueOf(b.h),true);
        LinearLayout row1=new LinearLayout(this),row2=new LinearLayout(this); row1.addView(x,new LinearLayout.LayoutParams(0,d(52),1));row1.addView(y,new LinearLayout.LayoutParams(0,d(52),1));row2.addView(w,new LinearLayout.LayoutParams(0,d(52),1));row2.addView(h,new LinearLayout.LayoutParams(0,d(52),1));box.addView(row1);box.addView(row2);

        if ("Text".equals(b.type) || "Heading".equals(b.type) || "Button".equals(b.type)) {
            EditText font=field("Font size",String.valueOf(b.font),true); box.addView(font,new LinearLayout.LayoutParams(-1,d(52)));
            Button fontMinus=btn("− Font"),fontPlus=btn("+ Font"); LinearLayout fr=new LinearLayout(this);fr.addView(fontMinus,new LinearLayout.LayoutParams(0,d(44),1));fr.addView(fontPlus,new LinearLayout.LayoutParams(0,d(44),1));box.addView(fr);
            fontMinus.setOnClickListener(v->{b.font=Math.max(8,b.font-1);font.setText(String.valueOf(b.font));}); fontPlus.setOnClickListener(v->{b.font=Math.min(96,b.font+1);font.setText(String.valueOf(b.font));});
            if ("Button".equals(b.type)) {
                EditText radius=field("Corner radius",String.valueOf((int)b.radius),true);box.addView(radius,new LinearLayout.LayoutParams(-1,d(52)));
                Button bg=btn("Button background");bg.setOnClickListener(v->PhotoshopColorPickerDialog.show(this,b.bg,c->{b.bg=c;render();}));box.addView(bg,new LinearLayout.LayoutParams(-1,d(44)));
                Button action=btn("Button action: "+b.action);action.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("Button action").setItems(new String[]{"None","Open page","Open URL","Scroll to section"},(di,which)->{b.action=new String[]{"None","Open page","Open URL","Scroll to section"}[which];action.setText("Button action: "+b.action);}).show());box.addView(action,new LinearLayout.LayoutParams(-1,d(44)));
            }
        }
        if ("Image".equals(b.type)) {
            Button crop=btn(b.crop?"Crop mode: ON":"Crop mode: OFF");crop.setOnClickListener(v->{b.crop=!b.crop;crop.setText(b.crop?"Crop mode: ON":"Crop mode: OFF");});box.addView(crop,new LinearLayout.LayoutParams(-1,d(44)));
            Button preview=btn("Preview image");preview.setOnClickListener(v->preview());box.addView(preview,new LinearLayout.LayoutParams(-1,d(44)));
        }
        if ("Section".equals(b.type) || "Shape".equals(b.type)) {
            EditText radius=field("Radius",String.valueOf((int)b.radius),true);box.addView(radius,new LinearLayout.LayoutParams(-1,d(52)));
            Button fill=btn("Container fill");fill.setOnClickListener(v->PhotoshopColorPickerDialog.show(this,b.bg,c->{b.bg=c;render();}));box.addView(fill,new LinearLayout.LayoutParams(-1,d(44)));
            Button children=btn("Move children into container");children.setOnClickListener(v->{for(Block child:page.blocks) if(child!=b && child.parent==0 && child.x>=b.x && child.y>=b.y && child.x+child.w<=b.x+b.w && child.y+child.h<=b.y+b.h) child.parent=b.id;save();render();});box.addView(children,new LinearLayout.LayoutParams(-1,d(44)));
        }
        new AlertDialog.Builder(this).setTitle("Properties").setView(box).setNegativeButton("Cancel",null).setPositiveButton("Apply",(di,which)->{applyCommon(x,y,w,h);try{if("Text".equals(b.type)||"Heading".equals(b.type)||"Button".equals(b.type))b.font=Math.max(8,Math.min(96,Integer.parseInt(((EditText)box.getChildAt(2)).getText().toString()));}catch(Exception ignored){} save();render();}).show();
    }
}
