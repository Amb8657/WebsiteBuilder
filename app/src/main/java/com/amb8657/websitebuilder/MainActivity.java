package com.amb8657.websitebuilder;

import android.app.*;import android.os.*;import android.graphics.Color;import android.view.*;import android.widget.*;import java.util.*;

public class MainActivity extends Activity {
 LinearLayout root,canvas,side; TextView title; ArrayList<View> elements=new ArrayList<>();
 int purple=Color.rgb(103,80,164);
 @Override public void onCreate(Bundle b){super.onCreate(b); build();}
 TextView t(String s,int z){TextView v=new TextView(this);v.setText(s);v.setTextSize(z);v.setTextColor(Color.DKGRAY);v.setPadding(18,12,18,12);return v;}
 Button btn(String s){Button b=new Button(this);b.setText(s);return b;}
 void build(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(Color.WHITE);
  LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL); title=t("Website Builder",20);bar.addView(title,new LinearLayout.LayoutParams(0,70,1));Button preview=btn("Preview");bar.addView(preview,new LinearLayout.LayoutParams(-2,65));root.addView(bar);
  LinearLayout body=new LinearLayout(this);side=new LinearLayout(this);side.setOrientation(LinearLayout.VERTICAL);side.setPadding(8,8,8,8);side.addView(t("ELEMENTS",12));
  String[] tools={"Text","Heading","Button","Image","Section","Spacer"}; for(String x:tools){Button q=btn("+ "+x);q.setOnClickListener(v->addElement(x));side.addView(q,new LinearLayout.LayoutParams(-1,60));}
  Button clear=btn("Clear");clear.setOnClickListener(v->{canvas.removeAllViews();elements.clear();});side.addView(clear);
  body.addView(side,new LinearLayout.LayoutParams(230,-1)); ScrollView sv=new ScrollView(this);canvas=new LinearLayout(this);canvas.setOrientation(LinearLayout.VERTICAL);canvas.setPadding(25,25,25,80);canvas.setBackgroundColor(Color.rgb(245,245,248));sv.addView(canvas);body.addView(sv,new LinearLayout.LayoutParams(0,-1,1));root.addView(body,new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
  preview.setOnClickListener(v->showPreview()); addElement("Heading");addElement("Text");addElement("Button");
 }
 void addElement(String type){TextView v=t(type.equals("Heading")?"Your Website Heading":type.equals("Text")?"Start building your website here.":type.equals("Button")?"Click Me":type, type.equals("Heading")?25:16);v.setGravity(Gravity.CENTER_VERTICAL);v.setBackgroundColor(Color.WHITE);v.setElevation(3);v.setOnClickListener(x->edit(v,type));canvas.addView(v,new LinearLayout.LayoutParams(-1,type.equals("Spacer")?80:90));elements.add(v);}
 void edit(TextView v,String type){final EditText e=new EditText(this);e.setText(v.getText());e.setHint("Content");new AlertDialog.Builder(this).setTitle("Edit "+type).setView(e).setPositiveButton("Save",(d,w)->v.setText(e.getText().toString())).setNegativeButton("Cancel",null).show();}
 void showPreview(){LinearLayout p=new LinearLayout(this);p.setOrientation(LinearLayout.VERTICAL);p.setPadding(30,30,30,30);for(View x:elements){TextView n=(TextView)x;p.addView(t(n.getText().toString(),n.getTextSize()/getResources().getDisplayMetrics().scaledDensity));}new AlertDialog.Builder(this).setTitle("Website Preview").setView(new ScrollView(this){{addView(p);}}).setPositiveButton("Close",null).show();}
}
