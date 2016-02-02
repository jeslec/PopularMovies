package com.lecomte.jessy.popularmovies.RecyclerView;

/**
 * Created by Jessy on 2016-01-27.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lecomte.jessy.popularmovies.R;

// ViewHolder for ViewType 3
public class RV_ViewHolderViewType3 extends RecyclerView.ViewHolder {
    public final View mView;
    public TextView sectionTitleTextView;

    public RV_ViewHolderViewType3(View view) {
        super(view);
        mView = view;

        // Get a reference to each view
        sectionTitleTextView = (TextView)view.findViewById(R.id.section_title_TextView);
        //----
    }
}
