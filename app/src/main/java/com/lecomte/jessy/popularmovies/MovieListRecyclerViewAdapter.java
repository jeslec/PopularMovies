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
public class MovieListRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> {

    private final FragmentActivity mFragmentActivity;
    private final boolean mTwoPane;

    // Data to be displayed in the RecyclerView
    private ArrayList<MovieInfo> mData;

    public MovieListRecyclerViewAdapter(FragmentActivity activity, boolean twoPane,
                                        MovieInfo[] resultsArray) {
        mData = new ArrayList<MovieInfo>(Arrays.asList(resultsArray));
        mFragmentActivity = activity;
        mTwoPane = twoPane;
    }

    /*public void swap(ArrayList<Data> datas){
        data.clear();
        data.addAll(datas);
        notifyDataSetChanged();
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mData.get(position);
        String posterUrl = MovieDataUrlBuilder.buildPosterUrl(holder.mItem.getPoster_path());
        //Log.d(TAG, String.format("[%d]: %s", position, posterUrl));

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
                    intent.putExtra(MovieDetailFragment.ARG_ITEM, holder.mItem);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView posterImageView;
        public MovieInfo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            posterImageView = (ImageView)view.findViewById(R.id.movie_list_poster_ImageView);
        }

        @Override
        public String toString() {
            return super.toString();// + " '";// + mContentView.getText() + "'";
        }
    }
}
