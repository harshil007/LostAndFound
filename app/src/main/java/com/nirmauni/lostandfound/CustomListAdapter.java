package com.nirmauni.lostandfound;

/**
 * Created by USER on 14/09/2015.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;

import java.lang.annotation.Target;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] imageurl;
    private final int[] imgid;
    //rivate RequestQueue mQueue;

    public CustomListAdapter(Activity context, String[] imageurl, int[] imgid) {
        super(context, R.layout.mylist, imageurl);
        // TODO Auto-generated constructor stub


        this.context=context;
        this.imageurl=imageurl;
        this.imgid=imgid;
       // mQueue = CustomVolleyRequestQueue.getInstance(this.context)
        //        .getRequestQueue();
    }

    public View getView(int position,View view,ViewGroup parent) {
        final View rowView;
        final ImageView imageView;
        LayoutInflater inflater=context.getLayoutInflater();

        rowView=inflater.inflate(R.layout.mylist, null, true);







        //TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);


        //Button button = (Button) rowView.findViewById(R.id.button_image);
       // NetworkImageView img_view = (NetworkImageView) rowView.findViewById(R.id.img);

         imageView = (ImageView) rowView.findViewById(R.id.img);



        //ImageLoader.ImageCache imageCache = new BitmapLruCache();
        //ImageLoader imageLoader = new ImageLoader(mQueue, imageCache);

        String url = imageurl[position];




            Glide.with(context)
                    .load(url)
                    //.fitCenter()
                    .placeholder(R.drawable.load)
                    .crossFade()
                    .into(imageView);





        //rowView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);









        return rowView;

    }
}
