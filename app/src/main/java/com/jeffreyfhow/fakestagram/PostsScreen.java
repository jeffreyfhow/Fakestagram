package com.jeffreyfhow.fakestagram;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class PostsScreen {

    private Context context;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;


    public PostsScreen(Context context, View parent){
        this.context = context;
        recyclerView = parent.findViewById(R.id.post_recycler_view);
        adapter = new PostAdapter(context);
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(context, 2);

        linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(gridLayoutManager);
    }

//    public void render(ItemListState state) {
//        adapter.update(state.getItems());
//    }


}
