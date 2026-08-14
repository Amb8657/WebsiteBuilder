package com.amb8657.websitebuilder;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

/** Option #2: dedicated project-setup screen before entering the visual editor. */
public class ProjectSetupActivity extends Activity {
    private int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density + .5f); }
    private GradientDrawable bg(int color, float radius) { GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp((int)radius)); return d; }
    private TextView text(String s,float size,int color){ TextView v=new TextView(this); v.setText(s); v.setTextSize(size); v.setTextColor(color); return v; }
    @Override public void onCreate(Bundle state){
        super.onCreate(state);
        final int dark=Color.rgb(19,18,23), soft=Color.rgb(30,29,35), white=Color.WHITE;
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(20),dp(18),dp(20),dp(18)); root.setBackgroundColor(dark);
        TextView back=text("‹",32,white); back.setGravity(Gravity.CENTER); root.addView(back,new LinearLayout.LayoutParams(dp(52),dp(52))); back.setOnClickListener(v->finish());
        TextView title=text("Create your website",28,white); title.setTypeface(Typeface.DEFAULT_BOLD); root.addView(title,new LinearLayout.LayoutParams(-1,dp(55)));
        root.addView(text("Choose how you want to start. You can change everything later.",15,Color.rgb(150,148,160)),new LinearLayout.LayoutParams(-1,dp(55)));
        Button blank=option("＋  Start from Scratch","Blank canvas • full visual control",soft,white); Button template=option("▧  Use a Template","Start with a ready-made structure",soft,white);
        root.addView(blank,new LinearLayout.LayoutParams(-1,dp(100))); root.addView(template,new LinearLayout.LayoutParams(-1,dp(100)));
        root.addView(new Space(this),new LinearLayout.LayoutParams(1,0,1));
        TextView note=text("MAHA Builder • Option #2",13,Color.rgb(150,148,160)); note.setGravity(Gravity.CENTER); root.addView(note,new LinearLayout.LayoutParams(-1,dp(35)));
        View.OnClickListener go=v->startActivity(new Intent(this,BuilderV2Activity.class)); blank.setOnClickListener(go); template.setOnClickListener(go); setContentView(root);
    }
    private Button option(String title,String sub,int background,int color){ Button b=new Button(this); b.setAllCaps(false); b.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL); b.setText(title+"\n"+sub); b.setTextSize(16); b.setTextColor(color); b.setPadding(dp(18),0,dp(18),0); b.setBackground(bg(background,14)); return b; }
}
