package com.lecomte.jessy.popularmovies;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lecomte.jessy.mynetworklib.NetworkUtils;
import com.lecomte.jessy.mythemoviedblib.MovieDataUrlBuilder;
import com.lecomte.jessy.mythemoviedblib.TheMovieDbJsonParser;
import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.lecomte.jessy.mythemoviedblib.data.TrailerInfo;
import com.lecomte.jessy.mythemoviedblib.data.Trailers;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
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

    ListView mTrailerListView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = getArguments().getParcelable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }

            new downloadTrailerDataTask().execute(MovieDataUrlBuilder.buildTrailersUrl(
                    MovieListActivity.TMDB_API_KEY, mItem.getId()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        // Show the movie data
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.detail_summary_TextView)).setText(mItem.getOverview());

            // Movie poster
            ImageView poster = (ImageView)rootView.findViewById(R.id.detail_poster_ImageView);
            String posterUrl = MovieDataUrlBuilder.buildPosterUrl(mItem.getPoster_path());
            Picasso.with(getContext()).load(posterUrl).into(poster);

            // Release date
            ((TextView)rootView.findViewById(R.id.detail_releaseDate_TextView))
                    .setText(mItem.getRelease_date());

            // Vote average
            ((TextView)rootView.findViewById(R.id.detail_voteAverage_TextView))
                    .setText(mItem.getVote_average() + AVERAGE_RATING_SUFFIX);

            // Trailers
            mTrailerListView = (ListView)rootView.findViewById(R.id.detail_trailers_ListView);
            assert mTrailerListView != null;

            // http://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view#19311197
            mTrailerListView.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);

                    TextView clickedView = (TextView) v;
                    String viewText = (String)clickedView.getText();

                    return false;
                }
            });

            /*mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
            */
        }

        return rootView;
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class downloadTrailerDataTask extends AsyncTask<String, Void, Trailers> {
        private final String TAG = downloadTrailerDataTask.class.getSimpleName();

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
            updateUI(trailersData.getResults());
            Log.d(TAG, trailersData.toString());
        }
    }

    private void updateUI(TrailerInfo[] trailerArray) {
        if (trailerArray == null) {
            Log.d(TAG, "Trailers data is null!");
            return;
        }
        mTrailerListView.setAdapter(new TrailerListViewAdapter(trailerArray));
        setListViewHeightBasedOnChildren(mTrailerListView);
    }

    /*
        Method for Setting the Height of the ListView dynamically.
        Hack to fix the issue of not showing all the items of the ListView
        when placed inside a ScrollView

        http://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view#19311197
    */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
