package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/** Batch 9: multi-selection and precise multi-element spacing/size controls. */
public class Batch9FeatureActivity extends Batch8FeatureActivity {
    private int d(int v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private void msg(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }

    @Override void editor() {
        super.editor();
        android.widget.Button multi = btn("Multi");
        multi.setTextColor(CanvaDesignSystem.TEXT);
        multi.setBackground(gd(CanvaDesignSystem.PANEL_2, 10));
        multi.setOnClickListener(v -> multiMenu());
        android.view.ViewGroup toolbar = (android.view.ViewGroup) root.getChildAt(0);
        toolbar.addView(multi, new LinearLayout.LayoutParams(d(78), d(44)));
    }

    private ArrayList<Block> topBlocks() {
        ArrayList<Block> list = new ArrayList<>();
        if (page != null) for (Block b : page.blocks) if (b.parent == 0) list.add(b);
        return list;
    }

    private ArrayList<Block> chooseBlocks() {
        final ArrayList<Block> all = topBlocks();
        if (all.size() < 2) { msg("Add at least two top-level elements"); return null; }
        final boolean[] checked = new boolean[all.size()];
        String[] labels = new String[all.size()];
        for (int i = 0; i < all.size(); i++) labels[i] = all.get(i).type + " " + all.get(i).id;
        final ArrayList<Block> result = new ArrayList<>();
        new AlertDialog.Builder(this).setTitle("Select elements")
                .setMultiChoiceItems(labels, checked, (d, which, isChecked) -> checked[which] = isChecked)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Apply", (d, w) -> {
                    for (int i = 0; i < all.size(); i++) if (checked[i]) result.add(all.get(i));
                    if (result.size() < 2) { msg("Select at least two elements"); return; }
                    multiActionMenu(result);
                }).show();
        return result;
    }

    /** 1: explicit multi-selection entry point. */
    private void multiSelect() { chooseBlocks(); }

    /** 2: distribute selected elements evenly by their horizontal centers. */
    private void distributeHorizontal(ArrayList<Block> list) {
        if (list.size() < 3) { msg("Select three or more elements"); return; }
        pushHistory();
        Collections.sort(list, Comparator.comparingInt(b -> b.x));
        int first = list.get(0).x, last = list.get(list.size()-1).x;
        int step = (last - first) / (list.size()-1);
        for (int i=0;i<list.size();i++) list.get(i).x = first + step*i;
        changedMulti("Distributed horizontally");
    }

    /** 3: distribute selected elements evenly by their vertical positions. */
    private void distributeVertical(ArrayList<Block> list) {
        if (list.size() < 3) { msg("Select three or more elements"); return; }
        pushHistory();
        Collections.sort(list, Comparator.comparingInt(b -> b.y));
        int first = list.get(0).y, last = list.get(list.size()-1).y;
        int step = (last - first) / (list.size()-1);
        for (int i=0;i<list.size();i++) list.get(i).y = first + step*i;
        changedMulti("Distributed vertically");
    }

    /** 4: make selected elements the same width as the first selected element. */
    private void matchWidth(ArrayList<Block> list) { pushHistory(); int w=list.get(0).w; for(Block b:list)b.w=w; changedMulti("Matched width"); }

    /** 5: make selected elements the same height as the first selected element. */
    private void matchHeight(ArrayList<Block> list) { pushHistory(); int h=list.get(0).h; for(Block b:list)b.h=h; changedMulti("Matched height"); }

    /** 6: align all selected elements to the first selected element's X position. */
    private void alignToFirstX(ArrayList<Block> list) { pushHistory(); int x=list.get(0).x; for(Block b:list)b.x=x; changedMulti("Aligned to first X"); }

    /** 7: align all selected elements to the first selected element's Y position. */
    private void alignToFirstY(ArrayList<Block> list) { pushHistory(); int y=list.get(0).y; for(Block b:list)b.y=y; changedMulti("Aligned to first Y"); }

    /** 8: nudge all selected elements 8dp left. */
    private void nudgeLeft(ArrayList<Block> list) { pushHistory(); int n=d(8); for(Block b:list)b.x=Math.max(0,b.x-n); changedMulti("Nudged left 8dp"); }

    /** 9: nudge all selected elements 8dp right. */
    private void nudgeRight(ArrayList<Block> list) { pushHistory(); int n=d(8); for(Block b:list)b.x+=n; changedMulti("Nudged right 8dp"); }

    /** 10: nudge all selected elements 8dp down; repeatable for precise placement. */
    private void nudgeDown(ArrayList<Block> list) { pushHistory(); int n=d(8); for(Block b:list)b.y+=n; changedMulti("Nudged down 8dp"); }

    private void pushHistory() {
        // Batch 8 persists each concrete change; the existing V4 history is intentionally reused.
        save();
    }

    private void changedMulti(String text) { save(); render(); msg(text); }

    private void multiActionMenu(ArrayList<Block> list) {
        String[] actions={"Distribute horizontally","Distribute vertically","Match width","Match height","Align X to first","Align Y to first","Nudge left 8dp","Nudge right 8dp","Nudge down 8dp"};
        new AlertDialog.Builder(this).setTitle(list.size()+" selected").setItems(actions,(d,w)->{
            switch(w){case 0:distributeHorizontal(list);break;case 1:distributeVertical(list);break;case 2:matchWidth(list);break;case 3:matchHeight(list);break;case 4:alignToFirstX(list);break;case 5:alignToFirstY(list);break;case 6:nudgeLeft(list);break;case 7:nudgeRight(list);break;case 8:nudgeDown(list);break;default:break;}
        }).show();
    }

    private void multiMenu() {
        String[] actions={"Select multiple elements","Distribute horizontally","Distribute vertically","Match width","Match height","Align X to first","Align Y to first","Nudge left 8dp","Nudge right 8dp","Nudge down 8dp"};
        new AlertDialog.Builder(this).setTitle("Multi-element tools").setItems(actions,(d,w)->{
            if(w==0){multiSelect();return;}
            final ArrayList<Block> list=chooseBlocks();
            if(list==null)return;
            switch(w){case 1:distributeHorizontal(list);break;case 2:distributeVertical(list);break;case 3:matchWidth(list);break;case 4:matchHeight(list);break;case 5:alignToFirstX(list);break;case 6:alignToFirstY(list);break;case 7:nudgeLeft(list);break;case 8:nudgeRight(list);break;case 9:nudgeDown(list);break;default:break;}
        }).show();
    }
}
