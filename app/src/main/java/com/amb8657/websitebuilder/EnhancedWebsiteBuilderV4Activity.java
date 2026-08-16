package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/** V4 editor controls: duplicate, clipboard, history, grouping, lock/hide, layers and names. */
public class EnhancedWebsiteBuilderV4Activity extends WebsiteBuilderV4Activity {
    private final Deque<String> undo = new ArrayDeque<>();
    private final Deque<String> redo = new ArrayDeque<>();
    private android.content.SharedPreferences controlPrefs;
    private static final String PREF = "v4_editor_controls";

    @Override public void onCreate(android.os.Bundle b) {
        controlPrefs = getSharedPreferences(PREF, 0);
        super.onCreate(b);
    }

    /** Local toolbar button helper; the V4 parent keeps its equivalent private. */
    private Button pill(String text) {
        Button b = btn(text);
        b.setTextColor(CanvaDesignSystem.TEXT);
        b.setBackground(gd(CanvaDesignSystem.PANEL_2, 10));
        b.setPadding(dp(10), 0, dp(10), 0);
        return b;
    }

    @Override void editor() {
        super.editor();
        Button edit = pill("Edit");
        edit.setBackground(gd(CanvaDesignSystem.ACCENT, 10));
        edit.setOnClickListener(v -> editMenu());
        ViewGroup toolbar = (ViewGroup) root.getChildAt(0);
        toolbar.addView(edit, new LinearLayout.LayoutParams(dp(64), dp(44)));
    }

    private String key(Block b, String suffix) { return "b:" + b.id + ":" + suffix; }
    private boolean flag(Block b, String suffix) { return controlPrefs.getBoolean(key(b, suffix), false); }
    private String name(Block b) { return controlPrefs.getString(key(b, "name"), b.type + " " + b.id); }
    private void setFlag(Block b, String suffix, boolean value) { controlPrefs.edit().putBoolean(key(b, suffix), value).apply(); }
    private void setName(Block b, String value) { controlPrefs.edit().putString(key(b, "name"), value).apply(); }

    private void pushUndo() {
        String s = snapshot();
        if (!undo.isEmpty() && undo.peek().equals(s)) return;
        undo.push(s); while (undo.size() > 30) undo.removeLast(); redo.clear();
    }

    private String snapshot() {
        try {
            JSONObject o = new JSONObject(); o.put("project", project.name); o.put("page", page.name);
            JSONArray ps = new JSONArray();
            for (Page p : project.pages) {
                JSONObject po = new JSONObject().put("name", p.name).put("bg", p.bg); JSONArray bs = new JSONArray();
                for (Block b : p.blocks) bs.put(blockJson(b));
                po.put("blocks", bs); ps.put(po);
            }
            o.put("pages", ps).put("nextId", nextId); return o.toString();
        } catch (Exception e) { return ""; }
    }

    private JSONObject blockJson(Block b) throws Exception {
        return new JSONObject().put("id",b.id).put("parent",b.parent).put("type",b.type).put("text",b.text)
                .put("x",b.x).put("y",b.y).put("w",b.w).put("h",b.h).put("bg",b.bg).put("bg2",b.bg2)
                .put("tc",b.tc).put("font",b.font).put("opacity",b.opacity).put("radius",b.radius)
                .put("uri",b.uri).put("action",b.action).put("target",b.target).put("fill",b.fill)
                .put("crop",b.crop).put("animation",b.animation);
    }

    private void restore(String raw) {
        try {
            JSONObject o = new JSONObject(raw); project.name=o.optString("project",project.name);
            project.pages.clear(); JSONArray ps=o.getJSONArray("pages");
            for(int i=0;i<ps.length();i++) { JSONObject po=ps.getJSONObject(i); Page p=new Page(po.optString("name","Home")); p.bg=po.optInt("bg",Color.WHITE); JSONArray bs=po.optJSONArray("blocks");
                if(bs!=null) for(int j=0;j<bs.length();j++){JSONObject x=bs.getJSONObject(j);Block b=new Block(x.optInt("id"),x.optString("type","Text"),x.optString("text",""));
                    b.parent=x.optInt("parent",0);b.x=x.optInt("x",24);b.y=x.optInt("y",24);b.w=x.optInt("w",300);b.h=x.optInt("h",80);b.bg=x.optInt("bg",Color.TRANSPARENT);b.bg2=x.optInt("bg2",Color.WHITE);b.tc=x.optInt("tc",Color.DKGRAY);b.font=x.optInt("font",18);b.opacity=(float)x.optDouble("opacity",1);b.radius=(float)x.optDouble("radius",0);b.uri=x.optString("uri","");b.action=x.optString("action","None");b.target=x.optString("target","");b.fill=x.optString("fill","solid");b.crop=x.optBoolean("crop",false);b.animation=x.optString("animation","None");p.blocks.add(b);}
                project.pages.add(p);
            }
            nextId=o.optInt("nextId",nextId); page=project.pages.get(0); for(Page p:project.pages) if(p.name.equals(o.optString("page",page.name))) page=p;
            selected=null; save(); render();
        } catch(Exception ignored) {}
    }

