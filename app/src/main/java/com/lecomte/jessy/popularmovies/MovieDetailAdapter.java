package com.lecomte.jessy.popularmovies;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecomte.jessy.mythemoviedblib.MovieDataUrlBuilder;
import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// Only for a single viewType (means 1 layout is used for all items)
// Only for data that is represented as List<Data>

public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = MovieDetailAdapter.class.getSimpleName();

    // Movie info (poster, release date, rating, summary)
    private static final int VIEW_TYPE_0_MOVIE_INFO = 0;

    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    // TODO: explain what this is
    private List<IndexViewTypePair> mIndexViewTypePairList;

    private static final String AVERAGE_RATING_SUFFIX = "/10";

    // Data to be displayed in the RecyclerView
    private MovieInfo mMovieInfo;
    private FragmentActivity mFragmentActivity;
    private boolean mTwoPane;
    private List<Integer> mSectionTitleResList;

    private int mItemCount = 0;

    // Layout Id for a single row (or item) of the RecyclerView
    private int mItemLayoutId;

    public MovieDetailAdapter(FragmentActivity activity, boolean twoPane,
                              MovieInfo movieInfo) {
        mMovieInfo = movieInfo;
        mFragmentActivity = activity;
        mTwoPane = twoPane;
        mIndexViewTypePairList = new ArrayList<>();

        // Holds the list of string resources Ids for the section titles
        // Use this list index in the <index, viewType> pair
        mSectionTitleResList = new ArrayList<Integer>();

        if (movieInfo != null) {
            mItemCount++;
            mIndexViewTypePairList.add(new IndexViewTypePair(0, VIEW_TYPE_0_MOVIE_INFO));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mIndexViewTypePairList.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        View view;

        switch (viewType) {

            case VIEW_TYPE_0_MOVIE_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.movie_detail_item_viewtype0, parent, false);
                return new MovieListViewHolderType0(view);
        }

        return viewHolder;
    }

    // Put the data to display (list item) inside each view (TextView, ImageView, etc.)
    // This is called for each row of the recyclerView
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        int itemIndex;
        final Context context = viewHolder.itemView.getContext();

        switch (getItemViewType(position)) {
            case VIEW_TYPE_0_MOVIE_INFO:
                // Set onClick listener on view holder (vh)
                final MovieListViewHolderType0 vhViewType0 = (MovieListViewHolderType0)viewHolder;
                vhViewType0.item = mMovieInfo;

                // No image Url specified: load default image
                if (vhViewType0.item.getPoster_path() == null ||
                        vhViewType0.item.getPoster_path().isEmpty() ||
                        vhViewType0.item.getPoster_path().equals("null")) {
                    // Load default image (no image)
                    Picasso.with(vhViewType0.posterImageView.getContext())
                            .load(R.drawable.noimage).into(vhViewType0.posterImageView);
                }

                // Load image specified by Url
                else {
                    // Load image from Url
                    String posterUrl = MovieDataUrlBuilder.buildPosterUrl(
                            vhViewType0.item.getPoster_path());
                    Picasso.with(vhViewType0.posterImageView.getContext())
                            .load(posterUrl).into(vhViewType0.posterImageView);
                }

                vhViewType0.releaseDateTextView.setText(vhViewType0.item.getRelease_date());

                // Vote average
                vhViewType0.voteAverageTextView.setText(vhViewType0.item.getVote_average() +
                        AVERAGE_RATING_SUFFIX);

                vhViewType0.summaryTextView.setText(vhViewType0.item.getOverview());

                break;

            // TODO: Add other view types later (reviews, trailers, etc.)
        }
    }

    @Override
    public int getItemCount() {
        return mIndexViewTypePairList.size();
    }
}