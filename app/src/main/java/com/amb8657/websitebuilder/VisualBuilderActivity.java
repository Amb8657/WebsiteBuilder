package com.amb8657.websitebuilder;

import android.app.*;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.util.*;

public class VisualBuilderActivity extends Activity {
    final int BG=Color.rgb(19,18,23), SOFT=Color.rgb(31,30,36), TEXT=Color.rgb(237,235,243), ACCENT=Color.rgb(124,92,255);
    LinearLayout root, list;
    ArrayList<String> projects=new ArrayList<>();
    int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    GradientDrawable bg(int c){GradientDrawable g=new GradientDrawable();g.setColor(c);g.setCornerRadius(dp(10));return g;}
    Button button(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextColor(TEXT);b.setBackground(bg(SOFT));return b;}
    @Override public void onCreate(Bundle b){super.onCreate(b);showProjects();}
    void showProjects(){
        root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(BG);setContentView(root);
        TextView title=new TextView(this);title.setText("P   Website Builder");title.setTextSize(22);title.setTextColor(TEXT);title.setGravity(Gravity.CENTER_VERTICAL);root.addView(title,new LinearLayout.LayoutParams(-1,dp(80)));
        TextView p=new TextView(this);p.setText("Projects");p.setTextSize(17);p.setTextColor(Color.LTGRAY);p.setPadding(dp(18),0,0,0);root.addView(p,new LinearLayout.LayoutParams(-1,dp(45)));
        ScrollView scroll=new ScrollView(this);list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);list.setPadding(dp(16),dp(8),dp(16),dp(20));scroll.addView(list);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        Button n=button("+  New Project");n.setTextSize(16);n.setTextColor(Color.WHITE);n.setBackground(bg(ACCENT));list.addView(n,new LinearLayout.LayoutParams(-1,dp(58)));n.setOnClickListener(v->newProject());
        for(String name:projects)addProjectRow(name);
        if(projects.isEmpty()){projects.add("My Website");addProjectRow("My Website");}
    }
    void addProjectRow(String name){LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);TextView t=new TextView(this);t.setText(name);t.setTextSize(16);t.setTextColor(TEXT);row.addView(t,new LinearLayout.LayoutParams(0,dp(62),1));Button o=button("Open");row.addView(o,new LinearLayout.LayoutParams(dp(75),dp(44)));list.addView(row,new LinearLayout.LayoutParams(-1,dp(72)));o.setOnClickListener(v->showEditor(name));t.setOnClickListener(v->showEditor(name));}
    void newProject(){EditText e=new EditText(this);e.setHint("Project name");new AlertDialog.Builder(this).setTitle("New Project").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Create",(d,w)->{String n=e.getText().toString().trim();if(n.isEmpty())n="My Website";projects.add(0,n);showEditor(n);}).show();}
    void showEditor(String name){
        root.removeAllViews();
        LinearLayout head=new LinearLayout(this);head.setBackgroundColor(BG);head.setGravity(Gravity.CENTER_VERTICAL);Button back=button("‹ Projects");head.addView(back,new LinearLayout.LayoutParams(dp(105),dp(55)));TextView t=new TextView(this);t.setText(name);t.setTextSize(18);t.setTextColor(TEXT);head.addView(t,new LinearLayout.LayoutParams(0,dp(55),1));root.addView(head);
        back.setOnClickListener(v->showProjects());
        LinearLayout tools=new LinearLayout(this);tools.setPadding(dp(8),dp(5),dp(8),dp(5));String[] a={"+ Heading","+ Text","+ Image","+ Button"};for(String s:a){Button b=button(s);tools.addView(b,new LinearLayout.LayoutParams(0,dp(44),1));}root.addView(tools);
        TextView notice=new TextView(this);notice.setText("Visual editor foundation\n\nSelect an element to edit its properties. Drag/resize support is being wired into this canvas.");notice.setTextColor(Color.DKGRAY);notice.setTextSize(16);notice.setGravity(Gravity.CENTER);notice.setBackgroundColor(Color.WHITE);root.addView(notice,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout bottom=new LinearLayout(this);bottom.setBackgroundColor(SOFT);for(String s:new String[]{"Pages","Style","Preview","Save"})bottom.addView(button(s),new LinearLayout.LayoutParams(0,dp(58),1));root.addView(bottom);
    }
}