    private void undoAction(){ if(undo.isEmpty()){ToastMsg("Nothing to undo");return;} redo.push(snapshot()); restore(undo.pop()); }
    private void redoAction(){ if(redo.isEmpty()){ToastMsg("Nothing to redo");return;} undo.push(snapshot()); restore(redo.pop()); }
    private void ToastMsg(String s){android.widget.Toast.makeText(this,s,android.widget.Toast.LENGTH_SHORT).show();}

    private Block cloneBlock(Block src, int id) { Block b=new Block(id,src.type,src.text); b.parent=src.parent;b.x=src.x;b.y=src.y;b.w=src.w;b.h=src.h;b.bg=src.bg;b.bg2=src.bg2;b.tc=src.tc;b.font=src.font;b.opacity=src.opacity;b.radius=src.radius;b.uri=src.uri;b.action=src.action;b.target=src.target;b.fill=src.fill;b.crop=src.crop;b.animation=src.animation; return b; }

    private void duplicate(){ if(selected==null){ToastMsg("Select an element first");return;} if(flag(selected,"locked")){ToastMsg("Element is locked");return;} pushUndo(); Block b=cloneBlock(selected,nextId++); b.x+=24;b.y+=24;page.blocks.add(b);selected=b;save();render(); }
    private Block clipboard;
    private void copy(){ if(selected==null){ToastMsg("Select an element first");return;} clipboard=cloneBlock(selected,-1);ToastMsg("Copied"); }
    private void paste(){ if(clipboard==null){ToastMsg("Clipboard is empty");return;} pushUndo(); Block b=cloneBlock(clipboard,nextId++);b.x+=24;b.y+=24;page.blocks.add(b);selected=b;save();render(); }

    private void toggleLock(){ if(selected==null){ToastMsg("Select an element first");return;} pushUndo();setFlag(selected,"locked",!flag(selected,"locked"));save();render(); }
    private void toggleHide(){ if(selected==null){ToastMsg("Select an element first");return;} pushUndo();setFlag(selected,"hidden",!flag(selected,"hidden"));if(flag(selected,"hidden"))selected=null;save();render(); }

