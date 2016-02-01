package com.lecomte.jessy.popularmovies;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.lecomte.jessy.popularmovies.RecyclerView.RV_MultiViewTypeAdapter;

/**
 * Created by Jessy on 2016-01-28.
 */
public class MovieDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item (movie) that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item";
    private static final String AVERAGE_RATING_SUFFIX = "/10";
    private static final String TAG = MovieDetailFragment_old.class.getSimpleName();

    /**
     * The movie data to display
     */
    private MovieInfo mItem;
    private OnCreateFragmentViewListener mCallback;

    public MovieDetailFragment() {
    }

    // Container Activity must implement this interface
    public interface OnCreateFragmentViewListener {
        public MovieInfo onCreateFragmentView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCreateFragmentViewListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDataChangeListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mItem = mCallback.onCreateFragmentView();

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        assert recyclerView != null;

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //List<RecyclerViewData> recyclerViewItems = new ArrayList<RecyclerViewData>();

        RV_MultiViewTypeAdapter recyclerViewAdapter =
                new RV_MultiViewTypeAdapter(getActivity(), false, mItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

       /* item = mCallback.onCreateFragmentView();

        // Show the movie data
        if (item != null) {
            ((TextView) rootView.findViewById(R.id.detail_summary_TextView)).setText(item.getOverview());

            // Movie posterTextView
            ImageView posterTextView = (ImageView) rootView.findViewById(R.id.detail_poster_ImageView);
            String posterUrl = MovieDataUrlBuilder.buildPosterUrl(item.getPoster_path());
            Picasso.with(getContext()).load(posterUrl).into(posterTextView);

            // Release date
            ((TextView) rootView.findViewById(R.id.detail_releaseDate_TextView))
                    .setText(item.getRelease_date());

            // Vote average
            ((TextView) rootView.findViewById(R.id.detail_voteAverage_TextView))
                    .setText(item.getVote_average() + AVERAGE_RATING_SUFFIX);
        }*/
        return rootView;
    }
}
