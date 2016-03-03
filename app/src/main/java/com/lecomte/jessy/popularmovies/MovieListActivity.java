package com.lecomte.jessy.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lecomte.jessy.mynetworklib.NetworkUtils;
import com.lecomte.jessy.mythemoviedblib.MovieDataUrlBuilder;
import com.lecomte.jessy.mythemoviedblib.TheMovieDbJsonParser;
import com.lecomte.jessy.mythemoviedblib.data.Movies;

import org.json.JSONException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity_old} representing
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        mRecyclerView = (RecyclerView)findViewById(R.id.movie_list);
        assert mRecyclerView != null;
        setupRecyclerView();

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        new downloadMovieDataTask()
                .execute(MovieDataUrlBuilder.buildDiscoverUrl(TMDB_API_KEY, mSortBy));
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class downloadMovieDataTask extends AsyncTask<String, Void, Movies> {
        private final String TAG = downloadMovieDataTask.class.getSimpleName();

        @Override
        protected Movies doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            String jsonString = NetworkUtils.downloadData(urls[0]);

            try {
                // Parse the JSON string into our model layer
                return TheMovieDbJsonParser.parseMovieData(jsonString);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
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
            Log.d(TAG, "Movies data is null!");
            return;
        }

        Log.d(TAG, movies.toString());
        mRecyclerView.setAdapter(
                new MovieListRecyclerViewAdapter(this, mTwoPane, movies.getResults()));
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

        if (id == R.id.action_sortOrder) {
            boolean bSortCriteriaValid = true;
            Log.d(TAG, "onOptionsItemSelected: toggle sortOrder");

            // Select next sorting criteria
            mSortBy = (mSortBy + 1) >= MovieDataUrlBuilder.SORT_CRITERIA_COUNT? 0: mSortBy + 1;
            Log.d(TAG, "onOptionsItemSelected: mSortBy = " + mSortBy);

            // Update UI with next sorting criteria
            if (mSortBy == MovieDataUrlBuilder.SORT_BY_HIGHEST_RATED) {
                item.setTitle(R.string.sort_by_HighestRated);
            }

            else if (mSortBy == MovieDataUrlBuilder.SORT_BY_MOST_POPULAR) {
                item.setTitle(R.string.sort_by_MostPopular);
            }
            
            else {
                Log.d(TAG, "onOptionsItemSelected: mSortBy value is invalid");
                bSortCriteriaValid = false;
            }

            // Download movies based on sort criteria and update UI
            if (bSortCriteriaValid) {
                new downloadMovieDataTask()
                        .execute(MovieDataUrlBuilder.buildDiscoverUrl(TMDB_API_KEY, mSortBy));
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
