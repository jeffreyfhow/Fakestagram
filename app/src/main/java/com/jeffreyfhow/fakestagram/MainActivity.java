package com.jeffreyfhow.fakestagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jeffreyfhow.fakestagram.DataStructures.Post;
import com.jeffreyfhow.fakestagram.DataStructures.Posts;
import com.jeffreyfhow.fakestagram.Retrofit.GetDataService;
import com.jeffreyfhow.fakestagram.Retrofit.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String mTokenId;
    private ArrayList<Post> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTokenId = getIntent().getStringExtra(AuthenticatorActivity.ID_TOKEN_MESSAGE);
        getPosts(mTokenId);

    }

    private void getPosts(String id_token) {
        GetDataService client = ServiceGenerator.createService(GetDataService.class);
        Call<Posts> call = client.getAllPosts("Bearer " + id_token);

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                Posts p = response.body();
                mPosts = p.getPostList();
                refreshDisplay();
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                //TODO: Dialog to reroute to AuthenticatorActivity
                Log.v("AuthenticatorActivity", t.getMessage());
            }
        });
    }

    private void refreshDisplay(){

    }
}
