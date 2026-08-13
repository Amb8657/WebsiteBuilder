package com.amb8657.websitebuilder;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity {
    static final int PICK_IMAGE = 41, CREATE_HTML = 42;
    LinearLayout root, canvas, toolbar;
    TextView projectTitle, pageTitle, status;
    ArrayList<Page> pages = new ArrayList<>();
    int currentPage = 0;
    SharedPreferences prefs;
    int ink=Color.rgb(28,30,34), muted=Color.rgb(105,108,114), paper=Color.rgb(246,247,249), accent=Color.rgb(38,40,45);

    static class Block {
        String type, text, imageUri;
        Block(String t,String x){type=t;text=x;imageUri="";}
        Block(String t,String x,String u){type=t;text=x;imageUri=u;}
    }
    static class Page {
        String name; ArrayList<Block> blocks=new ArrayList<>();
        Page(String n){name=n;}
    }

    @Override public void onCreate(Bundle b){super.onCreate(b);prefs=getSharedPreferences("website_builder",MODE_PRIVATE);load();build();render();}

    TextView label(String s,float size){TextView v=new TextView(this);v.setText(s);v.setTextSize(size);v.setTextColor(ink);v.setPadding(16,10,16,10);return v;}
    GradientDrawable bg(int c,float r){GradientDrawable g=new GradientDrawable();g.setColor(c);g.setCornerRadius(r);return g;}
    Button action(String s){Button b=new Button(this);b.setText(s);b.setTextSize(13);b.setAllCaps(false);b.setTextColor(ink);b.setBackground(bg(Color.WHITE,18));return b;}
    Page page(){return pages.get(currentPage);}

    void build(){
        root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(paper);
        LinearLayout top=new LinearLayout(this);top.setGravity(Gravity.CENTER_VERTICAL);top.setPadding(10,8,10,6);top.setBackgroundColor(Color.WHITE);
        TextView app=label("Website Builder",18);app.setTypeface(Typeface.DEFAULT_BOLD);top.addView(app,new LinearLayout.LayoutParams(145,56));
        projectTitle=label("My Website",17);projectTitle.setTypeface(Typeface.DEFAULT_BOLD);top.addView(projectTitle,new LinearLayout.LayoutParams(0,56,1));
        Button save=action("Save");Button preview=action("Preview");Button export=action("Export");
        top.addView(save,new LinearLayout.LayoutParams(74,52));top.addView(preview,new LinearLayout.LayoutParams(86,52));top.addView(export,new LinearLayout.LayoutParams(78,52));root.addView(top);

        LinearLayout pagebar=new LinearLayout(this);pagebar.setGravity(Gravity.CENTER_VERTICAL);pagebar.setPadding(10,4,10,4);pagebar.setBackgroundColor(Color.WHITE);
        Button pagesBtn=action("Pages");pageTitle=label("Home",14);pagebar.addView(pagesBtn,new LinearLayout.LayoutParams(75,44));pagebar.addView(pageTitle,new LinearLayout.LayoutParams(0,44,1));
        Button rename=action("Rename");pagebar.addView(rename,new LinearLayout.LayoutParams(82,44));status=label("Saved locally",11);status.setTextColor(muted);pagebar.addView(status,new LinearLayout.LayoutParams(125,44));root.addView(pagebar);
        View line=new View(this);line.setBackgroundColor(Color.rgb(225,227,231));root.addView(line,new LinearLayout.LayoutParams(-1,1));

        HorizontalScrollView hs=new HorizontalScrollView(this);hs.setHorizontalScrollBarEnabled(false);toolbar=new LinearLayout(this);toolbar.setPadding(8,7,8,7);
        String[] types={"Heading","Text","Button","Image","Section","Card","Navbar","Divider","Spacer","Footer"};
        for(String type:types){Button a=action("+ "+type);a.setOnClickListener(v->addBlock(type));toolbar.addView(a,new LinearLayout.LayoutParams(108,48));}
        hs.addView(toolbar);root.addView(hs,new LinearLayout.LayoutParams(-1,63));

        ScrollView sv=new ScrollView(this);sv.setFillViewport(true);canvas=new LinearLayout(this);canvas.setOrientation(LinearLayout.VERTICAL);canvas.setPadding(18,18,18,100);canvas.setBackgroundColor(Color.rgb(232,234,238));sv.addView(canvas);root.addView(sv,new LinearLayout.LayoutParams(-1,0,1));

        LinearLayout bottom=new LinearLayout(this);bottom.setPadding(8,6,8,6);bottom.setBackgroundColor(Color.WHITE);Button manage=action("Pages");Button assets=action("Assets");Button clear=action("Clear");Button settings=action("Settings");
        bottom.addView(manage,new LinearLayout.LayoutParams(0,54,1));bottom.addView(assets,new LinearLayout.LayoutParams(0,54,1));bottom.addView(clear,new LinearLayout.LayoutParams(0,54,1));bottom.addView(settings,new LinearLayout.LayoutParams(0,54,1));root.addView(bottom);
        setContentView(root);

        save.setOnClickListener(v->save());preview.setOnClickListener(v->preview());export.setOnClickListener(v->chooseExport());pagesBtn.setOnClickListener(v->pageManager());manage.setOnClickListener(v->pageManager());rename.setOnClickListener(v->renameProject());assets.setOnClickListener(v->pickImage());clear.setOnClickListener(v->{page().blocks.clear();render();save();});settings.setOnClickListener(v->settings());
    }

    void addBlock(String type){String text="";if(type.equals("Heading"))text="Your headline";else if(type.equals("Text"))text="Tell visitors about your work, services or project.";else if(type.equals("Button"))text="Get Started";else if(type.equals("Image"))text="Choose an image";else if(type.equals("Section"))text="New Section";else if(type.equals("Card"))text="Card title\nShort description";else if(type.equals("Navbar"))text="Home   About   Projects   Contact";else if(type.equals("Footer"))text="© 2026 Your Website";page().blocks.add(new Block(type,text));render();save();}

    TextView blockView(final int index){Block b=page().blocks.get(index);TextView v=label(b.text,b.type.equals("Heading")?27:16);v.setElevation(2);v.setGravity(Gravity.CENTER_VERTICAL);int h=82;
        if(b.type.equals("Heading")){v.setTypeface(Typeface.DEFAULT_BOLD);h=108;}if(b.type.equals("Image")){h=190;v.setGravity(Gravity.CENTER);}if(b.type.equals("Section")){h=125;v.setGravity(Gravity.CENTER);}if(b.type.equals("Card"))h=125;if(b.type.equals("Navbar"))h=60;if(b.type.equals("Spacer"))h=42;if(b.type.equals("Divider"))h=24;if(b.type.equals("Footer")){h=70;v.setGravity(Gravity.CENTER);v.setTextColor(Color.WHITE);}
        int c=Color.WHITE;if(b.type.equals("Image"))c=Color.rgb(218,221,226);if(b.type.equals("Section"))c=Color.rgb(242,243,245);if(b.type.equals("Button")){c=accent;v.setTextColor(Color.WHITE);v.setGravity(Gravity.CENTER);}if(b.type.equals("Footer"))c=Color.rgb(34,35,39);if(b.type.equals("Divider"))c=Color.rgb(205,207,211);if(b.type.equals("Navbar"))v.setTypeface(Typeface.DEFAULT_BOLD);v.setBackground(bg(c,16));
        if(b.type.equals("Image")&&!b.imageUri.isEmpty()){try{v.setText("IMAGE\n"+fileName(Uri.parse(b.imageUri)));v.setBackground(bg(Color.rgb(205,208,214),16));}catch(Exception ignored){}}
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,h);lp.setMargins(0,0,0,10);v.setLayoutParams(lp);v.setOnClickListener(x->editBlock(index));return v;}

    void render(){canvas.removeAllViews();pageTitle.setText(page().name);for(int i=0;i<page().blocks.size();i++)canvas.addView(blockView(i));if(page().blocks.size()==0){TextView e=label("Start building\n\nTap an element above to add it to this page.",18);e.setGravity(Gravity.CENTER);e.setTextColor(muted);e.setBackground(bg(Color.WHITE,18));canvas.addView(e,new LinearLayout.LayoutParams(-1,280));}}

    void editBlock(final int index){Block b=page().blocks.get(index);LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(18,0,18,0);EditText e=new EditText(this);e.setText(b.text);e.setHint("Content");box.addView(e,new LinearLayout.LayoutParams(-1,-2));
        LinearLayout row=new LinearLayout(this);Button up=action("Move up"),down=action("Move down");row.addView(up,new LinearLayout.LayoutParams(0,48,1));row.addView(down,new LinearLayout.LayoutParams(0,48,1));box.addView(row);up.setOnClickListener(x->{if(index>0){Collections.swap(page().blocks,index,index-1);render();save();}});down.setOnClickListener(x->{if(index<page().blocks.size()-1){Collections.swap(page().blocks,index,index+1);render();save();}});
        AlertDialog d=new AlertDialog.Builder(this).setTitle("Edit "+b.type).setView(box).setNeutralButton("Delete",(q,w)->{page().blocks.remove(index);render();save();}).setNegativeButton("Cancel",null).setPositiveButton("Save",(q,w)->{b.text=e.getText().toString();render();save();}).create();d.show();}

    void renameProject(){EditText e=new EditText(this);e.setSingleLine();e.setText(projectTitle.getText());new AlertDialog.Builder(this).setTitle("Project name").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{projectTitle.setText(e.getText().toString());save();}).show();}

    void pageManager(){String[] names=new String[pages.size()+2];for(int i=0;i<pages.size();i++)names[i]=pages.get(i).name;names[pages.size()]= "+ Add page";names[pages.size()+1]="Rename current page";new AlertDialog.Builder(this).setTitle("Pages").setItems(names,(d,w)->{if(w<pages.size()){currentPage=w;render();}else if(w==pages.size())addPage();else renamePage();}).show();}
    void addPage(){EditText e=new EditText(this);e.setHint("Page name");new AlertDialog.Builder(this).setTitle("New page").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Create",(d,w)->{String n=e.getText().toString().trim();if(n.isEmpty())n="Page "+(pages.size()+1);pages.add(new Page(n));currentPage=pages.size()-1;render();save();}).show();}
    void renamePage(){EditText e=new EditText(this);e.setText(page().name);new AlertDialog.Builder(this).setTitle("Rename page").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{page().name=e.getText().toString();render();save();}).show();}

    void pickImage(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("image/*");i.addCategory(Intent.CATEGORY_OPENABLE);startActivityForResult(i,PICK_IMAGE);}
    @Override protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);if(result!=RESULT_OK||data==null)return;if(request==PICK_IMAGE){Uri u=data.getData();try{getContentResolver().takePersistableUriPermission(u,Intent.FLAG_GRANT_READ_URI_PERMISSION);}catch(Exception ignored){}page().blocks.add(new Block("Image","Image",u.toString()));render();save();}else if(request==CREATE_HTML){Uri u=data.getData();if(u!=null)writeHtml(u);}}

    String fileName(Uri u){Cursor c=getContentResolver().query(u,null,null,null,null);if(c!=null){try{int n=c.getColumnIndex(OpenableColumns.DISPLAY_NAME);if(c.moveToFirst()&&n>=0)return c.getString(n);}finally{c.close();}}return "image";}
    void chooseExport(){Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);i.setType("text/html");i.putExtra(Intent.EXTRA_TITLE,slug(projectTitle.getText().toString())+".html");startActivityForResult(i,CREATE_HTML);}
    void writeHtml(Uri u){try{OutputStream out=getContentResolver().openOutputStream(u);out.write(html().getBytes("UTF-8"));out.close();Toast.makeText(this,"Website exported successfully",Toast.LENGTH_LONG).show();}catch(Exception e){Toast.makeText(this,"Export failed: "+e.getMessage(),Toast.LENGTH_LONG).show();}}

    String html(){StringBuilder h=new StringBuilder();h.append("<!doctype html><html><head><meta name=viewport content=\"width=device-width,initial-scale=1\"><title>").append(esc(projectTitle.getText().toString())).append("</title><style>");h.append("*{box-sizing:border-box}body{margin:0;font-family:Arial,sans-serif;color:#1c1e22;background:#fff}.site{max-width:1100px;margin:auto;padding:20px}.nav{padding:18px;background:#fff;border-bottom:1px solid #ddd;font-weight:700}.hero{padding:70px 20px;text-align:center}.section{padding:45px 20px;background:#f4f5f7;margin:12px 0;border-radius:16px}.card{padding:25px;background:#f6f7f9;border-radius:16px;margin:10px 0}.btn{display:inline-block;background:#26282d;color:white;padding:13px 22px;border-radius:10px;text-decoration:none}.divider{height:1px;background:#ddd;margin:20px 0}.spacer{height:45px}.footer{padding:30px;text-align:center;background:#222327;color:white;border-radius:12px}.image{width:100%;max-height:600px;object-fit:cover;border-radius:14px}</style></head><body><div class=site>");
        for(Page p:pages){if(p==page()){for(Block b:p.blocks){if(b.type.equals("Heading"))h.append("<div class=hero><h1>").append(esc(b.text)).append("</h1></div>");else if(b.type.equals("Text"))h.append("<p>").append(esc(b.text).replace("\n","<br>" )).append("</p>");else if(b.type.equals("Button"))h.append("<p><a class=btn href=\"#\">").append(esc(b.text)).append("</a></p>");else if(b.type.equals("Image")){String src=imageData(b.imageUri);h.append("<img class=image src=\"").append(src.isEmpty()?"":src).append(" alt=\"Image\">");}else if(b.type.equals("Section"))h.append("<section class=section><h2>").append(esc(b.text)).append("</h2></section>");else if(b.type.equals("Card"))h.append("<div class=card>").append(esc(b.text).replace("\n","<br>")).append("</div>");else if(b.type.equals("Navbar"))h.append("<nav class=nav>").append(esc(b.text)).append("</nav>");else if(b.type.equals("Divider"))h.append("<div class=divider></div>");else if(b.type.equals("Spacer"))h.append("<div class=spacer></div>");else if(b.type.equals("Footer"))h.append("<footer class=footer>").append(esc(b.text)).append("</footer>");}}}
        h.append("</div></body></html>");return h.toString();}

    String imageData(String uri){if(uri==null||uri.isEmpty())return "";try{Uri u=Uri.parse(uri);InputStream in=getContentResolver().openInputStream(u);ByteArrayOutputStream b=new ByteArrayOutputStream();byte[] buf=new byte[8192];int n;while((n=in.read(buf))!=-1)b.write(buf,0,n);in.close();String mime=getContentResolver().getType(u);if(mime==null)mime="image/jpeg";return "data:"+mime+";base64,"+Base64.encodeToString(b.toByteArray(),Base64.NO_WRAP);}catch(Exception e){return "";}}
    String esc(String s){return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");}
    String slug(String s){return s.toLowerCase().replaceAll("[^a-z0-9]+","-").replaceAll("^-|-$","");}

    void preview(){WebView w=new WebView(this);w.getSettings().setJavaScriptEnabled(false);w.loadDataWithBaseURL(null,html(),"text/html","UTF-8",null);new AlertDialog.Builder(this).setTitle("Website Preview").setView(w).setPositiveButton("Close",null).show();}
    void settings(){String[] a={"Save project","Reset current page","About Website Builder"};new AlertDialog.Builder(this).setTitle("Settings").setItems(a,(d,w)->{if(w==0)save();else if(w==1){page().blocks.clear();render();save();}else Toast.makeText(this,"Website Builder — local visual website editor",Toast.LENGTH_LONG).show();}).show();}

    void save(){SharedPreferences.Editor e=prefs.edit();e.putString("project",projectTitle.getText().toString());e.putInt("current",currentPage);e.putInt("pages",pages.size());for(int i=0;i<pages.size();i++){Page p=pages.get(i);e.putString("p"+i+"name",p.name);e.putInt("p"+i+"count",p.blocks.size());for(int j=0;j<p.blocks.size();j++){Block b=p.blocks.get(j);e.putString("p"+i+"b"+j,b.type+"\u0001"+b.text.replace("\u0001"," ")+"\u0001"+b.imageUri.replace("\u0001"," "));}}e.apply();status.setText("Saved just now");}
    void load(){pages.clear();String title=prefs.getString("project","My Website");int n=prefs.getInt("pages",0);if(n==0){pages.add(new Page("Home"));pages.get(0).blocks.add(new Block("Heading","Welcome to your website"));pages.get(0).blocks.add(new Block("Text","Start editing this page."));}else{for(int i=0;i<n;i++){Page p=new Page(prefs.getString("p"+i+"name","Page "+(i+1)));int c=prefs.getInt("p"+i+"count",0);for(int j=0;j<c;j++){String s=prefs.getString("p"+i+"b"+j,"");String[] a=s.split("\u0001",-1);p.blocks.add(new Block(a.length>0?a[0]:"Text",a.length>1?a[1]:"",a.length>2?a[2]:""));}pages.add(p);}}currentPage=Math.max(0,Math.min(prefs.getInt("current",0),pages.size()-1));}
}
