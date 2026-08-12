package com.amb8657.websitebuilder;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    LinearLayout root, canvas, tools;
    TextView projectTitle, status;
    ArrayList<Block> blocks = new ArrayList<>();
    SharedPreferences prefs;
    int ink = Color.rgb(31, 31, 36), muted = Color.rgb(105, 105, 112), brand = Color.rgb(34, 34, 34), paper = Color.rgb(248, 248, 246);

    static class Block {
        String type, text;
        Block(String t, String x) { type=t; text=x; }
    }

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences("site", MODE_PRIVATE);
        buildApp();
        loadSite();
    }

    TextView label(String s, float size) {
        TextView v = new TextView(this);
        v.setText(s); v.setTextSize(size); v.setTextColor(ink);
        v.setPadding(18, 10, 18, 10);
        return v;
    }

    GradientDrawable bg(int color, float radius) {
        GradientDrawable g = new GradientDrawable(); g.setColor(color); g.setCornerRadius(radius); return g;
    }

    Button action(String text) {
        Button b = new Button(this);
        b.setText(text); b.setTextSize(13); b.setAllCaps(false); b.setTextColor(ink);
        b.setBackground(bg(Color.WHITE, 18));
        return b;
    }

    void buildApp() {
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(paper);

        LinearLayout top = new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL); top.setPadding(12, 10, 12, 8); top.setBackgroundColor(Color.WHITE);
        TextView logo = label("MAHA", 17); logo.setTypeface(Typeface.DEFAULT_BOLD); logo.setTextColor(brand);
        top.addView(logo, new LinearLayout.LayoutParams(78, 54));
        projectTitle = label("My Website", 17); projectTitle.setTypeface(Typeface.DEFAULT_BOLD);
        top.addView(projectTitle, new LinearLayout.LayoutParams(0, 54, 1));
        Button save = action("Save"); top.addView(save, new LinearLayout.LayoutParams(82, 52));
        Button preview = action("Preview"); top.addView(preview, new LinearLayout.LayoutParams(92, 52));
        root.addView(top);

        LinearLayout sub = new LinearLayout(this); sub.setGravity(Gravity.CENTER_VERTICAL); sub.setPadding(16, 4, 16, 4); sub.setBackgroundColor(Color.WHITE);
        status = label("Local project • Saved on this phone", 12); status.setTextColor(muted); sub.addView(status, new LinearLayout.LayoutParams(0, 38, 1));
        Button rename = action("Rename"); sub.addView(rename, new LinearLayout.LayoutParams(88, 42)); root.addView(sub);
        View line = new View(this); line.setBackgroundColor(Color.rgb(230,230,230)); root.addView(line, new LinearLayout.LayoutParams(-1,1));

        LinearLayout workspace = new LinearLayout(this); workspace.setOrientation(LinearLayout.VERTICAL);
        HorizontalScrollView toolScroll = new HorizontalScrollView(this); toolScroll.setHorizontalScrollBarEnabled(false); tools = new LinearLayout(this); tools.setPadding(10,8,10,8);
        String[] types={"Heading","Text","Button","Image","Section","Card","Divider","Spacer","Navbar","Footer"};
        for(String type:types){ Button add=action("+ "+type); add.setOnClickListener(v->addBlock(type)); tools.addView(add,new LinearLayout.LayoutParams(112,48)); }
        toolScroll.addView(tools); workspace.addView(toolScroll,new LinearLayout.LayoutParams(-1,64));

        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(true); canvas=new LinearLayout(this); canvas.setOrientation(LinearLayout.VERTICAL); canvas.setPadding(22,22,22,90); canvas.setBackgroundColor(Color.rgb(235,235,238)); scroll.addView(canvas); workspace.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        root.addView(workspace,new LinearLayout.LayoutParams(-1,0,1));

        LinearLayout bottom = new LinearLayout(this); bottom.setGravity(Gravity.CENTER_VERTICAL); bottom.setPadding(10,6,10,6); bottom.setBackgroundColor(Color.WHITE);
        Button addPage=action("Pages"); Button assets=action("Assets"); Button settings=action("Settings"); Button clear=action("Clear");
        bottom.addView(addPage,new LinearLayout.LayoutParams(0,54,1)); bottom.addView(assets,new LinearLayout.LayoutParams(0,54,1)); bottom.addView(settings,new LinearLayout.LayoutParams(0,54,1)); bottom.addView(clear,new LinearLayout.LayoutParams(0,54,1)); root.addView(bottom);
        setContentView(root);

        save.setOnClickListener(v->saveSite()); preview.setOnClickListener(v->showPreview());
        rename.setOnClickListener(v->renameProject()); clear.setOnClickListener(v->{blocks.clear(); render(); saveSite();});
        addPage.setOnClickListener(v->Toast.makeText(this,"Pages: Home is ready. More pages can be added in the next build.",Toast.LENGTH_SHORT).show());
        assets.setOnClickListener(v->Toast.makeText(this,"Assets: image upload is ready to be connected to project storage.",Toast.LENGTH_SHORT).show());
        settings.setOnClickListener(v->showSettings());
    }

    void addBlock(String type) {
        String text;
        if(type.equals("Heading")) text="Your headline";
        else if(type.equals("Text")) text="Tell visitors about your work, services or project.";
        else if(type.equals("Button")) text="Get Started";
        else if(type.equals("Image")) text="IMAGE / PROJECT VISUAL";
        else if(type.equals("Section")) text="New Section";
        else if(type.equals("Card")) text="Card title\nShort description goes here.";
        else if(type.equals("Divider")) text="";
        else if(type.equals("Spacer")) text="";
        else if(type.equals("Navbar")) text="Home    About    Projects    Contact";
        else text="© 2026 MAHA ARCHITECTS";
        blocks.add(new Block(type,text)); render();
    }

    TextView blockView(final Block block, final int index) {
        TextView v=label(block.text, block.type.equals("Heading")?28:16); v.setTextColor(ink); v.setGravity(Gravity.CENTER_VERTICAL); v.setElevation(2);
        int height=82;
        if(block.type.equals("Heading")) {v.setTypeface(Typeface.DEFAULT_BOLD); height=112;}
        if(block.type.equals("Image")) {v.setGravity(Gravity.CENTER); height=190;}
        if(block.type.equals("Section")) {v.setGravity(Gravity.CENTER); height=120;}
        if(block.type.equals("Card")) height=120;
        if(block.type.equals("Spacer")) height=45;
        if(block.type.equals("Divider")) height=28;
        if(block.type.equals("Navbar")) height=62;
        if(block.type.equals("Footer")) {v.setGravity(Gravity.CENTER); height=72;}
        int color=Color.WHITE;
        if(block.type.equals("Image")) color=Color.rgb(224,224,224);
        if(block.type.equals("Section")) color=Color.rgb(242,242,242);
        if(block.type.equals("Button")) {color=brand; v.setTextColor(Color.WHITE); v.setGravity(Gravity.CENTER);}
        if(block.type.equals("Navbar")) {color=Color.WHITE; v.setTypeface(Typeface.DEFAULT_BOLD);}
        if(block.type.equals("Footer")) color=Color.rgb(35,35,38);
        if(block.type.equals("Footer")) v.setTextColor(Color.WHITE);
        v.setBackground(bg(color,16));
        v.setOnClickListener(x->editBlock(index));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,height); lp.setMargins(0,0,0,12); v.setLayoutParams(lp);
        return v;
    }

    void render() {
        canvas.removeAllViews();
        if(blocks.size()==0) {
            TextView empty=label("Start your website\n\nChoose an element above to begin.",18); empty.setGravity(Gravity.CENTER); empty.setTextColor(muted); empty.setBackground(bg(Color.WHITE,20)); canvas.addView(empty,new LinearLayout.LayoutParams(-1,260)); return;
        }
        for(int i=0;i<blocks.size();i++) canvas.addView(blockView(blocks.get(i),i));
    }

    void editBlock(final int index) {
        Block b=blocks.get(index); LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(20,0,20,0);
        EditText text=new EditText(this); text.setText(b.text); text.setHint("Content"); box.addView(text,new LinearLayout.LayoutParams(-1,-2));
        new AlertDialog.Builder(this).setTitle("Edit "+b.type).setView(box).setNeutralButton("Delete",(d,w)->{blocks.remove(index);render();saveSite();}).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{b.text=text.getText().toString();render();saveSite();}).show();
    }

    void renameProject(){ final EditText e=new EditText(this); e.setSingleLine(); e.setText(projectTitle.getText()); new AlertDialog.Builder(this).setTitle("Project name").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{projectTitle.setText(e.getText().toString());saveSite();}).show(); }

    void saveSite(){ StringBuilder s=new StringBuilder(); for(Block b:blocks){s.append(b.type.replace("|"," ")).append("|").append(b.text.replace("|"," ").replace("\n","\\n")).append("\n");} prefs.edit().putString("blocks",s.toString()).putString("title",projectTitle.getText().toString()).apply(); status.setText("Saved just now • Local project"); }

    void loadSite(){ String title=prefs.getString("title","My Website"); projectTitle.setText(title); String raw=prefs.getString("blocks",""); if(raw.length()>0){for(String line:raw.split("\n")){int p=line.indexOf('|'); if(p>0) blocks.add(new Block(line.substring(0,p),line.substring(p+1).replace("\\n","\n")));}} render(); }

    void showPreview(){
        ScrollView sc=new ScrollView(this); LinearLayout p=new LinearLayout(this); p.setOrientation(LinearLayout.VERTICAL); p.setPadding(24,24,24,60); p.setBackgroundColor(Color.WHITE);
        TextView brandText=label("MAHA ARCHITECTS",14); brandText.setTypeface(Typeface.DEFAULT_BOLD); brandText.setTextColor(brand); p.addView(brandText,new LinearLayout.LayoutParams(-1,50));
        for(Block b:blocks){TextView v=label(b.text,b.type.equals("Heading")?30:16);v.setPadding(18,16,18,16);v.setGravity(b.type.equals("Button")?Gravity.CENTER:Gravity.CENTER_VERTICAL); if(b.type.equals("Button")){v.setTextColor(Color.WHITE);v.setBackground(bg(brand,14));} else if(b.type.equals("Image")){v.setGravity(Gravity.CENTER);v.setBackground(bg(Color.LTGRAY,14));} p.addView(v,new LinearLayout.LayoutParams(-1,b.type.equals("Image")?190:Math.max(58,b.type.equals("Heading")?100:72))); }
        sc.addView(p); new AlertDialog.Builder(this).setTitle("Website Preview").setView(sc).setPositiveButton("Close",null).show();
    }

    void showSettings(){
        String[] items={"Dark editor (coming next)","Project backup","About MAHA Website Builder"};
        new AlertDialog.Builder(this).setTitle("Settings").setItems(items,(d,w)->{if(w==1) saveSite(); else Toast.makeText(this,items[w],Toast.LENGTH_SHORT).show();}).show();
    }
}
