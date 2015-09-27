package com.nirmauni.lostandfound;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class PostLogin extends Activity implements View.OnClickListener{

    Button lost,found,back;
    ActionBar a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/roboto_robotoregular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.postlogin);

        a = getActionBar();
        a.setDisplayHomeAsUpEnabled(true);

        a.show();

        setup();
        /*
        final AlertDialog alertDialog = new AlertDialog.Builder(PostLogin.this).create();
        alertDialog.setTitle("Count");
        alertDialog.setMessage(""+MainActivity.COUNT);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void setup(){
        lost = (Button) findViewById(R.id.lostbutton);
        found = (Button) findViewById(R.id.foundbutton);
        back = (Button) findViewById(R.id.backbutton);
        lost.setOnClickListener(this);
        found.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lostbutton:
                Intent i = new Intent("android.intent.action.LOSTACTIVITY");
                startActivity(i);
                break;
            case R.id.foundbutton:
                Intent ii = new Intent("android.intent.action.FORMACTIVITY");
                startActivity(ii);
                break;
            case R.id.backbutton:
                finish();
                break;
        }
    }
}
