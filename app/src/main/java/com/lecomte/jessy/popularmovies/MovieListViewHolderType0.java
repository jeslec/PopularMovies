package com.lecomte.jessy.popularmovies;

/**
 * Created by Jessy on 2016-01-27.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;

public class MovieListViewHolderType0 extends RecyclerView.ViewHolder {
    public TextView trailersSectionTextView;
    public View mView;
    public TextView releaseDateTextView;
    public TextView voteAverageTextView;
    public ImageView posterImageView;
    public TextView summaryTextView;
    public MovieInfo item;

    public MovieListViewHolderType0(View view) {
        super(view);
        mView = view;
        posterImageView = (ImageView) view.findViewById(R.id.detail_poster_ImageView);
        releaseDateTextView = (TextView)view.findViewById(R.id.detail_releaseDate_TextView);
        voteAverageTextView = (TextView)view.findViewById(R.id.detail_voteAverage_TextView);
        summaryTextView = (TextView) view.findViewById(R.id.detail_summary_TextView);
    }

    @Override
    public String toString() {
        return super.toString();// + " '";// + mContentView.getText() + "'";
    }
}
