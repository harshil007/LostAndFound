package com.nirmauni.lostandfound;

/**
 * Created by USER on 14/09/2015.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] imageurl;
    private final int[] imgid;
    private RequestQueue mQueue;

    public CustomListAdapter(Activity context, String[] imageurl, int[] imgid) {
        super(context, R.layout.mylist, imageurl);
        // TODO Auto-generated constructor stub


        this.context=context;
        this.imageurl=imageurl;
        this.imgid=imgid;
        mQueue = CustomVolleyRequestQueue.getInstance(this.context)
                .getRequestQueue();
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);


        //TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


        //Button button = (Button) rowView.findViewById(R.id.button_image);
        NetworkImageView img_view = (NetworkImageView) rowView.findViewById(R.id.img);

        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        ImageLoader imageLoader = new ImageLoader(mQueue, imageCache);

        img_view.setImageUrl(imageurl[position],imageLoader);


        return rowView;

    };
}
