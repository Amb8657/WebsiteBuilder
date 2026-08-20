package com.amb8657.websitebuilder;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/** Batch 17: reusable form and submission controls built on the existing editor model. */
public class Batch17FeatureActivity extends Batch16FeatureActivity {
    private SharedPreferences formPrefs;
    private int d(int v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
    private SharedPreferences forms(){if(formPrefs==null)formPrefs=getSharedPreferences("batch17_forms",0);return formPrefs;}
    private void saved(String key,String value){forms().edit().putString(key,value.trim()).apply();save();Toast.makeText(this,"Saved "+key,Toast.LENGTH_SHORT).show();}

    @Override public void onCreate(android.os.Bundle state){formPrefs=getSharedPreferences("batch17_forms",0);super.onCreate(state);}
    @Override void editor(){
        super.editor();
        Button b=btn("Forms"); b.setTextColor(CanvaDesignSystem.TEXT); b.setBackground(gd(CanvaDesignSystem.PANEL_2,10));
        b.setOnClickListener(v->formMenu());
        ViewGroup toolbar=(ViewGroup)root.getChildAt(0); toolbar.addView(b,new LinearLayout.LayoutParams(d(82),d(44)));
    }
    private void setting(String title,String key,String hint){
        EditText e=edit(title,forms().getString(key,hint));
        new AlertDialog.Builder(this).setTitle(title).setView(e).setNegativeButton("Cancel",null).setPositiveButton("Save",(x,w)->saved(key,e.getText().toString())).show();
    }
    /** 1: form name. */ private void formName(){setting("Form name","name","Contact form");}
    /** 2: submit label. */ private void submitLabel(){setting("Submit button label","submit","Submit");}
    /** 3: success message. */ private void success(){setting("Success message","success","Thanks! Your submission was received.");}
    /** 4: required fields policy. */ private void required(){setting("Required fields","required","name,email,message");}
    /** 5: email field label. */ private void email(){setting("Email field","email","Email");}
    /** 6: name field label. */ private void name(){setting("Name field","name_field","Name");}
    /** 7: message field label. */ private void message(){setting("Message field","message_field","Message");}
    /** 8: validation mode. */ private void validation(){setting("Validation mode","validation","required + email");}
    /** 9: submission storage mode. */ private void storage(){setting("Submission storage","storage","local");}
    /** 10: confirmation/redirect target. */ private void confirmation(){setting("Confirmation target","confirmation","success");}
    private void summary(){
        String[] keys={"name","submit","success","required","email","name_field","message_field","validation","storage","confirmation"};
        StringBuilder s=new StringBuilder(); for(String k:keys)s.append(k).append(": ").append(forms().getString(k,"Not configured")).append("\n");
        new AlertDialog.Builder(this).setTitle("Form settings").setMessage(s.toString()).setPositiveButton("Done",null).show();
    }
    private void formMenu(){
        String[] items={"Form name","Submit button label","Success message","Required fields","Email field","Name field","Message field","Validation mode","Submission storage","Confirmation target","Form summary"};
        new AlertDialog.Builder(this).setTitle("Forms & Submissions").setItems(items,(d,w)->{switch(w){case 0:formName();break;case 1:submitLabel();break;case 2:success();break;case 3:required();break;case 4:email();break;case 5:name();break;case 6:message();break;case 7:validation();break;case 8:storage();break;case 9:confirmation();break;default:summary();}}).show();
    }
}
