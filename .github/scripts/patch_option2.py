from pathlib import Path
p=Path('app/src/main/java/com/amb8657/websitebuilder/BuilderV3Activity.java')
s=p.read_text()
def between(a,b,r):
    global s
    i=s.index(a); j=s.index(b,i); s=s[:i]+r+s[j:]
s=s.replace('final int BG=Color.rgb(18,18,22), PANEL=Color.rgb(29,29,35), WHITE=Color.WHITE, DIM=Color.rgb(160,160,170), ACCENT=Color.rgb(70,92,255);','final int BG=Color.rgb(12,15,22), PANEL=Color.rgb(24,29,39), WHITE=Color.WHITE, DIM=Color.rgb(164,171,186), ACCENT=Color.rgb(79,102,255);')
s=s.replace('float opacity=1,radius=0; String type,text="",uri="",action="None",target="",fill="solid"; boolean crop=false;','float opacity=1,radius=0; String type,text="",uri="",action="None",target="",fill="solid",animation="None"; boolean crop=false;')
s=s.replace('x.crop=o.optBoolean("crop",false);q.blocks.add(x);','x.crop=o.optBoolean("crop",false);x.animation=o.optString("animation","None");q.blocks.add(x);')
s=s.replace('.put("fill",b.fill).put("crop",b.crop);bs.put(o);','.put("fill",b.fill).put("crop",b.crop).put("animation",b.animation);bs.put(o);')
between('    void splash(){','    void load(){','''    void splash(){base();root.setGravity(Gravity.CENTER);LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setGravity(Gravity.CENTER);c.setPadding(dp(24),dp(24),dp(24),dp(24));TextView l=tv("G",52,WHITE);l.setGravity(Gravity.CENTER);l.setTypeface(Typeface.DEFAULT,Typeface.BOLD);l.setBackground(gd(ACCENT,28));c.addView(l,new LinearLayout.LayoutParams(dp(104),dp(104)));TextView t=tv("Website Builder",25,WHITE);t.setGravity(Gravity.CENTER);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);c.addView(t,new LinearLayout.LayoutParams(-1,dp(62)));TextView x=tv("Create  •  Design  •  Publish",14,DIM);x.setGravity(Gravity.CENTER);c.addView(x,new LinearLayout.LayoutParams(-1,dp(36)));ProgressBar q=new ProgressBar(this);c.addView(q,new LinearLayout.LayoutParams(dp(30),dp(30)));root.addView(c,new LinearLayout.LayoutParams(-1,-2));new Handler().postDelayed(this::dashboard,1800);}

''')
s=s.replace('new String[]{"Text","Image","Button","Section","Spacer","Shape","Tool"}','new String[]{"Text","Image","Button","Section","Shape","Tool"}')
s=s.replace('Button tc=btn("Text colour");box.addView(tc);','Button tc=btn("Text colour");box.addView(tc);Button an=btn("Animation: "+b.animation);box.addView(an);an.setOnClickListener(v->animationDialog(b,an));')
s=s.replace('v.setBackground(fill(b));v.setAlpha(b.opacity);FrameLayout.LayoutParams lp=','v.setBackground(fill(b));v.setAlpha(b.opacity);applyAnimation(v,b);FrameLayout.LayoutParams lp=')
s=s.replace('    void radius(Block b){','''    void animationDialog(Block b,Button label){String[] a={"None","Fade In","Slide Up","Slide Left","Scale In"};new AlertDialog.Builder(this).setTitle("Animation / Interaction").setItems(a,(d,w)->{b.animation=a[w];label.setText("Animation: "+b.animation);save();}).show();}
    void applyAnimation(View v,Block b){if("Fade In".equals(b.animation)){v.setAlpha(0f);v.animate().alpha(b.opacity).setDuration(450).start();}else if("Slide Up".equals(b.animation)){v.setTranslationY(dp(30));v.animate().translationY(0).setDuration(450).start();}else if("Slide Left".equals(b.animation)){v.setTranslationX(dp(30));v.animate().translationX(0).setDuration(450).start();}else if("Scale In".equals(b.animation)){v.setScaleX(.75f);v.setScaleY(.75f);v.animate().scaleX(1).scaleY(1).setDuration(450).start();}}
    void radius(Block b){''')
p.write_text(s)
