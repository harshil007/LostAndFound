package com.nirmauni.lostandfound;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.jibble.simpleftp.SimpleFTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;




public class FormActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner item_spinner;
    String[] paths = { "Laptop", "Mobile", "Keys", "Bag", "Others"};
    Button Bbrowse,Bsubmit;
    EditText etDate,etPlace,etDetails,etName,etRoll_no;
    TextView tvImage;

    public static InputStream is=null;
    static int REQUEST_CAMERA;


    static int SELECT_IMAGE;

    private static int COUNT;

    private static String image_name=null;
    private static String image_url="";

    private static String item_type=null,place=null,details=null,name=null,roll_no=null;
    private static java.sql.Date sqldate;
    private static Date date;

    private static boolean verify_flag=false;
    private static final String INSERT_URL = "http://nirmalostandfound.site11.com/insert.php";
    private static final String COUNT_URL = "http://nirmalostandfound.site11.com/get_count.php";


    private ProgressDialog pDialog;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        setup();
        get_count();

    }



    private void get_count() {

        JSONObject abc = new JSONObject();
        JsonObjectRequest count_req = new JsonObjectRequest(COUNT_URL,abc,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));

                            COUNT = response.getInt("count");

                            String msg = ""+COUNT;
                            Toast.makeText(FormActivity.this,msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();


            }
        });
        mQueue.add(count_req);
    }

    public void setup(){
        Bbrowse = (Button)findViewById(R.id.browse_button);
        Bsubmit = (Button)findViewById(R.id.bSubmit);
        etPlace = (EditText)findViewById(R.id.editText_place);
        etDetails = (EditText)findViewById(R.id.editText_details);
        etName = (EditText)findViewById(R.id.editText_fname);
        etRoll_no = (EditText)findViewById(R.id.editText_no);
        tvImage = (TextView)findViewById(R.id.Textview_image);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FormActivity.this, android.R.layout.simple_spinner_item, paths);
        item_spinner = (Spinner) findViewById(R.id.spinner);
        item_spinner.setAdapter(adapter);
        item_spinner.setOnItemSelectedListener(this);
        Bbrowse.setOnClickListener(this);
        Bsubmit.setOnClickListener(this);
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int position = item_spinner.getSelectedItemPosition();
        switch(position){
            case 0:

                    break;
            case 1:

                    break;
            case 2:

                    break;
            case 3:

                    break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.browse_button:
                selectImage();
                break;

            case R.id.bSubmit:
                set();
                verify();
                if(verify_flag){
                    insert_data();
                }
                break;
        }
    }

    private void set() {
        item_type = item_spinner.getSelectedItem().toString();
        place = etPlace.getText().toString();
        details = etDetails.getText().toString();
        name = etName.getText().toString();
        roll_no = etRoll_no.getText().toString();
        int c = COUNT+1;
        image_name=c+".png";
        image_url="http://nirmalostandfound.site11.com/Images/"+image_name;

        Calendar caldate = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(caldate.getTime());

        date = new Date(formattedDate);

        sqldate = new java.sql.Date(new java.util.Date().getTime());

    }


    private void verify() {
        AlertDialog alertDialog = new AlertDialog.Builder(FormActivity.this).create();
        alertDialog.setTitle("!~Error~!");
        alertDialog.setMessage("Die hard");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if(item_type.equals("")){
            alertDialog.setMessage("Please select a item type");
            alertDialog.show();
        }
        else if(place.equals("")){
            alertDialog.setMessage("Please enter a Place");
            alertDialog.show();
        }
        else if(details.equals("")){
            alertDialog.setMessage("Please enter the details");
            alertDialog.show();
        }
        else if(is==null){
            alertDialog.setMessage("Please select an Image");
            alertDialog.show();
        }
        else if(name.equals("")){
            alertDialog.setMessage("Please enter name of the person");
            alertDialog.show();
        }
        else if(roll_no.equals("")){
            alertDialog.setMessage("Please enter roll number of the person");
            alertDialog.show();
        }
        else{
            verify_flag=true;
        }

    }



    private void insert_data() {
        pDialog = new ProgressDialog(FormActivity.this);
        pDialog.setMessage("Connecting to FTP Server...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("Processing");
        pDialog.show();

        //Thread ftp_thread = new Thread();

        new FTP().execute(image_name,is);


        pDialog.setMessage("Inserting into the database...");

        JSONObject insert = new JSONObject();
        try {
            insert.put("id",COUNT+1);
            insert.put("item_type",item_type);
            insert.put("place",place);
            insert.put("date",sqldate);
            insert.put("image_url",image_url);
            //insert.put("image",is);
            insert.put("description",details);
            insert.put("name",name);
            insert.put("roll_no",roll_no);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(FormActivity.this).create();
        alertDialog.setTitle("Status");
        alertDialog.setMessage("");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                });


        JsonObjectRequest req = new JsonObjectRequest(INSERT_URL, insert,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            pDialog.dismiss();
                            int i = response.getInt("success");
                            String msg = response.getString("message");
                            if(i==1){
                                alertDialog.setMessage(msg);
                                alertDialog.show();
                            }

                            Toast.makeText(FormActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FormActivity.this,"Some Connection error maybe", Toast.LENGTH_SHORT).show();

            }
        });
        mQueue.add(req);


    }

    class FTP extends AsyncTask<Object,Void,Void>{


        @Override
        protected Void doInBackground(Object... params) {
            String name = (String) params[0];
            InputStream inputstream = (InputStream) params[1];
            FTPClient ftp = new FTPClient();

            try {
                ftp.connect("nirmalostandfound.site11.com");
                if(!ftp.login("a4869649", "WhySoSerious?"))
                {
                    ftp.logout();
                    return null;
                }
                int reply = ftp.getReplyCode();
                //FTPReply stores a set of constants for FTP reply codes.
                if (!FTPReply.isPositiveCompletion(reply))
                {
                    ftp.disconnect();
                    return null;
                }
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                ftp.changeWorkingDirectory("/public_html/Images");
                ftp.storeFile(name,inputstream);
                ftp.logout();
                ftp.disconnect();


            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_IMAGE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_IMAGE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();





        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            String path=destination.getAbsolutePath();
            String name=destination.getName();
            tvImage.setText(path+name);



            try {
                convert(thumbnail);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 100;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        try {
            convert(bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvImage.setText(selectedImagePath);
    }


    private void convert(Bitmap bitmap) throws Exception {
        if (bitmap == null) {
           return;
        }

        Bitmap bitmap1 = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap1.compress(Bitmap.CompressFormat.PNG, 70, stream); // convert Bitmap to ByteArrayOutputStream
        is = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream
    }
}


