package com.lecomte.jessy.popularmovies.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lecomte.jessy.mythemoviedblib.MovieDataUrlBuilder;
import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.lecomte.jessy.mythemoviedblib.data.Results;
import com.lecomte.jessy.mythemoviedblib.data.TrailerInfo;
import com.lecomte.jessy.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// Only for a single viewType (means 1 layout is used for all items)
// Only for data that is represented as List<Data>

public class RV_MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RV_MultiViewTypeAdapter.class.getSimpleName();

    // Movie info (poster, release date, rating, summary)
    private static final int VIEW_TYPE_0 = 0;

    // A single trailer (play button, trailer title)
    private static final int VIEW_TYPE_1 = 1;

    // A single review
    private static final int VIEW_TYPE_2 = 2;

    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private final Results[] mReviewArray;
    private int mFirstTrailerIndex = 0;
    private int mFirstReviewIndex = 0;

    // TODO: explain what this is
    private List<IndexViewTypePair> mIndexViewTypePairList;

    private static final String AVERAGE_RATING_SUFFIX = "/10";
    private TrailerInfo[] mTrailerArray;

    // Data to be displayed in the RecyclerView
    private MovieInfo mMovieInfo;
    private FragmentActivity mFragmentActivity;
    private boolean mTwoPane;

    private int mItemCount = 0;

    // Layout Id for a single row (or item) of the RecyclerView
    private int mItemLayoutId;

    public RV_MultiViewTypeAdapter(FragmentActivity activity, boolean twoPane,
                                   MovieInfo movieInfo, TrailerInfo[] trailerArray,
                                   Results[] reviewArray) {
        mMovieInfo = movieInfo;
        mFragmentActivity = activity;
        mTwoPane = twoPane;
        mTrailerArray = trailerArray;
        mReviewArray = reviewArray;
        mIndexViewTypePairList = new ArrayList<>();

        if (movieInfo != null) {
            mItemCount++;
            mIndexViewTypePairList.add(new IndexViewTypePair(0, VIEW_TYPE_0));
        }

        if (mTrailerArray != null) {
            mFirstTrailerIndex = mIndexViewTypePairList.size();
            mItemCount += mTrailerArray.length;

            for (int i=0; i<mTrailerArray.length; i++) {
                mIndexViewTypePairList.add(new IndexViewTypePair(i, VIEW_TYPE_1));
            }
        }

        if (mReviewArray != null) {
            mFirstReviewIndex =  mIndexViewTypePairList.size();
            mItemCount += mReviewArray.length;

            for (int i=0; i<mReviewArray.length; i++) {
                mIndexViewTypePairList.add(new IndexViewTypePair(i, VIEW_TYPE_2));
            }
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

            case VIEW_TYPE_0:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.recyclerview_item_viewtype0, parent, false);
                return new RV_ViewHolderViewType0(view);

            case VIEW_TYPE_1:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.recyclerview_item_viewtype1, parent, false);
                return new RV_ViewHolderViewType1(view);

            case VIEW_TYPE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.recyclerview_item_viewtype2, parent, false);
                return new RV_ViewHolderViewType2(view);
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
            case VIEW_TYPE_0:
                // Set onClick listener on view holder (vh)
                final RV_ViewHolderViewType0 vhViewType0 = (RV_ViewHolderViewType0)viewHolder;
                vhViewType0.item = mMovieInfo;

                String posterUrl = MovieDataUrlBuilder.buildPosterUrl(vhViewType0.item.getPoster_path());

                // Neat trick: we are getting the context from the view itself
                Picasso.with(vhViewType0.posterTextView.getContext())
                        .load(posterUrl).into(vhViewType0.posterTextView);

                vhViewType0.releaseDateTextView.setText(vhViewType0.item.getRelease_date());

                // Vote average
                vhViewType0.voteAverageTextView.setText(vhViewType0.item.getVote_average() +
                        AVERAGE_RATING_SUFFIX);

                vhViewType0.summaryTextView.setText(vhViewType0.item.getOverview());

                break;

            case VIEW_TYPE_1:
                final RV_ViewHolderViewType1 vhViewType1 = (RV_ViewHolderViewType1)viewHolder;
                itemIndex = mIndexViewTypePairList.get(position).getIndex();
                final TrailerInfo trailerInfo = mTrailerArray[itemIndex];
                vhViewType1.trailerTitleTextView.setText(trailerInfo.getName());

                vhViewType1.playTrailerImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(vhViewType1.playTrailerImageView.getContext(),
                                "Play trailer: " + vhViewType1.trailerTitleTextView.getText(),
                                Toast.LENGTH_SHORT).show();

                        // http://stackoverflow.com/questions/4654878/how-to-play-youtube-video-in-my-android-application
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL +
                                trailerInfo.getKey())));
                    }
                });
                break;

            case VIEW_TYPE_2:
                final RV_ViewHolderViewType2 vhViewType2 = (RV_ViewHolderViewType2)viewHolder;
                itemIndex = mIndexViewTypePairList.get(position).getIndex();
                final Results reviewInfo = mReviewArray[itemIndex];
                vhViewType2.reviewAuthorTextView.setText(context.getString(
                        R.string.review_author_prefix) + " " + reviewInfo.getAuthor());
                vhViewType2.reviewContentTextView.setText(reviewInfo.getContent());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mIndexViewTypePairList.size();
    }
}