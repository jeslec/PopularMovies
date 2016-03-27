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
    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    /**
     * The movie data to display
     */
    private MovieInfo mItem;
    private OnCreateFragmentViewListener mCallback;
    private RecyclerView mRecyclerView;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItem = mCallback.onCreateFragmentView();
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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        assert mRecyclerView != null;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return rootView;
    }

    private void updateUI() {
        MovieDetailAdapter recyclerViewAdapter =
                new MovieDetailAdapter(getActivity(), false, mItem);

        mRecyclerView.setAdapter(recyclerViewAdapter);
    }
}
