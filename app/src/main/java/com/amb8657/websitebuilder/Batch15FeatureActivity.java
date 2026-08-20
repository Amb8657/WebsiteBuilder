package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;

/** Batch 15: website structure expansion — hierarchy, navigation, shared chrome and 404. */
public class Batch15FeatureActivity extends Batch14FeatureActivity {
    private SharedPreferences sitePrefs;
    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private void msg(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
    private boolean ok() { if(project==null || page==null){msg("Open a website first");return false;} return true; }
    private SharedPreferences sp() { if(sitePrefs==null) sitePrefs=getSharedPreferences("batch15_site_structure",0); return sitePrefs; }
    private String pageKey(Page p) { return "p:" + p.name; }
    private void changed(String s) { save(); render(); msg(s); }

    @Override public void onCreate(android.os.Bundle state) {
        sitePrefs=getSharedPreferences("batch15_site_structure",0);
        super.onCreate(state);
    }

    @Override void editor() {
        super.editor();
        Button site=btn("Structure");
        site.setTextColor(CanvaDesignSystem.TEXT);
        site.setBackground(gd(CanvaDesignSystem.PANEL_2,10));
        site.setOnClickListener(v->structureMenu());
        ViewGroup toolbar=(ViewGroup)root.getChildAt(0);
        toolbar.addView(site,new LinearLayout.LayoutParams(d(96),d(44)));
    }

    /** 1: stable human-readable page URL/slug. */
    private void setSlug() {
        if(!ok())return;
        EditText e=edit("Page URL slug",sp().getString(pageKey(page)+":slug",slugify(page.name)));
        new AlertDialog.Builder(this).setTitle("Page URL").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{
            String s=slugify(e.getText().toString());
            if(s.isEmpty())s=slugify(page.name);
            sp().edit().putString(pageKey(page)+":slug",s).apply();
            msg("URL: /"+s);
        }).show();
    }

    private String slugify(String s) {
        if(s==null)return "";
        s=s.trim().toLowerCase().replaceAll("[^a-z0-9]+","-").replaceAll("^-+|-+$","");
        return s.length()>60?s.substring(0,60):s;
    }

    /** 2: parent/child page hierarchy. */
    private void setParentPage() {
        if(!ok())return;
        ArrayList<Page> choices=new ArrayList<>();
        for(Page p:project.pages)if(p!=page)choices.add(p);
        String[] labels=new String[choices.size()+1];labels[0]="No parent (top level)";
        for(int i=0;i<choices.size();i++)labels[i+1]=choices.get(i).name;
        String current=sp().getString(pageKey(page)+":parent","");
        int checked=0;for(int i=0;i<choices.size();i++)if(choices.get(i).name.equals(current))checked=i+1;
        new AlertDialog.Builder(this).setTitle("Page hierarchy").setSingleChoiceItems(labels,checked,(dialog,which)->{
            String parent=which==0?"":choices.get(which-1).name;
            sp().edit().putString(pageKey(page)+":parent",parent).apply();
            dialog.dismiss();changed(parent.isEmpty()?"Page is top level":"Parent: "+parent);
        }).show();
    }

    /** 3: hierarchy browser showing parent → child relationships. */
    private void hierarchyPreview() {
        if(!ok())return;
        StringBuilder out=new StringBuilder();
        for(Page p:project.pages){
            String parent=sp().getString(pageKey(p)+":parent","");
            if(parent.isEmpty())out.append("• ").append(p.name).append("\n");
            else out.append("   ↳ ").append(p.name).append("  (under ").append(parent).append(")\n");
        }
        new AlertDialog.Builder(this).setTitle("Site hierarchy").setMessage(out.length()==0?"No pages":out.toString()).setPositiveButton("Done",null).show();
    }

    /** 4: dropdown navigation entry as a real persisted site-navigation block. */
    private void addDropdown() {
        if(!ok())return;
        Block b=new Block(nextId++,"Dropdown","Menu");
        b.w=360;b.h=72;b.x=0;b.y=0;b.bg=Color.WHITE;b.tc=Color.DKGRAY;b.radius=d(8);b.fill="solid";
        page.blocks.add(b);selected=b;changed("Dropdown navigation added");
    }

