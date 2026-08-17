package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;

/** Batch 14: advanced website elements from the master specification. */
public class Batch14FeatureActivity extends Batch4PersistenceActivity {
    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private void msg(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
    private boolean ok() { if(project==null || page==null){msg("Open a website first");return false;} return true; }

    @Override void editor() {
        super.editor();
        Button advanced = btn("Elements");
        advanced.setTextColor(CanvaDesignSystem.TEXT);
        advanced.setBackground(gd(CanvaDesignSystem.PANEL_2,10));
        advanced.setOnClickListener(v -> elementsMenu());
        ViewGroup toolbar=(ViewGroup)root.getChildAt(0);
        toolbar.addView(advanced,new LinearLayout.LayoutParams(d(92),d(44)));
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
        String[] items={
            "Icon","Line","Video embed","Audio embed","Map","Social icon",
            "Card","Testimonial","Pricing table","FAQ / accordion","Countdown",
            "Table","Gallery","Slider / carousel"
        };
        new AlertDialog.Builder(this).setTitle("Advanced elements").setItems(items,(d,w)->{
            switch(w){
                case 0:addConfigured("Icon","Icon","★");break;
                case 1:addConfigured("Line","Line label","────────────");break;
                case 2:addConfigured("Video","Video URL","https://www.youtube.com/watch?v=");break;
                case 3:addConfigured("Audio","Audio URL","https://example.com/audio.mp3");break;
                case 4:addConfigured("Map","Map location","Nagpur, India");break;
                case 5:addConfigured("Social","Social handle","Instagram / Maha Architects");break;
                case 6:addConfigured("Card","Card content","Title\nDescription");break;
                case 7:addConfigured("Testimonial","Testimonial","“Great design and service.” — Client");break;
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

    @Override TextView blockView(Block b,int index) {
        if(!isAdvanced(b.type)) return super.blockView(b,index);
        LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(d(14),d(10),d(14),d(10));
        box.setBackground(gd(Color.WHITE,10));
        TextView title=tv(advancedTitle(b.type),16,Color.rgb(35,34,40));title.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);box.addView(title,new LinearLayout.LayoutParams(-1,d(32)));
        TextView body=tv(b.text,13,Color.rgb(80,78,88));body.setPadding(0,d(4),0,d(4));box.addView(body,new LinearLayout.LayoutParams(-1,d(50)));
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,d(92));p.setMargins(0,0,0,d(7));box.setLayoutParams(p);
        if("FAQ".equals(b.type)) box.setOnClickListener(v->{body.setVisibility(body.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);});
        else if("Slider".equals(b.type)) box.setOnClickListener(v->{String[] s=b.text.split("\\n");if(s.length>1){String first=s[0];System.arraycopy(s,1,s,0,s.length-1);s[s.length-1]=first;b.text=String.join("\n",s);save();render();}});
        else box.setOnClickListener(v->editBlock(b));
        return wrap(box,p);
    }

    private LinearLayout wrap(LinearLayout v,LinearLayout.LayoutParams p){LinearLayout outer=new LinearLayout(this);outer.addView(v,p);return outer;}
    private boolean isAdvanced(String t){return Arrays.asList("Icon","Line","Video","Audio","Map","Social","Card","Testimonial","Pricing","FAQ","Countdown","Table","Gallery","Slider").contains(t);}
    private String advancedTitle(String t){if("Video".equals(t))return "Video embed";if("Audio".equals(t))return "Audio player";if("Map".equals(t))return "Map";if("Social".equals(t))return "Social link";if("Pricing".equals(t))return "Pricing table";if("FAQ".equals(t))return "FAQ / accordion";if("Slider".equals(t))return "Slider / carousel";return t;}
}
