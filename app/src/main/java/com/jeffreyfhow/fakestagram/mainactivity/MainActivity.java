package com.jeffreyfhow.fakestagram.mainactivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jeffreyfhow.fakestagram.R;
import com.jeffreyfhow.fakestagram.data.Constants;
import com.jeffreyfhow.fakestagram.data.Post;
import com.jeffreyfhow.fakestagram.data.PostAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Main Scene where all the functionality happens
 */
public class MainActivity extends AppCompatActivity implements IMainActivityView {

    private IMainActivityPresenter presenter;

    private RecyclerView recyclerView;
    private Menu menu;

    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private PostAdapter adapter;

    //region Android Lifecycle Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar t = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(t);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        presenter = new MainActivityPresenter();
        presenter.onCreate(
            getApplicationContext(),
            this,
            getIntent().getStringExtra(Constants.ID_TOKEN_MESSAGE)
        );

        recyclerView = findViewById(R.id.post_recycler_view);
        adapter = new PostAdapter(this);
        recyclerView.setAdapter(adapter);
        gridLayoutManager = new GridLayoutManager(this, 2);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }
    //endregion


    //region IMainActivityView Overrides
    @DebugLog
    @Override
    public void displayGridView(List<Post> posts, PostAdapter.Listener listener, boolean hasRecencyText) {
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setHasRecencyText(hasRecencyText);
        adapter.update(posts);
        adapter.setListener(listener);
    }

    @DebugLog
    @Override
    public void displayDetailView(Post post, PostAdapter.Listener listener, boolean hasRecencyText) {
        adapter.setHasRecencyText(hasRecencyText);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.update(post);
        adapter.setListener(listener);
    }

    @DebugLog
    @Override
    public void refreshState(){
        adapter.notifyDataSetChanged();
        recyclerView.refreshDrawableState();
    }

    @DebugLog
    @Override
    public void setUserIcon(String profileURL) {

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

    @DebugLog
    @Override
    public void exit() {
        finish();
    }
    //endregion


    //region Toolbar Overrides
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
                presenter.onLogoutButtonPressed();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }
    //endregion

}
