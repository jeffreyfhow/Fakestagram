package com.jeffreyfhow.fakestagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.jeffreyfhow.fakestagram.Network.NetworkService;
import com.jeffreyfhow.fakestagram.Network.ServiceGenerator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String mTokenId;
    private ArrayList<Post> mPosts;
    private RecyclerView recyclerView;
    private boolean mIsDetailView = false;
    private String profileURL;
    private Menu menu;

    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private PostAdapter adapter;
    int detailIdx = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar t = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(t);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTokenId = getIntent().getStringExtra(AuthenticatorActivity.ID_TOKEN_MESSAGE);
        getPosts(mTokenId);
        Log.v("MainActivity", "Token: " + mTokenId);

        recyclerView = findViewById(R.id.post_recycler_view);

        adapter = new PostAdapter(this);
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(this, 2);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(gridLayoutManager);
    }


    private void getPosts(String id_token) {
        NetworkService client = ServiceGenerator.createService(NetworkService.class);
        Call<ArrayList<Post>> call = client.getAllPosts("Bearer " + id_token);

        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                mPosts = response.body();

                profileURL = mPosts.get(0).getProfilePictureUrl();
                setUserIcon();
                Collections.sort(mPosts, new Comparator<Post>() {
                    @Override
                    public int compare(Post p1, Post p2) {
                        return p1.getDateCreated().isAfter(p2.getDateCreated()) ? -1 : 1;
                    }
                });

                displayGridView();
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error retrieving posts. Please try again.", Toast.LENGTH_SHORT).show();
                startAuthenticatorActivity();
                Log.v("MainActivity", t.getMessage());
            }
        });
    }

    private void displayGridView() {
        mIsDetailView = false;
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setHasRecencyText(false);
        adapter.update(mPosts);
        adapter.setListener(new PostAdapter.Listener() {
            @Override
            public void onClick(int position) {
                displayDetailView(position);
            }
        });
    }

    private void displayDetailView(final int pos) {
        mIsDetailView = true;

        adapter.setHasRecencyText(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        detailIdx = pos;
        adapter.update(mPosts.get(pos));
        adapter.setListener(new PostAdapter.Listener() {
            @Override
            public void onClick(int position) {
                if(detailIdx >= 0){
                    tryLikePhoto(detailIdx);
                }
            }
        });

    }

    private void tryLikePhoto(int pos) {

        Post p = mPosts.get(pos);
        boolean hasLiked = p.getUserHasLiked();
        long likeCnt = p.getLikeCnt();
        if (hasLiked) {
            p.setUserHasLiked(false);
            p.setLikeCnt(--likeCnt);
            sendUnlikeRequest(p.getPostId());
        } else {
            p.setUserHasLiked(true);
            p.setLikeCnt(++likeCnt);
            sendLikeRequest(p.getPostId());
        }
        adapter.notifyDataSetChanged();
        recyclerView.refreshDrawableState();
    }

    private void sendLikeRequest(String id) {
        NetworkService client = ServiceGenerator.createService(NetworkService.class);
        Call<Void> call = client.postLike("Bearer " + mTokenId, id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Like Posted Response - Success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.v("MainActivity", t.getMessage());
            }
        });
    }

    private void sendUnlikeRequest(String id) {
        NetworkService client = ServiceGenerator.createService(NetworkService.class);
        Call<Void> call = client.deleteLike("Bearer " + mTokenId, id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Like Deleted Response - Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.v("MainActivity", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logOut();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://insta23prod.auth.us-west-2.amazoncognito.com/logout?response_type=token&client_id=5khm2intordkd1jjr7rbborbfj&logout_uri=https://www.23andme.com/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        startAuthenticatorActivity();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        startAuthenticatorActivity();
                    }
                });

        queue.add(stringRequest);
    }

    private void startAuthenticatorActivity() {
        Intent intent = new Intent(this, AuthenticatorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mIsDetailView) {
            displayGridView();
        } else {
            logOut();
        }
    }

    private void setUserIcon() {

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                MenuItem m = menu.getItem(0);
                m.setIcon(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.v("MainActivity", e.getMessage());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.get().load(profileURL).into(target);

    }

}
