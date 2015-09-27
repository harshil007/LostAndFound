package com.nirmauni.lostandfound;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends Activity implements View.OnClickListener {

    Button login;
    EditText user, pass;
    String username, password;

    public static int COUNT = 0;

    private static final String LOGIN_URL = "http://nirmalostandfound.site11.com/login.php";    //url of your php file
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    private RequestQueue mQueue;
    ActionBar a;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/roboto_robotoregular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }

        setContentView(R.layout.activity_main);


        a = getActionBar();
        a.setDisplayHomeAsUpEnabled(true);

        a.show();


        setup();
        login.setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mainscreen, menu);
        return true;
    }

    public void setup() {
        login = (Button) findViewById(R.id.bLogin);
        user = (EditText) findViewById(R.id.etUsername);
        pass = (EditText) findViewById(R.id.etPassword);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogin:
                username = user.getText().toString();
                password = pass.getText().toString();
                if (username.equals("admin") && password.equals("admin")) {
                    Intent i = new Intent("android.intent.action.POSTLOGIN");
                    startActivity(i);
                    Toast.makeText(MainActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                } else {
                    jsonconnect(username,password);

                    break;
                }

                break;
        }
    }

    private void jsonconnect(String username, String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Signing in...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("Connecting to Server");
        pDialog.show();


       /* Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        new JSONObject(params)*/

        JSONObject jobj = new JSONObject();
        try {
            jobj.put("username",username);
            jobj.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest req = new JsonObjectRequest(LOGIN_URL, jobj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            pDialog.dismiss();
                            int i = response.getInt("success");

                            String msg = response.getString("message");
                            if(i==1){
                                COUNT = response.getInt("count");
                                Intent ii = new Intent("android.intent.action.POSTLOGIN");

                                finish();
                                startActivity(ii);
                            }

                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
                Toast.makeText(MainActivity.this,"Some Connection error maybe", Toast.LENGTH_SHORT).show();

            }
        });
        mQueue.add(req);
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Error!");
        alertDialog.setMessage("Die hard");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        switch (item.getItemId()) {



            case R.id.aboutUs:
                alertDialog.setTitle("About us..!!");
                alertDialog.setMessage("Harshil Laheri : 13BCE049\nDishank Mehta : 13BCE054");
                alertDialog.show();
                break;

            case R.id.prefrences:
                alertDialog.setTitle("Prefrences..!!");
                alertDialog.setMessage("Coming soon...");
                alertDialog.show();
                break;

            case R.id.exit:
                finish();
                break;




        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }
}