    /** 5: mobile hamburger navigation entry. */
    private void addMobileMenu() {
        if(!ok())return;
        Block b=new Block(nextId++,"MobileMenu","☰  Menu");
        b.w=360;b.h=64;b.x=0;b.y=0;b.bg=Color.WHITE;b.tc=Color.DKGRAY;b.radius=d(8);b.fill="solid";
        page.blocks.add(b);selected=b;changed("Mobile menu added");
    }

    /** 6: reusable header template — copied to the current page when created. */
    private void createHeader() {
        if(!ok())return;
        if(sp().getBoolean("header:enabled",false)){msg("Shared header already exists");return;}
        sp().edit().putBoolean("header:enabled",true).putString("header:text","Website Header").apply();
        addChrome("Header","Website Header",0);
    }

    /** 7: reusable footer template — copied to the current page when created. */
    private void createFooter() {
        if(!ok())return;
        if(sp().getBoolean("footer:enabled",false)){msg("Shared footer already exists");return;}
        sp().edit().putBoolean("footer:enabled",true).putString("footer:text","Website Footer").apply();
        addChrome("Footer","Website Footer",page.blocks.size()*96);
    }

    private void addChrome(String type,String text,int y) {
        Block b=new Block(nextId++,type,text);b.w=360;b.h=72;b.x=0;b.y=Math.max(0,y);b.bg=CanvaDesignSystem.PANEL_2;b.tc=CanvaDesignSystem.TEXT;b.radius=d(8);page.blocks.add(b);selected=b;changed(type+" created");
    }

    /** 8: apply shared header/footer text to existing matching blocks on every page. */
    private void syncSharedChrome() {
        if(!ok())return;
        int changedCount=0;
        for(Page p:project.pages)for(Block b:p.blocks){
            if("Header".equals(b.type)&&sp().getBoolean("header:enabled",false)){b.text=sp().getString("header:text","Website Header");changedCount++;}
            if("Footer".equals(b.type)&&sp().getBoolean("footer:enabled",false)){b.text=sp().getString("footer:text","Website Footer");changedCount++;}
        }
        changed("Shared header/footer synced across "+changedCount+" blocks");
    }

    /** 9: designate a custom 404 page. */
    private void set404Page() {
        if(!ok())return;
        String[] names=new String[project.pages.size()];int checked=0;String current=sp().getString("404","");
        for(int i=0;i<project.pages.size();i++){names[i]=project.pages.get(i).name;if(names[i].equals(current))checked=i;}
        new AlertDialog.Builder(this).setTitle("404 page").setSingleChoiceItems(names,checked,(dialog,which)->{
            sp().edit().putString("404",project.pages.get(which).name).apply();dialog.dismiss();msg("404 page: "+project.pages.get(which).name);
        }).show();
    }

    /** 10: navigation/404 configuration summary for preview and export layers. */
    private void siteSummary() {
        if(!ok())return;
        StringBuilder s=new StringBuilder();
        s.append("Home: ").append(project.pages.get(0).name).append("\n");
        s.append("Current URL: /").append(sp().getString(pageKey(page)+":slug",slugify(page.name))).append("\n");
        s.append("Shared header: ").append(sp().getBoolean("header:enabled",false)?"ON":"OFF").append("\n");
        s.append("Shared footer: ").append(sp().getBoolean("footer:enabled",false)?"ON":"OFF").append("\n");
        s.append("404: ").append(sp().getString("404","Not configured")).append("\n\n");
        for(Page p:project.pages){String parent=sp().getString(pageKey(p)+":parent","");s.append(p.name).append(parent.isEmpty()?"":"  ←  "+parent).append("\n");}
        new AlertDialog.Builder(this).setTitle("Website structure summary").setMessage(s.toString()).setPositiveButton("Done",null).show();
    }

    private void structureMenu() {
        String[] items={"Page URL / slug","Set parent page","View hierarchy","Add dropdown navigation","Add mobile menu","Create reusable header","Create reusable footer","Sync shared header/footer","Set custom 404 page","Structure summary"};
        new AlertDialog.Builder(this).setTitle("Website Structure").setItems(items,(d,w)->{
            switch(w){case 0:setSlug();break;case 1:setParentPage();break;case 2:hierarchyPreview();break;case 3:addDropdown();break;case 4:addMobileMenu();break;case 5:createHeader();break;case 6:createFooter();break;case 7:syncSharedChrome();break;case 8:set404Page();break;case 9:siteSummary();break;default:break;}
        }).show();
    }
}