    private void rename(){ if(selected==null){ToastMsg("Select an element first");return;} final android.widget.EditText e=edit("Element name",name(selected)); new AlertDialog.Builder(this).setTitle("Rename element").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{pushUndo();setName(selected,e.getText().toString().trim().isEmpty()?selected.type:e.getText().toString().trim());save();render();}).show(); }

    private void group(){
        if(selected==null){ToastMsg("Select an element first");return;}
        ArrayList<Block> candidates=new ArrayList<>();for(Block b:page.blocks)if(b!=selected&&b.parent==0&&!flag(b,"hidden"))candidates.add(b);
        if(candidates.isEmpty()){ToastMsg("Add another top-level element to group");return;}
        String[] labels=new String[candidates.size()];for(int i=0;i<candidates.size();i++)labels[i]=name(candidates.get(i));
        new AlertDialog.Builder(this).setTitle("Group with…").setItems(labels,(d,which)->groupWith(candidates.get(which))).show();
    }
    private void groupWith(Block other){Block a=selected;if(a==null||other==null)return;pushUndo();int x=Math.min(a.x,other.x),y=Math.min(a.y,other.y);int r=Math.max(a.x+a.w,other.x+other.w),bot=Math.max(a.y+a.h,other.y+other.h);Block g=new Block(nextId++,"Section","");g.x=x;g.y=y;g.w=Math.max(MIN_W,r-x);g.h=Math.max(MIN_H,bot-y);g.bg=Color.TRANSPARENT;page.blocks.add(0,g);a.x-=x;a.y-=y;other.x-=x;other.y-=y;a.parent=g.id;other.parent=g.id;selected=g;setName(g,"Group "+g.id);save();render();}
    private void ungroup(){if(selected==null||!"Section".equals(selected.type)){ToastMsg("Select a group/section");return;}Block g=selected;boolean has=false;pushUndo();for(Block b:page.blocks)if(b.parent==g.id){b.x+=g.x;b.y+=g.y;b.parent=0;has=true;}if(!has){ToastMsg("This section has no children");return;}page.blocks.remove(g);selected=null;save();render();}

    private void layers(){
        final ArrayList<Block> visible=new ArrayList<>();for(Block b:page.blocks)if(b.parent==0)visible.add(b);
        String[] labels=new String[visible.size()];for(int i=0;i<visible.size();i++){Block b=visible.get(i);labels[i]=(flag(b,"hidden")?"◌ ":"")+(flag(b,"locked")?"🔒 ":"")+name(b);}
        new AlertDialog.Builder(this).setTitle("Layers").setItems(labels,(d,which)->{selected=visible.get(which);layerActions();}).setNegativeButton("Close",null).show();
    }
    private void layerActions(){final Block b=selected;if(b==null)return;String[] actions={"Move up","Move down","Rename","Lock/Unlock","Hide/Show","Duplicate","Delete"};new AlertDialog.Builder(this).setTitle(name(b)).setItems(actions,(d,w)->{switch(w){case 0:reorder(b,-1);break;case 1:reorder(b,1);break;case 2:rename();break;case 3:toggleLock();break;case 4:toggleHide();break;case 5:duplicate();break;case 6:deleteSelected();break;}}).show();}
    private void reorder(Block b,int delta){pushUndo();int i=page.blocks.indexOf(b),j=i+delta;if(i<0||j<0||j>=page.blocks.size()){ToastMsg("Already at layer edge");return;}Block x=page.blocks.remove(i);page.blocks.add(j,x);save();render();}
    private void deleteSelected(){if(selected==null)return;pushUndo();int id=selected.id;for(Block b:new ArrayList<>(page.blocks))if(b.parent==id)b.parent=0;page.blocks.remove(selected);selected=null;save();render();}

    private void editMenu(){String[] actions={"Duplicate","Copy","Paste","Undo","Redo","Group with…","Ungroup","Lock / Unlock","Hide / Show","Layers / Reorder","Rename","Delete"};new AlertDialog.Builder(this).setTitle(selected==null?"Editor controls":name(selected)).setItems(actions,(d,w)->{switch(w){case 0:duplicate();break;case 1:copy();break;case 2:paste();break;case 3:undoAction();break;case 4:redoAction();break;case 5:group();break;case 6:ungroup();break;case 7:toggleLock();break;case 8:toggleHide();break;case 9:layers();break;case 10:rename();break;case 11:deleteSelected();break;}}).show();}

    @Override boolean move(View v, MotionEvent e, Block b){ if(flag(b,"locked")){if(e.getAction()==MotionEvent.ACTION_UP)ToastMsg("Element is locked");return true;} return super.move(v,e,b); }
    @Override boolean resize(MotionEvent e, View v, Block b, int gravity){ if(flag(b,"locked")){if(e.getAction()==MotionEvent.ACTION_UP)ToastMsg("Element is locked");return true;} return super.resize(e,v,b,gravity); }

    @Override void draw(FrameLayout parent, Block b, int ox, int oy){
        if(flag(b,"hidden"))return;
        FrameLayout h=new FrameLayout(this);h.setX(dp(b.x+ox));h.setY(dp(b.y+oy));h.setAlpha(b.opacity);h.setClipToOutline(true);h.setBackground(fill(b));
        View v;if("Image".equals(b.type)&&!b.uri.isEmpty()){ImageView im=new ImageView(this);im.setImageURI(Uri.parse(b.uri));im.setScaleType(b.crop?ImageView.ScaleType.CENTER_CROP:ImageView.ScaleType.FIT_CENTER);v=im;}else{TextView z=tv(b.text,b.font,b.tc);z.setGravity("Button".equals(b.type)||b.type.startsWith("Tool:")?Gravity.CENTER:Gravity.CENTER_VERTICAL);z.setPadding(dp(8),dp(4),dp(8),dp(4));if("Button".equals(b.type)||b.type.startsWith("Tool:"))z.setTextColor(Color.WHITE);if("Section".equals(b.type)||"Shape".equals(b.type))z.setText("");v=z;}
        h.addView(v,new FrameLayout.LayoutParams(-1,-1));if(selected==b){h.setBackground(selectedFill(b));moveHandle(h,b);resizeHandle(h,b,Gravity.LEFT|Gravity.TOP);resizeHandle(h,b,Gravity.RIGHT|Gravity.TOP);resizeHandle(h,b,Gravity.LEFT|Gravity.BOTTOM);resizeHandle(h,b,Gravity.RIGHT|Gravity.BOTTOM);}h.setOnTouchListener((q,e)->move(h,e,b));parent.addView(h,new FrameLayout.LayoutParams(dp(Math.max(MIN_W,b.w)),dp(Math.max(MIN_H,b.h))));for(Block child:page.blocks)if(child.parent==b.id)draw(parent,child,b.x+ox,b.y+oy);
    }
}
