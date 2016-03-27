package com.lecomte.jessy.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

        assert intent != null;
        assert intent.getExtras() != null;

        mItem = intent.getExtras().getParcelable(EXTRA_ITEM);
        assert mItem != null;

        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getTitle());
        }
    }

    @Override
    public MovieInfo onCreateFragmentView() {
        return mItem;
    }
}
