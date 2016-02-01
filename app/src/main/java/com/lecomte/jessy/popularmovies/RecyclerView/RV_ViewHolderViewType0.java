package com.lecomte.jessy.popularmovies.RecyclerView;

/**
 * Created by Jessy on 2016-01-27.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.lecomte.jessy.popularmovies.R;

public class RV_ViewHolderViewType0 extends RecyclerView.ViewHolder {
    public View mView;
    public TextView releaseDateTextView;
    public TextView voteAverageTextView;
    public ImageView posterTextView;
    public MovieInfo item;

    public RV_ViewHolderViewType0(View view) {
        super(view);
        mView = view;
        posterTextView = (ImageView) view.findViewById(R.id.detail_poster_ImageView);
        releaseDateTextView = (TextView)view.findViewById(R.id.detail_releaseDate_TextView);
        voteAverageTextView = (TextView)view.findViewById(R.id.detail_voteAverage_TextView);
    }

    @Override
    public String toString() {
        return super.toString();// + " '";// + mContentView.getText() + "'";
    }
}
