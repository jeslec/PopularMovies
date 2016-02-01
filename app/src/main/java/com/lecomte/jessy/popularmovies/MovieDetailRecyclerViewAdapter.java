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

public class MovieDetailRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieDetailRecyclerViewAdapter.MovieDetailViewHolder> {

    private final FragmentActivity mFragmentActivity;
    private final boolean mTwoPane;

    // Data to be displayed in the RecyclerView
    private MovieInfo mMovieInfo;

    public MovieDetailRecyclerViewAdapter(FragmentActivity activity, boolean twoPane,
                                        MovieInfo movieInfo) {
        mMovieInfo = movieInfo;
        mFragmentActivity = activity;
        mTwoPane = twoPane;
    }

    /*public void swap(ArrayList<Data> datas){
        data.clear();
        data.addAll(datas);
        notifyDataSetChanged();
    }*/

    @Override
    public MovieDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_info_content, parent, false);
        return new MovieDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieDetailViewHolder holder, int position) {
        holder.mItem = mMovieInfo;
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
                    arguments.putParcelable(MovieDetailFragment_old.ARG_ITEM, holder.mItem);
                    MovieDetailFragment_old fragment = new MovieDetailFragment_old();
                    fragment.setArguments(arguments);
                    mFragmentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity_old.class);
                    intent.putExtra(MovieDetailFragment_old.ARG_ITEM, holder.mItem);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1; //mMovieInfo.size();
    }

    public class MovieDetailViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView posterImageView;
        public MovieInfo mItem;

        public MovieDetailViewHolder(View view) {
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