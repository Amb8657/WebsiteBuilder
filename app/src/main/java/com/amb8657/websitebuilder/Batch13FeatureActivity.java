package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/** Batch 13: advanced visual-editor object management and canvas controls. */
public class Batch13FeatureActivity extends Batch12FeatureActivity {
    private final Set<Integer> multi = new LinkedHashSet<>();
    private Block clipboard;
    private boolean snapEnabled;
    private boolean panEnabled;
    private SharedPreferences prefs13;
    private float panX, panY;

    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private void msg(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
    private boolean ok() { if(project==null || page==null){msg("Open a website first");return false;} return true; }
    private boolean selectedOk() { if(!ok())return false; if(selected==null){msg("Select an element first");return false;} return true; }
    private String key(String suffix){return "b:"+(selected==null?0:selected.id)+":"+suffix;}
    private SharedPreferences p(){ if(prefs13==null)prefs13=getSharedPreferences("batch13_editor",0); return prefs13; }
    private void changed(String s){save();render();msg(s);}

    @Override public void onCreate(android.os.Bundle state){prefs13=getSharedPreferences("batch13_editor",0);snapEnabled=prefs13.getBoolean("snap",false);super.onCreate(state);}

    @Override void editor(){
        super.editor();
        ViewGroup toolbar=(ViewGroup)root.getChildAt(0);
        Button advanced=btn("Advanced");
        advanced.setTextColor(CanvaDesignSystem.TEXT);
        advanced.setBackground(gd(CanvaDesignSystem.PANEL_2,10));
        advanced.setOnClickListener(v->advancedMenu());
        toolbar.addView(advanced,new LinearLayout.LayoutParams(d(96),d(44)));
    }

    /** 1: layers/object hierarchy panel. */
    private void layersPanel(){
        if(!ok())return;
        StringBuilder s=new StringBuilder();
        for(Block b:page.blocks){String name=p().getString("b:"+b.id+":name",b.type+" "+b.id);s.append(b.parent==0?"▣ ":"  ↳ ").append(name).append(b==selected?"  [SELECTED]":"").append("\n");}
        new AlertDialog.Builder(this).setTitle("Layers / Object hierarchy").setMessage(s.length()==0?"No elements yet":s.toString()).setPositiveButton("Done",null).show();
    }

    /** 2: multi-select entry point for advanced object operations. */
    private ArrayList<Block> chooseBlocks(){
        ArrayList<Block> all=new ArrayList<>(); if(page!=null)for(Block b:page.blocks)if(b.parent==0)all.add(b);
        if(all.size()<2){msg("Add at least two top-level elements");return null;}
        boolean[] checked=new boolean[all.size()]; for(int i=0;i<all.size();i++)checked[i]=multi.contains(all.get(i).id);
        String[] labels=new String[all.size()];for(int i=0;i<all.size();i++)labels[i]=p().getString("b:"+all.get(i).id+":name",all.get(i).type+" "+all.get(i).id);
        final ArrayList<Block> result=new ArrayList<>();
        new AlertDialog.Builder(this).setTitle("Multi-select").setMultiChoiceItems(labels,checked,(d,w,c)->checked[w]=c).setNegativeButton("Cancel",null).setPositiveButton("Apply",(d,w)->{multi.clear();for(int i=0;i<all.size();i++)if(checked[i]){multi.add(all.get(i).id);result.add(all.get(i));}msg(result.size()+" elements selected");}).show();
        return result;
    }

    /** 3: group selected top-level elements into a real Section container. */
    private void groupSelected(){
        if(!ok()||multi.size()<2){msg("Select at least two elements first");return;}
        ArrayList<Block> list=new ArrayList<>();for(Integer id:multi){Block b=find(id);if(b!=null&&b.parent==0)list.add(b);}if(list.size()<2){msg("Select at least two top-level elements");return;}
        int minX=Integer.MAX_VALUE,minY=Integer.MAX_VALUE,maxX=0,maxY=0;for(Block b:list){minX=Math.min(minX,b.x);minY=Math.min(minY,b.y);maxX=Math.max(maxX,b.x+b.w);maxY=Math.max(maxY,b.y+b.h);}
        Block group=new Block(nextId++,"Section","");group.x=minX;group.y=minY;group.w=Math.max(MIN_W,maxX-minX);group.h=Math.max(MIN_H,maxY-minY);page.blocks.add(0,group);
        for(Block b:list){b.parent=group.id;b.x-=minX;b.y-=minY;}
        multi.clear();selected=group;changed("Grouped "+list.size()+" elements");
    }

    /** 4: ungroup the selected Section/container while preserving absolute positions. */
    private void ungroupSelected(){
        if(!selectedOk())return;Block group=selected;if(!"Section".equals(group.type)){msg("Select a Section group");return;}
        for(Block b:page.blocks)if(b.parent==group.id){b.x+=group.x;b.y+=group.y;b.parent=0;}
        page.blocks.remove(group);selected=null;multi.clear();changed("Ungrouped elements");
    }

    /** 5: duplicate selected element with a safe offset. */
    private void duplicateSelected(){
        if(!selectedOk())return;Block s=selected;Block b=cloneBlock(s);b.id=nextId++;b.parent=s.parent;b.x+=16;b.y+=16;page.blocks.add(b);selected=b;changed("Element duplicated");
    }

    /** 6: copy/paste selected element using an in-app clipboard. */
    private void copyPaste(){
        if(selectedOk())clipboard=cloneBlock(selected);
        if(clipboard==null){msg("Nothing copied");return;}
        Block b=cloneBlock(clipboard);b.id=nextId++;b.parent=0;b.x+=24;b.y+=24;page.blocks.add(b);selected=b;changed("Copied and pasted element");
    }

    private Block cloneBlock(Block s){Block b=new Block(s.id,s.type,s.text);b.parent=s.parent;b.x=s.x;b.y=s.y;b.w=s.w;b.h=s.h;b.bg=s.bg;b.bg2=s.bg2;b.tc=s.tc;b.font=s.font;b.opacity=s.opacity;b.radius=s.radius;b.uri=s.uri;b.action=s.action;b.target=s.target;b.fill=s.fill;b.crop=s.crop;b.animation=s.animation;return b;}

    /** 7: lock/unlock selected object and enforce the lock during drag. */
    private void toggleLock(){
        if(!selectedOk())return;boolean n=!p().getBoolean(key("locked"),false);p().edit().putBoolean(key("locked"),n).apply();save();msg(n?"Element locked":"Element unlocked");
    }

    /** 8: hide/show selected object without deleting it. */
    private void toggleHide(){
        if(!selectedOk())return;boolean n=!p().getBoolean(key("hidden"),false);p().edit().putBoolean(key("hidden"),n).apply();selected.opacity=n?0f:1f;changed(n?"Element hidden":"Element shown");
    }

    /** 9: snap-to-grid toggle and rulers/zoom/pan canvas controls. */
    private void canvasControls(){
        String[] a={"Toggle snap-to-grid","Rulers / coordinates","Zoom in 10%","Zoom out 10%","Reset zoom","Pan canvas left","Pan canvas right","Pan canvas up","Pan canvas down"};
        new AlertDialog.Builder(this).setTitle("Canvas controls").setItems(a,(d,w)->{switch(w){case 0:snapEnabled=!snapEnabled;p().edit().putBoolean("snap",snapEnabled).apply();msg("Snap to grid: "+(snapEnabled?"ON":"OFF"));break;case 1:rulers();break;case 2:zoom(1.1f);break;case 3:zoom(.9f);break;case 4:zoom(1f);break;case 5:pan(-24,0);break;case 6:pan(24,0);break;case 7:pan(0,-24);break;case 8:pan(0,24);break;default:break;}}).show();
    }
    private void rulers(){if(!ok())return;StringBuilder x=new StringBuilder("X: ");for(int i=0;i<=10;i++)x.append(i*100).append(i==10?"":"  |  ");new AlertDialog.Builder(this).setTitle("Rulers / coordinates").setMessage(x+"\n\nSelected: "+(selected==null?"none":selected.x+"dp, "+selected.y+"dp")).setPositiveButton("Done",null).show();}
    private void zoom(float factor){float z=p().getFloat("zoom",1f);z=factor==1f?1f:Math.max(.5f,Math.min(1.5f,z*factor));p().edit().putFloat("zoom",z).apply();if(root!=null){root.setScaleX(z);root.setScaleY(z);}msg("Canvas zoom: "+Math.round(z*100)+"%");}
    private void pan(float dx,float dy){panX+=d(dx);panY+=d(dy);if(canvas!=null){canvas.setTranslationX(panX);canvas.setTranslationY(panY);}panEnabled=true;msg("Canvas panned");}

    /** 10: contextual advanced properties and object rename. */
    private void renameSelected(){
        if(!selectedOk())return;EditText e=edit("Element name",p().getString(key("name"),selected.type+" "+selected.id));
        new AlertDialog.Builder(this).setTitle("Rename element").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{String n=e.getText().toString().trim();if(!n.isEmpty())p().edit().putString(key("name"),n).apply();save();layersPanel();}).show();
    }

