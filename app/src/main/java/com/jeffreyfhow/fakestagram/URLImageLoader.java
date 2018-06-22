package com.jeffreyfhow.fakestagram;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Utility class used to load url images from Glide into ImageViews
 */
public class URLImageLoader {

    private static URLImageLoader instance = null;

    private URLImageLoader() {};

    public static synchronized URLImageLoader getInstance() {
        if (instance == null) {
            instance = new URLImageLoader();
        }
        return(instance);
    }

    public void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }

}
