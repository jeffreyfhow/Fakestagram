package com.jeffreyfhow.fakestagram.data;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeffreyfhow.fakestagram.R;
import com.jeffreyfhow.fakestagram.utility.URLImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter that binds Posts to CardViews
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView photoImageView;
        private ImageView likesImageView;
        private TextView likesTextView;
        private TextView durationTextView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            photoImageView = cardView.findViewById(R.id.card_image);
            likesImageView = cardView.findViewById(R.id.like_image);
            likesTextView = cardView.findViewById(R.id.likes_text);
            durationTextView = cardView.findViewById(R.id.recency_text);
        }
    }

    public interface Listener {
        void onClick(int position);
    }

    private Context context;
    private ArrayList<Post> postList;
    private Listener listener;

    protected boolean hasRecencyText = false;

    public PostAdapter(Context context) {
        this.context = context;
        postList = new ArrayList<>();
    }

    public void update(List<Post> newItems) {
        postList.clear();
        postList.addAll(newItems);
        notifyDataSetChanged();
    }

    public void update(Post newItem){
        postList.clear();
        postList.add(newItem);
        notifyDataSetChanged();

    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.image_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Post currentPost = postList.get(position);

        holder.likesTextView.setText(currentPost.getLikeCnt() + " Likes");

        if(getItemCount() == 1){
            holder.durationTextView.setVisibility(View.VISIBLE);
            holder.durationTextView.setText(currentPost.getRecencyString().toString());
        } else {
            holder.durationTextView.setVisibility(View.GONE);
        }

        URLImageLoader.getInstance().loadImage(
            context, currentPost.getImgLowRes(), holder.photoImageView);

        holder.likesImageView.setImageDrawable(
            context.getResources().getDrawable(
                currentPost.getUserHasLiked() ? android.R.drawable.star_on : android.R.drawable.star_off,
                context.getTheme()
            )
        );

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(listener != null){
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setHasRecencyText(boolean hasRecencyText){
        this.hasRecencyText = hasRecencyText;
    }
}
