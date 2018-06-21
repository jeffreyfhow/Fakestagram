package com.jeffreyfhow.fakestagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.jeffreyfhow.fakestagram.DataStructures.FlatPost;
import com.jeffreyfhow.fakestagram.Retrofit.GetDataService;
import com.jeffreyfhow.fakestagram.Retrofit.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String mTokenId;
    private ArrayList<FlatPost> mPosts;
    ArrayList<FlatPost> mDetailPosts;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTokenId = getIntent().getStringExtra(AuthenticatorActivity.ID_TOKEN_MESSAGE);
        getPosts(mTokenId);

        mDetailPosts = new ArrayList<>();

        gridview = (GridView) findViewById(R.id.gridview);
    }

    private void getPosts(String id_token) {
        GetDataService client = ServiceGenerator.createService(GetDataService.class);
        Call<ArrayList<FlatPost>> call = client.getAllPosts("Bearer " + id_token);

        call.enqueue(new Callback<ArrayList<FlatPost>>() {
            @Override
            public void onResponse(Call<ArrayList<FlatPost>> call, Response<ArrayList<FlatPost>> response) {
                mPosts = response.body();
                displayGridView();
            }

            @Override
            public void onFailure(Call<ArrayList<FlatPost>> call, Throwable t) {
                //TODO: Dialog to reroute to AuthenticatorActivity
                Log.v("MainActivity", t.getMessage());
            }
        });
    }

    private void displayGridView(){
        PostAdapter adapter = new PostAdapter(this, mPosts);
        gridview.setAdapter(adapter);
        gridview.setNumColumns(2);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                displayDetailView(i);
            }
        });
    }

    private void displayDetailView(final int pos){
        mDetailPosts.clear();
        mDetailPosts.add(mPosts.get(pos));
        PostAdapter adapter = new PostAdapter(this, mDetailPosts);
        adapter.setHasRecencyText(true);
        gridview.setNumColumns(1);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                likePhoto(pos);
            }
        });
    }

    private void likePhoto(int pos){
//        Toast.makeText(this, "Do Like Functionality on " + pos, Toast.LENGTH_SHORT).show();
        displayGridView();
    }
}
