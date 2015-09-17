package com.nirmauni.lostandfound;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by USER on 13/09/2015.
 */
public class Image_demo extends Activity implements View.OnClickListener{
    private static String IMAGE_URL="";

    TextView img_url;
    NetworkImageView img_view;
    EditText item_type;
    Button submit;


    private static String GET_URL = "http://nirmalostandfound.site11.com/image_get.php";

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_demo);
        setup();

    }

    private void get_url(String item) {

        JSONObject get = new JSONObject();
        try {
            get.put("item_type",item);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(GET_URL, get,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            IMAGE_URL=response.getString("url");
                            img_url.setText(IMAGE_URL);
                            load_image();
                            Toast.makeText(Image_demo.this, "Success", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
                Toast.makeText(Image_demo.this,"Some Connection error maybe", Toast.LENGTH_SHORT).show();

            }
        });
        mQueue.add(req);
    }

    private void setup() {
        img_url = (TextView) findViewById(R.id.img_url);
        img_view = (NetworkImageView) findViewById(R.id.imgAvatar);
        submit = (Button) findViewById(R.id.sub);
        item_type = (EditText) findViewById(R.id.etItem);
        submit.setOnClickListener(this);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
    }

    private void load_image(){
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        ImageLoader imageLoader = new ImageLoader(mQueue, imageCache);
        img_view.setImageUrl(IMAGE_URL,imageLoader);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sub:
                String item = item_type.getText().toString();
                get_url(item);
                break;


        }
    }
}
