package com.nirmauni.lostandfound;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class CustomRequest extends Request<JSONObject>{

    private Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomRequest(String url, Map<String, String> params,
                         Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomRequest(int method, String url, Map<String, String> params,
                         Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            Log.d("YO\n", "response headers " + response.headers.toString());
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (jsonString.length() == 0) {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }
            Log.d("OH NO!\n","json string "+jsonString);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (JSONException je) {
            Log.v("Volley", "JSONException " + response.statusCode);
            /*if (response.statusCode == 200)// Added for 200 response
                return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
            else*/
                return Response.error(new ParseError(je));
        }
        catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

}