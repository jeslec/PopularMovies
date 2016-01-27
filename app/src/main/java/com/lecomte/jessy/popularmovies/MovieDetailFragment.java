package com.lecomte.jessy.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecomte.jessy.mythemoviedblib.MovieDataUrlBuilder;
import com.lecomte.jessy.mythemoviedblib.data.Results;
import com.squareup.picasso.Picasso;

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

    /**
     * The movie data to display
     */
    private Results mItem;

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

        }

        return rootView;
    }
}
