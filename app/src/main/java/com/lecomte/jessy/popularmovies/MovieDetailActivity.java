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

        //Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getTitle());
        }

        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Add fragment to the activity

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        /*if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            *//*Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.ARG_ITEM,
                    getIntent().getParcelableExtra(MovieDetailFragment.ARG_ITEM));*//*
            MainFragment fragment = new MainFragment();
            //fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }*/
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
