package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/** Batch 14: advanced website elements from the master specification. */
public class Batch14FeatureActivity extends Batch4PersistenceActivity {
    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private void msg(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
    private boolean ok() { if(project==null || page==null){msg("Open a website first");return false;} return true; }

    @Override void editor() {
        super.editor();
        Button elements=btn("Elements");
        elements.setTextColor(CanvaDesignSystem.TEXT);
        elements.setBackground(gd(CanvaDesignSystem.PANEL_2,10));
        elements.setOnClickListener(v->elementsMenu());
        ViewGroup toolbar=(ViewGroup)root.getChildAt(0);
        toolbar.addView(elements,new LinearLayout.LayoutParams(d(92),d(44)));
    }

    private void addAdvanced(String type,String text) {
        if(!ok())return;
        Block b=new Block(nextId++,type,text);
        b.w=320;b.h=90;b.x=0;b.y=page.blocks.size()*96;
        page.blocks.add(b);selected=b;save();render();msg(type+" added");
    }

    private void addConfigured(String type,String title,String hint) {
        if(!ok())return;
        EditText e=edit(title,hint);
        new AlertDialog.Builder(this).setTitle(title).setView(e).setNegativeButton("Cancel",null).setPositiveButton("Add",(d,w)->{
            String value=e.getText().toString().trim();if(value.isEmpty())value=hint;addAdvanced(type,value);
        }).show();
    }

    private void elementsMenu() {
        String[] items={"Icon","Line","Video embed","Audio embed","Map","Social icon","Card","Testimonial","Pricing table","FAQ / accordion","Countdown","Table","Gallery","Slider / carousel"};
        new AlertDialog.Builder(this).setTitle("Advanced elements").setItems(items,(d,w)->{
            switch(w){
                case 0:addConfigured("Icon","Icon","★");break;
                case 1:addConfigured("Line","Line label","────────────");break;
                case 2:addConfigured("Video","Video URL","https://www.youtube.com/watch?v=");break;
                case 3:addConfigured("Audio","Audio URL","https://example.com/audio.mp3");break;
                case 4:addConfigured("Map","Map location","Nagpur, India");break;
                case 5:addConfigured("Social","Social handle","Instagram / Maha Architects");break;
                case 6:addConfigured("Card","Card content","Title\nDescription");break;
                case 7:addConfigured("Testimonial","Testimonial","Great design and service. — Client");break;
                case 8:addConfigured("Pricing","Pricing","Basic — ₹0\nPro — ₹999\nBusiness — ₹1999");break;
                case 9:addConfigured("FAQ","FAQ","Question?\nAnswer shown when opened.");break;
                case 10:addConfigured("Countdown","Countdown","7 days");break;
                case 11:addConfigured("Table","Table","Item | Value\nArea | 1200 sqft\nYear | 2026");break;
                case 12:addConfigured("Gallery","Gallery","Image 1\nImage 2\nImage 3");break;
                case 13:addConfigured("Slider","Slider","Slide 1\nSlide 2\nSlide 3");break;
                default:break;
            }
        }).show();
    }
}
