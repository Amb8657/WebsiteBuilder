package com.amb8657.websitebuilder;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import org.json.*;
import java.util.*;

public class BuilderV3Activity extends Activity {
    static final int PICK_IMAGE=73, MIN_W=60, MIN_H=40;
    final int BG=Color.rgb(18,18,22), PANEL=Color.rgb(29,29,35), WHITE=Color.WHITE, DIM=Color.rgb(160,160,170), ACCENT=Color.rgb(70,92,255);
    LinearLayout root,pages,tools; FrameLayout canvas; ScrollView canvasScroll; SharedPreferences sp;
    ArrayList<Project> projects=new ArrayList<>(); Project project; Page page; Block selected,imageTarget; int nextId=1;
    static class Project{String name;ArrayList<Page> pages=new ArrayList<>();Project(String n){name=n;}}
    static class Page{String name;int bg=Color.WHITE;ArrayList<Block> blocks=new ArrayList<>();Page(String n){name=n;}}
    static class Block{
        int id,parent=0,x=24,y=24,w=300,h=80,bg=Color.TRANSPARENT,bg2=Color.WHITE,tc=Color.DKGRAY,font=18;
        float opacity=1,radius=0; String type,text="",uri="",action="None",target="",fill="solid"; boolean crop=false;
        Block(int i,String t,String s){id=i;type=t;text=s;}
    }
