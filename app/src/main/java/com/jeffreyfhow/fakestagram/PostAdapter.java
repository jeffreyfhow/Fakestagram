package com.jeffreyfhow.fakestagram;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {

    protected boolean hasRecencyText = false;

    public PostAdapter(@NonNull Context context, @NonNull List<Post> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View gridItemView = convertView;
        if(gridItemView == null){
            gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.image_card, parent, false);
        }

        Post currentPost = getItem(position);

        TextView likesText = (TextView) gridItemView.findViewById(R.id.likes_text);
        likesText.setText(currentPost.getLikeCnt() + " Likes");

        TextView recencyText = (TextView) gridItemView.findViewById(R.id.recency_text);
        if(hasRecencyText){
            recencyText.setText(currentPost.getRecencyString().toString());
        } else {
            recencyText.setVisibility(View.GONE);
        }

        ImageView image = gridItemView.findViewById(R.id.card_image);
        Glide
                .with(getContext())
                .load(currentPost.getImgLowRes())
                .into(image);

        ImageView likeImg = gridItemView.findViewById(R.id.like_image);
        if(currentPost.getUserHasLiked()){
            Context c = getContext();
            Drawable d = c.getResources().getDrawable(android.R.drawable.star_on, c.getTheme());
            likeImg.setImageDrawable(d);
        } else {
            Context c = getContext();
            Drawable d = c.getResources().getDrawable(android.R.drawable.star_off, c.getTheme());
            likeImg.setImageDrawable(d);
        }

        return gridItemView;
    }

    public void setHasRecencyText(boolean hasRecencyText) {
        this.hasRecencyText = hasRecencyText;
    }
}
