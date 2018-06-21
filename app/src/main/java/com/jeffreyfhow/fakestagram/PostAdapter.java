package com.jeffreyfhow.fakestagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeffreyfhow.fakestagram.DataStructures.FlatPost;

import java.util.List;

public class PostAdapter extends ArrayAdapter<FlatPost> {

    protected boolean hasRecencyText = false;

    public PostAdapter(@NonNull Context context, @NonNull List<FlatPost> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View gridItemView = convertView;
        if(gridItemView == null){
            gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.image_card, parent, false);
        }

        FlatPost currentPost = getItem(position);

        TextView likesText = (TextView) gridItemView.findViewById(R.id.likes_text);
        likesText.setText(currentPost.getLikeCnt().toString() + " Likes");

        TextView recencyText = (TextView) gridItemView.findViewById(R.id.recency_text);
        if(hasRecencyText){
            recencyText.setText(currentPost.getTimeCreated().toString());
        } else {
            recencyText.setVisibility(View.GONE);
        }

        ImageView image = gridItemView.findViewById(R.id.card_image);
        Glide
                .with(getContext())
                .load(currentPost.getImgLowRes())
                .into(image);

        return gridItemView;
    }

    public void setHasRecencyText(boolean hasRecencyText) {
        this.hasRecencyText = hasRecencyText;
    }
}
