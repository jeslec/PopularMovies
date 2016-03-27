package com.lecomte.jessy.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lecomte.jessy.mynetworklib.NetworkUtils;
import com.lecomte.jessy.mythemoviedblib.MovieDataUrlBuilder;
import com.lecomte.jessy.mythemoviedblib.TheMovieDbJsonParser;
import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.lecomte.jessy.mythemoviedblib.data.Movies;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    private static final String TAG = MovieListActivity.class.getSimpleName();

    // TODO: Remove API key from GitHub!!!
    // Set as public for now because we also need this value in detailsView
    public static final String TMDB_API_KEY = "1ed96e22fff439407e05fbfbb876aa3b";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView mRecyclerView;
    private int mSortBy = MovieDataUrlBuilder.SORT_BY_MOST_POPULAR;
    private TextView mEmptyView;
    //private NetworkChangeReceiver mNetworkChangeReceiver;
    private boolean mNetworkConnectionMonitored = false;
    private MyNetworkChangeReceiver mNetworkChangeReceiver;
    private AsyncTask<String, Void, Movies> mDownloadMovieDataTask;
    private ArrayList<MovieInfo> mMoviesData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mRecyclerView = (RecyclerView)findViewById(R.id.movie_list);
        assert mRecyclerView != null;
        setupRecyclerView();

        mEmptyView = (TextView)findViewById(R.id.empty_view);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        Parcelable[] movieInfoParcelArray = new Parcelable[]{};

        // If onCreate() is being called as a result of a config change (e.g. device rotation),
        // load the movie info that was present before the config change
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate() - Was called because of a config change");
            movieInfoParcelArray = savedInstanceState.getParcelableArray(
                    getString(R.string.instance_state_key_movies_data));

            if (movieInfoParcelArray != null) {
                MovieInfo[] movieDataArray = new MovieInfo[movieInfoParcelArray.length];
                for (int i=0; i<movieInfoParcelArray.length; i++) {
                    movieDataArray[i] = (MovieInfo)movieInfoParcelArray[i];
                }

                Movies movies = new Movies();
                movies.setResults(movieDataArray);
                updateUI(movies);
            }
        }

        // No movies info was loaded before the config change, so download from server
        if (movieInfoParcelArray == null || movieInfoParcelArray.length == 0) {
            downloadMovieData();
        }
    }

    private void downloadMovieData() {
        mDownloadMovieDataTask = new DownloadMovieDataTask()
                .execute(MovieDataUrlBuilder.buildDiscoverUrl(TMDB_API_KEY, mSortBy));
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadMovieDataTask extends AsyncTask<String, Void, Movies> {
        private final String TAG = DownloadMovieDataTask.class.getSimpleName();

        @Override
        protected Movies doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            if (NetworkUtils.isInternetAvailable(MovieListActivity.this)) {
                Log.d(TAG, "doInBackground: Downloading movie data...");
                String jsonString = NetworkUtils.downloadData(urls[0]);

                try {
                    // Parse the JSON string into our model layer
                    return TheMovieDbJsonParser.parseMovieData(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            // Network is disabled, monitor it so we know when it is back online
            else if (!mNetworkConnectionMonitored){
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                mNetworkChangeReceiver = new MyNetworkChangeReceiver();
                registerReceiver(mNetworkChangeReceiver, filter);
                mNetworkConnectionMonitored = true;
            }

            return null;
        }

        // Displays the results of the AsyncTask
        @Override
        protected void onPostExecute(Movies moviesData) {
            updateUI(moviesData);
        }
    }

    private void updateUI(Movies movies) {
        if (movies == null) {
            // Show empty view
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }

        else {
            // TEST
            mMoviesData = new ArrayList<MovieInfo>(Arrays.asList(movies.getResults()));

            // Show movie list and hide empty view
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);

            mRecyclerView.setAdapter(
                    new MovieListAdapter(this, mTwoPane, movies.getResults()));
        }
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,
                getResources().getInteger(R.integer.recyclerview_columns)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_sort_order) {
            boolean bSortCriteriaValid = true;
            Log.d(TAG, "onOptionsItemSelected: toggle sortOrder");

            // Select next sorting criteria
            mSortBy = (mSortBy + 1) >= MovieDataUrlBuilder.SORT_CRITERIA_COUNT? 0: mSortBy + 1;
            Log.d(TAG, "onOptionsItemSelected: mSortBy = " + mSortBy);

            // Update UI with next sorting criteria
            if (mSortBy == MovieDataUrlBuilder.SORT_BY_HIGHEST_RATED) {
                item.setTitle(R.string.sort_by_highest_rated);
            }

            else if (mSortBy == MovieDataUrlBuilder.SORT_BY_MOST_POPULAR) {
                item.setTitle(R.string.sort_by_most_popular);
            }
            
            else {
                Log.d(TAG, "onOptionsItemSelected: mSortBy value is invalid");
                bSortCriteriaValid = false;
            }

            // Download movies based on sort criteria and update UI
            if (bSortCriteriaValid) {
                new DownloadMovieDataTask()
                        .execute(MovieDataUrlBuilder.buildDiscoverUrl(TMDB_API_KEY, mSortBy));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyNetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean bNetworkConnected = NetworkUtils.isInternetAvailable(context);

            if (bNetworkConnected) {
                stopMonitoringNetworkStatus();
                downloadMovieData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        stopMonitoringNetworkStatus();

        // Cancel task if not completed
        if (mDownloadMovieDataTask != null) {
            boolean bCanceled = mDownloadMovieDataTask.cancel(true);
        }

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save currently displayed movie info
        if (mMoviesData != null) {
            MovieInfo[] movieArray = mMoviesData.toArray(new MovieInfo[mMoviesData.size()]);
            outState.putParcelableArray(
                    getString(R.string.instance_state_key_movies_data),
                    movieArray);
        }
    }

    private void stopMonitoringNetworkStatus() {
        if (mNetworkConnectionMonitored) {
            unregisterReceiver(mNetworkChangeReceiver);
            mNetworkConnectionMonitored = false;
        }
    }
}
