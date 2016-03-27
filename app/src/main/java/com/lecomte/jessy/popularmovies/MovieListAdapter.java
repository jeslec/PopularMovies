package com.lecomte.jessy.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lecomte.jessy.mythemoviedblib.MovieDataUrlBuilder;
import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jessy on 2016-01-27.
 */
public class MovieListAdapter
        extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    private final FragmentActivity mFragmentActivity;
    private final boolean mTwoPane;

    // Data to be displayed in the RecyclerView
    private ArrayList<MovieInfo> mData;

    public MovieListAdapter(FragmentActivity activity, boolean twoPane,
                            MovieInfo[] resultsArray) {
        if (resultsArray != null) {
            mData = new ArrayList<MovieInfo>(Arrays.asList(resultsArray));
        }

        mFragmentActivity = activity;
        mTwoPane = twoPane;
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);
        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieListViewHolder holder, int position) {
        holder.mItem = mData.get(position);

        if (holder.mItem == null) {
            return;
        }

        String posterUrl = MovieDataUrlBuilder.buildPosterUrl(holder.mItem.getPoster_path());

        // Neat trick: we are getting the context from the view itself
        Picasso.with(holder.posterImageView.getContext())
                .load(posterUrl).into(holder.posterImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MovieDetailFragment.ARG_ITEM, holder.mItem);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    mFragmentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.EXTRA_ITEM, holder.mItem);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }

        return mData.size();
    }

    public class MovieListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView posterImageView;
        public MovieInfo mItem;

        public MovieListViewHolder(View view) {
            super(view);
            mView = view;
            posterImageView = (ImageView)view.findViewById(R.id.movie_list_poster_ImageView);
        }
    }
}
