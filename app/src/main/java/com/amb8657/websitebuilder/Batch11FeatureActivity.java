package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/** Batch 11: website structure/page-management foundation from the master specification. */
public class Batch11FeatureActivity extends Batch10FeatureActivity {
    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private void msg(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }

    @Override void editor() {
        super.editor();
        Button site = btn("Site");
        site.setTextColor(CanvaDesignSystem.TEXT);
        site.setBackground(gd(CanvaDesignSystem.PANEL_2, 10));
        site.setOnClickListener(v -> siteMenu());
        ViewGroup toolbar = (ViewGroup) root.getChildAt(0);
        toolbar.addView(site, new LinearLayout.LayoutParams(d(70), d(44)));
    }

    private boolean pageOk() { if (project == null || page == null) { msg("Open a website first"); return false; } return true; }
    private void addPage() {
        if (!pageOk()) return;
        Page p = new Page("Page " + (project.pages.size() + 1));
        project.pages.add(p); page = p; selected = null; save(); render(); msg("Page added");
    }
    private void renamePage() {
        if (!pageOk()) return;
        EditText e = edit("Page name", page.name);
        new AlertDialog.Builder(this).setTitle("Rename page").setView(e)
            .setNegativeButton("Cancel", null).setPositiveButton("Save", (d,w) -> {
                String n = e.getText().toString().trim(); if (!n.isEmpty()) page.name = n;
                save(); render(); msg("Page renamed");
            }).show();
    }
    private void duplicatePage() {
        if (!pageOk()) return;
        Page copy = new Page(page.name + " Copy"); copy.bg = page.bg;
        for (Block src : page.blocks) {
            Block b = new Block(nextId++, src.type, src.text); b.parent=src.parent; b.x=src.x; b.y=src.y; b.w=src.w; b.h=src.h;
            b.bg=src.bg; b.bg2=src.bg2; b.tc=src.tc; b.font=src.font; b.opacity=src.opacity; b.radius=src.radius;
            b.uri=src.uri; b.action=src.action; b.target=src.target; b.fill=src.fill; b.crop=src.crop; b.animation=src.animation;
            copy.blocks.add(b);
        }
        int at = project.pages.indexOf(page) + 1; project.pages.add(at, copy); page = copy; selected=null; save(); render(); msg("Page duplicated");
    }
    private void deletePage() {
        if (!pageOk()) return;
        if (project.pages.size() <= 1) { msg("A website must keep at least one page"); return; }
        int at = project.pages.indexOf(page); project.pages.remove(page); page = project.pages.get(Math.max(0, at-1)); selected=null; save(); render(); msg("Page deleted");
    }
    private void movePageLeft() {
        if (!pageOk()) return; int i=project.pages.indexOf(page); if(i<=0){msg("Already first page");return;}
        project.pages.remove(i); project.pages.add(i-1,page); save(); render(); msg("Page moved left");
    }
    private void movePageRight() {
        if (!pageOk()) return; int i=project.pages.indexOf(page); if(i<0||i>=project.pages.size()-1){msg("Already last page");return;}
        project.pages.remove(i); project.pages.add(i+1,page); save(); render(); msg("Page moved right");
    }
    private void pageBackground() {
        if (!pageOk()) return;
        final String[] colors={"White","Soft gray","Dark","Brand accent"};
        new AlertDialog.Builder(this).setTitle("Page background").setItems(colors,(d,w)->{
            if(w==0) page.bg=android.graphics.Color.WHITE;
            else if(w==1) page.bg=android.graphics.Color.rgb(245,246,248);
            else if(w==2) page.bg=android.graphics.Color.rgb(20,23,30);
            else page.bg=CanvaDesignSystem.ACCENT;
            save(); render(); msg("Page background updated");
        }).show();
    }
    private void setHomePage() {
        if (!pageOk()) return; int i=project.pages.indexOf(page); if(i>0){project.pages.remove(i);project.pages.add(0,page);} save();render();msg("Current page set as Home");
    }
    private void pagePreview() {
        if (!pageOk()) return;
        StringBuilder s=new StringBuilder(); for(int i=0;i<project.pages.size();i++){s.append(i+1).append(". ").append(project.pages.get(i).name); if(i==0)s.append("  [HOME]"); s.append("\n");}
        new AlertDialog.Builder(this).setTitle("Site navigation preview").setMessage(s.toString()).setPositiveButton("Done",null).show();
    }
    private void siteMenu() {
        String[] items={"Add page","Rename current page","Duplicate current page","Delete current page","Move page left","Move page right","Page background","Set current page as Home","Navigation preview"};
        new AlertDialog.Builder(this).setTitle("Site structure").setItems(items,(d,w)->{
            switch(w){case 0:addPage();break;case 1:renamePage();break;case 2:duplicatePage();break;case 3:deletePage();break;case 4:movePageLeft();break;case 5:movePageRight();break;case 6:pageBackground();break;case 7:setHomePage();break;case 8:pagePreview();break;default:break;}
        }).show();
    }
}
