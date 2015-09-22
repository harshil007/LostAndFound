package com.nirmauni.lostandfound;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LostActivity extends Activity  implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    Spinner spinner;
    private static String GET_URL = "http://nirmalostandfound.site11.com/img_get.php";
    private static String GET_DATA = "http://nirmalostandfound.site11.com/get_data.php";
    String paths[]={"Laptop", "Mobile", "Keys", "Bag", "Others"};
    ListView image_list;
    TextView results;
    private RequestQueue mQueue;
    String[] image_url;
    int[] image_id;
    String item_type;
    Button Yo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);

        setup();

    }

    public void setup(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LostActivity.this, android.R.layout.simple_spinner_item, paths);

        spinner = (Spinner) findViewById(R.id.spinner_lost);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        results = (TextView) findViewById(R.id.tvResults);
        Yo = (Button) findViewById(R.id.bimg);
        Yo.setOnClickListener(this);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();








        //image_url = new String[10];
        //image_id = new int[10];
        //image_url[1]="http://nirmalostandfound.site11.com/Images/3.png";
       //image_url[2]="http://nirmalostandfound.site11.com/Images/2.png";
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bimg:
                item_type = spinner.getSelectedItem().toString();

                get_url(item_type);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.spinner_lost:
                item_type = spinner.getSelectedItem().toString();
                get_url(item_type);
                break;
        }






    }

    private void get_url(String item) {
        results.setText("");
        final ProgressDialog pDialog = new ProgressDialog(LostActivity.this);
        pDialog.setMessage("Connecting to Server...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("Fetching Images");
        pDialog.show();

        JSONObject get = new JSONObject();
        try {
            get.put("item_type",item);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, GET_URL, get, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.dismiss();
                try {
                    VolleyLog.v("Response:%n %s", response.toString(4));

                    image_url = new String[response.length()];
                    image_id = new int[response.length()];
                    for(int i=0;i<response.length();i++){
                        image_url[i]=response.getString(i);
                    }
                    //image_url[0]=response.getString("url");
                    //results.setText(image_url[0]);
                    results.setText(response.length() + " Result(s) Found...");
                    new AsyncTask<Void,Void,Void>(){
                        CustomListAdapter adapter;
                        @Override
                        protected Void doInBackground(Void... params) {
                            adapter=new CustomListAdapter(LostActivity.this, image_url, image_id);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            load_image(adapter);
                        }
                    }.execute();

                    //load_image();
                    Toast.makeText(LostActivity.this, "Success", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LostActivity.this,"Some Connection error maybe", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(req);
    }






    private void load_image(CustomListAdapter adapter){


        image_list=(ListView)findViewById(R.id.image_list_view);
        image_list.setAdapter(adapter);

        image_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                final ProgressDialog p = new ProgressDialog(LostActivity.this);
                p.setMessage("Retreiving data from databse");
                p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                p.setCancelable(false);
                p.setTitle("Fetching Data");
                p.show();
                String Selecteditem = image_url[+position];
                JSONObject url = new JSONObject();
                try {
                    url.put("image_url",Selecteditem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(GET_DATA, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        p.dismiss();
                        try {
                            if(response.getInt("success")==1) {
                                String id = response.getString("id");
                                String item_type = response.getString("item_type");
                                String place = response.getString("place");
                                String date = response.getString("date");
                                String description = response.getString("description");


                                final Dialog dialog = new Dialog(LostActivity.this);
                                dialog.setContentView(R.layout.custom_dialog);
                                dialog.setTitle("!~Details~!");


                                TextView tv_id,tv_item_type,tv_place,tv_date,tv_description;
                                Button dismiss;
                                dismiss = (Button) dialog.findViewById(R.id.bdismiss);

                                tv_id = (TextView) dialog.findViewById(R.id.tv_id);
                                tv_item_type = (TextView) dialog.findViewById(R.id.tv_item);
                                tv_place = (TextView) dialog.findViewById(R.id.tv_place);
                                tv_date = (TextView) dialog.findViewById(R.id.tv_date);
                                tv_description = (TextView) dialog.findViewById(R.id.tv_description);

                                tv_id.setText(id);
                                tv_item_type.setText(item_type);
                                tv_place.setText(place);
                                tv_date.setText(date);
                                tv_description.setText(description);

                                dismiss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                            else{
                                Toast.makeText(LostActivity.this,"Some Connection error maybe", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        p.dismiss();
                        VolleyLog.e("Error: ", error.getMessage());
                        error.printStackTrace();
                        Toast.makeText(LostActivity.this,"Some Connection error maybe", Toast.LENGTH_SHORT).show();
                    }
                });
                mQueue.add(request);
                //Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}