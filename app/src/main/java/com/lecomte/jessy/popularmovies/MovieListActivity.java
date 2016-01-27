package com.lecomte.jessy.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lecomte.jessy.mynetworklib.NetworkUtils;
import com.lecomte.jessy.mythemoviedblib.TheMovieDbJsonParser;
import com.lecomte.jessy.mythemoviedblib.MovieDataUrlBuilder;
import com.lecomte.jessy.mythemoviedblib.data.Movies;
import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        new downloadMovieDataTask().execute(MovieDataUrlBuilder.buildDiscoverUrl(TMDB_API_KEY));
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
        mRecyclerView.setAdapter(new RecyclerViewAdapter(movies.getResults()));
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,
                getResources().getInteger(R.integer.recyclerview_columns)));

        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class RecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        // Data to be displayed in the RecyclerView
        private ArrayList<MovieInfo> mData;

        public RecyclerViewAdapter(MovieInfo[] resultsArray) {
            mData = new ArrayList<MovieInfo>(Arrays.asList(resultsArray));
        }

        /*public void swap(ArrayList<Data> datas){
            data.clear();
            data.addAll(datas);
            notifyDataSetChanged();
        }*/

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mData.get(position);
            String posterUrl = MovieDataUrlBuilder.buildPosterUrl(holder.mItem.getPoster_path());
            //Log.d(TAG, String.format("[%d]: %s", position, posterUrl));

            // Neat trick: we are getting the context from the view itself
            Picasso.with(holder.posterImageView.getContext())
                    .load(posterUrl).into(holder.posterImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(MovieDetailFragment.ARG_ITEM, holder.mItem);
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM, holder.mItem);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public ImageView posterImageView;
            public MovieInfo mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                posterImageView = (ImageView)view.findViewById(R.id.movie_list_poster_ImageView);
            }

            @Override
            public String toString() {
                return super.toString();// + " '";// + mContentView.getText() + "'";
            }
        }
    }

   /* public static int containerHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        *//*LogUtil.dLog(Static.HEIGHT_TAG, "Screen Height of " + Build.MANUFACTURER + " " + Build.DEVICE + " "
                + Build.MODEL + " is " + Integer.toString(dm.heightPixels));*//*

        double ratio = Static.PIC_RATIO_VALUE;

        try {
            if (activity.getDB() != null && activity.getDB().isOpen() && activity.getDB().exists(Static.PIC_RATIO))
                ratio = activity.getDB().getDouble(Static.PIC_RATIO);
        } catch (SnappydbException e) {
            e.printStackTrace();
            LogUtil.dLog(Static.DB_TAG, "SnappydbException | couldn't get Static.PIC_RATIO");
        }

        return (int) (dm.heightPixels / ratio);
    }*/
}
