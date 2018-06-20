package com.jeffreyfhow.fakestagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jeffreyfhow.fakestagram.DataStructures.Post;
import com.jeffreyfhow.fakestagram.DataStructures.Posts;
import com.jeffreyfhow.fakestagram.Retrofit.GetDataService;
import com.jeffreyfhow.fakestagram.Retrofit.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetDataService client = retrofit.create(GetDataService.class);
        Call<Posts> call = client.getAllPosts("Bearer " + id_token);

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                Log.v("AuthenticatorActivity", "RESPONDING!!!!");
                String a = this.getClass().getName();
                Posts p = response.body();
                mPosts = p.getPostList();
                int i = 0;
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Log.v("AuthenticatorActivity", t.getMessage());
            }
        });
    }
}
