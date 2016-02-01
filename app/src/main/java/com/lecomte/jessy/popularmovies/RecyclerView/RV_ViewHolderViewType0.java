package com.lecomte.jessy.popularmovies.RecyclerView;

/**
 * Created by Jessy on 2016-01-27.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lecomte.jessy.popularmovies.R;


public class RV_ViewHolderViewType0 extends RecyclerView.ViewHolder {
    public final View mView;

    // Views declarations (TextViews, ImageViews, etc.)
    // These variables should all be declared as "public"
    // e.g. public TextView titleTextView;
    public TextView itemTitleTextView;
    //----

    public RV_ViewHolderViewType0(View view) {
        super(view);
        mView = view;

        // Get a reference to each view
        itemTitleTextView = (TextView)view.findViewById(R.id.recyclerView_item_TextView);
        //----
    }
}
