package com.jeffreyfhow.fakestagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jeffreyfhow.fakestagram.DataStructures.FlatPost;
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
    private ArrayList<FlatPost> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTokenId = getIntent().getStringExtra(AuthenticatorActivity.ID_TOKEN_MESSAGE);
        getPosts(mTokenId);

    }

    private void getPosts(String id_token) {
        GetDataService client = ServiceGenerator.createService(GetDataService.class);
        Call<ArrayList<FlatPost>> call = client.getAllPosts("Bearer " + id_token);

        call.enqueue(new Callback<ArrayList<FlatPost>>() {
            @Override
            public void onResponse(Call<ArrayList<FlatPost>> call, Response<ArrayList<FlatPost>> response) {
                mPosts = response.body();
                Log.v("MainActivity", mPosts.toString());

                refreshDisplay();
            }

            @Override
            public void onFailure(Call<ArrayList<FlatPost>> call, Throwable t) {
                //TODO: Dialog to reroute to AuthenticatorActivity
                Log.v("MainActivity", t.getMessage());
            }
        });
    }

    private void refreshDisplay(){

    }
}
