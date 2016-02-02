package com.lecomte.jessy.popularmovies;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lecomte.jessy.mynetworklib.NetworkUtils;
import com.lecomte.jessy.mythemoviedblib.MovieDataUrlBuilder;
import com.lecomte.jessy.mythemoviedblib.TheMovieDbJsonParser;
import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.lecomte.jessy.mythemoviedblib.data.Reviews;
import com.lecomte.jessy.mythemoviedblib.data.Trailers;
import com.lecomte.jessy.popularmovies.RecyclerView.RV_MultiViewTypeAdapter;

import org.json.JSONException;

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
    private Trailers mTrailersData;
    private Reviews mReviewsData;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItem = mCallback.onCreateFragmentView();

        new downloadTrailerDataTask().execute(MovieDataUrlBuilder.buildTrailersUrl(
                MovieListActivity.TMDB_API_KEY, mItem.getId()));

        new downloadReviewsTask().execute(MovieDataUrlBuilder.buildReviewsUrl(
                MovieListActivity.TMDB_API_KEY, mItem.getId()));
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

        return rootView;
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class downloadTrailerDataTask extends AsyncTask<String, Void, Trailers> {

        @Override
        protected Trailers doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            String jsonString = NetworkUtils.downloadData(urls[0]);

            try {
                // Parse the JSON string into our model layer
                return TheMovieDbJsonParser.parseTrailerData(jsonString);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        // Displays the results of the AsyncTask
        @Override
        protected void onPostExecute(Trailers trailersData) {
            mTrailersData = trailersData;
            Log.d(TAG, trailersData.toString());
            updateUI();
        }
    }

    private void updateUI() {
        if (mTrailersData == null || mReviewsData == null) {
            Log.d(TAG, "Trailers data or Reviews data is null");
            return;
        }

        RV_MultiViewTypeAdapter recyclerViewAdapter =
                new RV_MultiViewTypeAdapter(getActivity(), false, mItem, mTrailersData.getResults(),
                        mReviewsData.getResults());

        mRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private class downloadReviewsTask extends AsyncTask<String, Void, Reviews> {

        @Override
        protected Reviews doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            String jsonString = NetworkUtils.downloadData(urls[0]);

            try {
                // Parse the JSON string into our model layer
                return TheMovieDbJsonParser.parseReviewsData(jsonString);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Reviews reviews) {
           mReviewsData = reviews;
           updateUI();
        }
    }
}
