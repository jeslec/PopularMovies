package com.lecomte.jessy.popularmovies.RecyclerView;

/**
 * Created by Jessy on 2016-02-01.
 */
public class IndexViewTypePair {
    private Integer mIndex;
    private Integer mViewType;

    public IndexViewTypePair(int index, int viewType) {
        mIndex = index;
        mViewType = viewType;
    }

    public int getViewType() {
        return mViewType;
    }
}
