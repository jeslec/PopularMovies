package com.lecomte.jessy.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;

public class MovieDetailActivity extends AppCompatActivity
        implements MovieDetailFragment.OnCreateFragmentViewListener {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String EXTRA_ITEM = "com.lecomte.jessy.popularmovies.EXTRA_ITEM";

    /**
     * The movie data to display
     */
    private MovieInfo mItem;

    private boolean mFavoriteMovie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent == null || intent.getExtras() == null ||
                !intent.getExtras().containsKey(EXTRA_ITEM)) {
            Log.d(TAG, "onCreate() - Intent is null, intent extra is null or extra not found");
        }

        mItem = intent.getExtras().getParcelable(EXTRA_ITEM);

        if (mItem == null) {
            Log.d(TAG, "onCreate() - item is null!");
        }

        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.favorite_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change icon to filled icon
                FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.favorite_fab);

                if (fab != null) {
                    if (mFavoriteMovie) {
                        // Set icon to unfilled star
                        fab.setImageResource(R.drawable.ic_star_border_white_48dp);
                    }
                    else {
                        // Set icon to filled star
                        fab.setImageResource(R.drawable.ic_star_white_48dp);
                        
                        Snackbar.make(view, "Movie added to your favorites", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    // Toggle favorite state
                    mFavoriteMovie = !mFavoriteMovie;
                }
            }
        });

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getTitle());
        }
    }

    @Override
    public MovieInfo onCreateFragmentView() {
        return mItem;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
