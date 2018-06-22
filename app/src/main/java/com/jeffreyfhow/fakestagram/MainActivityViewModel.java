package com.jeffreyfhow.fakestagram;

import java.util.ArrayList;

public class MainActivityViewModel {
    interface UpdateListener {
        void onUpdate(ArrayList<Post> posts);
    }

    private ArrayList<Post> posts;

    private UpdateListener listener;

    public MainActivityViewModel(){

    }

//    private void getPosts(String id_token) {
//        GetPostsService client = ServiceGenerator.createService(GetPostsService.class);
//        Call<ArrayList<Post>> call = client.getAllPosts("Bearer " + id_token);
//
//        call.enqueue(new Callback<ArrayList<Post>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
//                mPosts = response.body();
//
//                profileURL = mPosts.get(0).getProfilePictureUrl();
//                setUserIcon();
//                Collections.sort(mPosts, new Comparator<Post>() {
//                    @Override
//                    public int compare(Post p1, Post p2) {
//                        return p1.getDateCreated().isAfter(p2.getDateCreated()) ? -1 : 1;
//                    }
//                });
//
//                displayGridView();
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Error retrieving posts. Please try again.", Toast.LENGTH_SHORT).show();
//                startAuthenticatorActivity();
//                Log.v("MainActivity", t.getMessage());
//            }
//        });
//    }
}


