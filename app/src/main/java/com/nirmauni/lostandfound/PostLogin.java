package com.nirmauni.lostandfound;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




public class PostLogin extends Activity {

    Button lost,found,back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postlogin);
        setup();
        final AlertDialog alertDialog = new AlertDialog.Builder(PostLogin.this).create();
        alertDialog.setTitle("Count");
        alertDialog.setMessage(""+MainActivity.COUNT);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void setup(){
        lost = (Button) findViewById(R.id.lostbutton);
        found = (Button) findViewById(R.id.foundbutton);
        back = (Button) findViewById(R.id.backbutton);
    }



    public void onforlostButtonClicked(View view) {
        Intent i = new Intent("android.intent.action.LOSTACTIVITY");
        startActivity(i);
    }


    public void onforfoundButtonClicked(View view) {
        Intent i = new Intent("android.intent.action.FORMACTIVITY");
        startActivity(i);
    }

    public void onbackButtonClicked(View view) {
        finish();
    }
}
