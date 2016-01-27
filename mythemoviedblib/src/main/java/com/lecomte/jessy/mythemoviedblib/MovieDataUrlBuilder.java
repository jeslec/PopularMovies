package com.lecomte.jessy.mythemoviedblib;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Jessy on 2016-01-26.
 */
public class MovieDataUrlBuilder {
    private static final String TAG = MovieDataUrlBuilder.class.getSimpleName();

    // API Endpoints for TMDB (The Movie DataBase)

    // Discover URL
    private static final String TMDB_URL_DISCOVER =
            "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";

    // Reviews URL
    // URL = TMDB_URL_REVIEWS_PREFIX + { movieID } + TMDB_URL_REVIEWS_SUFFIX + TMDB_API_KEY_PARAM
    // Example of a well formed URL to query the reviews for a movie with ID 76341:
    // http://api.themoviedb.org/3/movie/76341/reviews?api_key=1ed96e22fff439407e05fbfbb876aa3b
    private static final String TMDB_URL_REVIEWS_PREFIX = "http://api.themoviedb.org/3/movie/";
    private static final String TMDB_URL_REVIEWS_SUFFIX = "/reviews?api_key=";

    // Trailers URL
    // URL = TMDB_URL_TRAILERS_PREFIX + { movieID } + TMDB_URL_TRAILERS_SUFFIX + TMDB_API_KEY_PARAM
    // Example of a well formed URL to query the trailers for a movie with ID 76341:
    //http://api.themoviedb.org/3/movie/76341/videos?api_key=1ed96e22fff439407e05fbfbb876aa3b
    private static final String TMDB_URL_TRAILERS_PREFIX = "http://api.themoviedb.org/3/movie/";
    private static final String TMDB_URL_TRAILERS_SUFFIX = "/videos?api_key=";

    // TODO: this value is now public (instead of private) because I access it also from the
    // details fragment. Should this be somewhere else, perhaps? In preferences, maybe???
    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342";//w185";

    public static String buildDiscoverUrl(@NonNull String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            Log.d(TAG, "buildDiscoverUrl() - API key is invalid");
            return null;
        }
        return TMDB_URL_DISCOVER + apiKey;
    }

    public static String buildReviewsUrl(@NonNull String apiKey, @NonNull String movieID) {
        if (apiKey == null || apiKey.isEmpty()) {
            Log.d(TAG, "buildReviewsUrl() - API key is invalid");
            return null;
        }

        if (movieID == null || movieID.isEmpty()) {
            Log.d(TAG, "buildReviewsUrl() - Movie ID is invalid");
            return null;
        }
        return TMDB_URL_REVIEWS_PREFIX + movieID + TMDB_URL_REVIEWS_SUFFIX + apiKey;
    }

    public static String buildTrailersUrl(@NonNull String apiKey, @NonNull String movieID) {
        if (apiKey == null || apiKey.isEmpty()) {
            Log.d(TAG, "buildTrailersUrl() - API key is invalid");
            return null;
        }

        if (movieID == null || movieID.isEmpty()) {
            Log.d(TAG, "buildTrailersUrl() - Movie ID is invalid");
            return null;
        }
        return TMDB_URL_TRAILERS_PREFIX + movieID + TMDB_URL_TRAILERS_SUFFIX + apiKey;
    }

    public static String buildPosterUrl(@NonNull String posterPath) {
        if (posterPath == null || posterPath.isEmpty()) {
            Log.d(TAG, "buildPosterUrl() - Poster path is invalid");
            return null;
        }
        return BASE_POSTER_URL + posterPath;
    }
}
