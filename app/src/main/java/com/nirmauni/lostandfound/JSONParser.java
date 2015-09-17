package com.nirmauni.lostandfound;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;


import org.apache.http.HttpEntity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jsonObj ;
    static String json = "";

    // default no argument constructor for jsonpaser class
    public JSONParser() {

    }


    public JSONObject getJSONFromUrl(final String url) {

        // Making HTTP request
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            // Executing POST request & storing the response from server  locally.
            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();

            is = httpEntity.getContent();                // get InputStream object

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("hey");
        }

        try {


            // Create a BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            // Declaring string builder
            StringBuilder str = new StringBuilder();
            //  string to store the JSON object.
            String strLine = null;

            // Building while we have string !equal null.
            while ((strLine = reader.readLine()) != null) {
                str.append(strLine + "\n");
            }

            // Close inputstream.
            is.close();
            // string builder data conversion  to string.
            json = str.toString();                           // inserting all data to string object
        } catch (Exception e) {
            Log.e("Error", " something wrong with converting result " + e.toString());
        }

        // Try block used for pasrseing String to a json object
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("json Parsering", "" + e.toString());
        }

        // Returning json Object.
        return jsonObj;

    }



    public JSONObject makeHttpRequest(String url, String method,
                                      String username, String password) {

        // Make HTTP request
        OutputStream post = null;
        try {

            // checking request method
            if(method == "POST"){

                // now defaultHttpClient object
               /* HttpParams httpParameters = new BasicHttpParams();
// Set the timeout in milliseconds until a connection is established.
// The default value is zero, that means the timeout is not used.
                int timeoutConnection = 30000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 50000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);*/

               /* DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();*/

                URL ul = new URL(url);
                //OkHttpClient ok = new OkHttpClient();
                HttpURLConnection con = (HttpURLConnection)ul.openConnection();
                //HttpURLConnection con = ok.open(ul);



                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);
                Uri.Builder u = new Uri.Builder().appendQueryParameter("username",username).appendQueryParameter("password",password);

                String b = u.build().getEncodedQuery();
               // UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
                con.setRequestMethod(method);
                post = con.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(post));
                bw.write(b);
                bw.flush();
                con.connect();
                is=con.getInputStream();

            }else if(method == "GET"){
                // request method is GET
               /* DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();*/
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("hey1");
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is));
            StringBuilder str = new StringBuilder();
            String strLine = null;
            while ((strLine = reader.readLine()) != null) {
                str.append(strLine + "\n");
            }
            post.close();

            is.close();
            json = str.toString();
        } catch (Exception e) {
            System.out.print("hey2");
        }

        // now will try to parse the string into JSON object
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return jsonObj;

    }

}


