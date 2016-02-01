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

    // Views declarations (TextViews, ImageViews, etc.)
    // These variables should all be declared as "public"
    // e.g. public TextView titleTextView;
    public TextView itemNameTextView;
    public ImageView itemImageView;
    //----

    public RV_ViewHolderViewType1(View view) {
        super(view);
        mView = view;

        // Get a reference to each view
        itemNameTextView = (TextView)view.findViewById(R.id.recyclerView_item_viewType1_TextView);
        itemImageView = (ImageView)view.findViewById(R.id.recyclerView_item_viewType1_ImageView);
        //----
    }
}