    @Override boolean move(View v,MotionEvent e,Block b){
        if(p().getBoolean("b:"+b.id+":locked",false)){if(e.getAction()==MotionEvent.ACTION_UP)msg("Element is locked");return true;}
        boolean result=super.move(v,e,b);
        if(snapEnabled && (e.getAction()==MotionEvent.ACTION_MOVE||e.getAction()==MotionEvent.ACTION_UP)){
            int grid=d(8);b.x=Math.max(0,Math.round(b.x/(float)grid)*grid);b.y=Math.max(0,Math.round(b.y/(float)grid)*grid);
            if(e.getAction()==MotionEvent.ACTION_UP){save();render();}
        }
        return result;
    }

    @Override public boolean onKeyDown(int keyCode,KeyEvent event){
        if(selected!=null){
            if(keyCode==KeyEvent.KEYCODE_DEL){page.blocks.remove(selected);selected=null;multi.clear();changed("Element deleted");return true;}
            if(event.isCtrlPressed() && keyCode==KeyEvent.KEYCODE_D){duplicateSelected();return true;}
        }
        return super.onKeyDown(keyCode,event);
    }

    private void advancedMenu(){
        String[] a={"Layers / hierarchy","Multi-select","Group selected","Ungroup selected","Duplicate selected","Copy + paste selected","Lock / unlock selected","Hide / show selected","Canvas controls","Rename selected"};
        new AlertDialog.Builder(this).setTitle("Advanced editor").setItems(a,(d,w)->{switch(w){case 0:layersPanel();break;case 1:chooseBlocks();break;case 2:groupSelected();break;case 3:ungroupSelected();break;case 4:duplicateSelected();break;case 5:copyPaste();break;case 6:toggleLock();break;case 7:toggleHide();break;case 8:canvasControls();break;case 9:renameSelected();break;default:break;}}).show();
    }
}
