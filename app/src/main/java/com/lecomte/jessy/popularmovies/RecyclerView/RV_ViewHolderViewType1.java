package com.lecomte.jessy.popularmovies.RecyclerView;

/**
 * Created by Jessy on 2016-01-27.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecomte.jessy.popularmovies.R;

// ViewHolder for ViewType 1
public class RV_ViewHolderViewType1 extends RecyclerView.ViewHolder {
    public final View mView;
    public final ImageView playTrailerImageView;
    public TextView trailerTitleTextView;

    public RV_ViewHolderViewType1(View view) {
        super(view);
        mView = view;

        // Get a reference to each view
        trailerTitleTextView = (TextView)view.findViewById(R.id.trailer_title_TextView);
        playTrailerImageView = (ImageView)view.findViewById(R.id.trailer_play_ImageView);
        //----
    }
}
