package com.nirmauni.lostandfound;



import android.content.Intent;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class Splash extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);
        TextView n = (TextView) findViewById(R.id.tvNirma);
        TextView u = (TextView) findViewById(R.id.tvUni);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/titlefont.ttf");
        n.setTypeface(typeFace);
        u.setTypeface(typeFace);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    Intent openMain = new Intent("android.intent.action.MAINACTIVITY");
                    startActivity(openMain);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
