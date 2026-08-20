package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/** Batch 16: SEO and publish metadata controls persisted with the current website. */
public class Batch16FeatureActivity extends Batch15FeatureActivity {
    private SharedPreferences seoPrefs;
    private int d(int v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
    private void msg(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
    private SharedPreferences seo(){if(seoPrefs==null)seoPrefs=getSharedPreferences("batch16_seo",0);return seoPrefs;}
    private void put(String key,String value){seo().edit().putString(key,value.trim()).apply();save();msg("Saved "+key);}
    private EditText field(String title,String key,String hint){return edit(title,seo().getString(key,hint));}

    @Override public void onCreate(android.os.Bundle state){
        seoPrefs=getSharedPreferences("batch16_seo",0);
        super.onCreate(state);
    }

    @Override void editor(){
        super.editor();
        Button seoButton=btn("SEO");
        seoButton.setTextColor(CanvaDesignSystem.TEXT);
        seoButton.setBackground(gd(CanvaDesignSystem.PANEL_2,10));
        seoButton.setOnClickListener(v->seoMenu());
        ViewGroup toolbar=(ViewGroup)root.getChildAt(0);
        toolbar.addView(seoButton,new LinearLayout.LayoutParams(d(72),d(44)));
    }

    private void textSetting(String title,String key,String hint){
        EditText e=field(title,key,hint);
        new AlertDialog.Builder(this).setTitle(title).setView(e).setNegativeButton("Cancel",null)
            .setPositiveButton("Save",(d,w)->put(key,e.getText().toString())).show();
    }

    /** 1: document/site title used by generated pages. */
    private void siteTitle(){textSetting("Site title","title","My Website");}
    /** 2: search-engine description. */
    private void description(){textSetting("Meta description","description","Describe your website in one short sentence.");}
    /** 3: comma-separated search keywords. */
    private void keywords(){textSetting("Meta keywords","keywords","architecture, design, portfolio");}
    /** 4: favicon asset/reference identifier. */
    private void favicon(){textSetting("Favicon","favicon","gmail");}
    /** 5: document language. */
    private void language(){textSetting("Site language","language","en");}
    /** 6: canonical URL. */
    private void canonical(){textSetting("Canonical URL","canonical","https://example.com/");}
    /** 7: robots directive. */
    private void robots(){textSetting("Robots directive","robots","index, follow");}
    /** 8: social sharing title. */
    private void ogTitle(){textSetting("Social share title","og_title","My Website");}
    /** 9: social sharing description. */
    private void ogDescription(){textSetting("Social share description","og_description","A preview description for social sharing.");}
    /** 10: project-level custom CSS stored for the export/publish layer. */
    private void customCss(){textSetting("Custom CSS","custom_css","/* Add site-wide CSS here */");}

    private void summary(){
        StringBuilder s=new StringBuilder();
        String[][] rows={{"Title","title"},{"Description","description"},{"Keywords","keywords"},{"Favicon","favicon"},{"Language","language"},{"Canonical","canonical"},{"Robots","robots"},{"OG title","og_title"},{"OG description","og_description"},{"Custom CSS","custom_css"}};
        for(String[] r:rows)s.append(r[0]).append(": ").append(seo().getString(r[1],"Not configured")).append("\n");
        new AlertDialog.Builder(this).setTitle("SEO & site metadata").setMessage(s.toString()).setPositiveButton("Done",null).show();
    }

    private void seoMenu(){
        String[] items={"Site title","Meta description","Meta keywords","Favicon","Site language","Canonical URL","Robots directive","Social share title","Social share description","Custom CSS","SEO summary"};
        new AlertDialog.Builder(this).setTitle("SEO & Metadata").setItems(items,(d,w)->{
            switch(w){case 0:siteTitle();break;case 1:description();break;case 2:keywords();break;case 3:favicon();break;case 4:language();break;case 5:canonical();break;case 6:robots();break;case 7:ogTitle();break;case 8:ogDescription();break;case 9:customCss();break;case 10:summary();break;default:break;}
        }).show();
    }
}
