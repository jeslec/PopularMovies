package com.lecomte.jessy.popularmovies.RecyclerView;

/**
 * Created by Jessy on 2016-01-27.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lecomte.jessy.popularmovies.R;

// ViewHolder for ViewType 2
public class RV_ViewHolderViewType2 extends RecyclerView.ViewHolder {
    public final View mView;
    public TextView reviewAuthorTextView;
    public TextView reviewContentTextView;

    public RV_ViewHolderViewType2(View view) {
        super(view);
        mView = view;

        // Get a reference to each view
        reviewAuthorTextView = (TextView)view.findViewById(R.id.review_author_TextView);
        reviewContentTextView = (TextView)view.findViewById(R.id.review_content_TextView);
        //----
    }
}
