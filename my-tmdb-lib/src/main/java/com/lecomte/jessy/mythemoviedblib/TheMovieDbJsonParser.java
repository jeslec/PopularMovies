package com.lecomte.jessy.mythemoviedblib;

import com.lecomte.jessy.mythemoviedblib.data.MovieInfo;
import com.lecomte.jessy.mythemoviedblib.data.Movies;
import com.lecomte.jessy.mythemoviedblib.data.Results;
import com.lecomte.jessy.mythemoviedblib.data.Reviews;
import com.lecomte.jessy.mythemoviedblib.data.TrailerInfo;
import com.lecomte.jessy.mythemoviedblib.data.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jessy on 2016-01-24.
 */
public class TheMovieDbJsonParser {
    private static final String TAG = TheMovieDbJsonParser.class.getSimpleName();

    public static Movies parseMovieData(String jsonString)  throws JSONException {

        Movies movies = new Movies();

        // These are the names of the JSON objects that need to be extracted
        // TMDB: The Movie DataBase
        final String TMDB_TOTAL_RESULTS     = "total_results";
        final String TMDB_TOTAL_PAGES       = "total_pages";
        final String TMDB_PAGE              = "page";
        final String TMDB_RESULTS           = "results";
        final String TMDB_POSTER_PATH       = "poster_path";
        final String TMDB_ADULT             = "adult";
        final String TMDB_OVERVIEW          = "overview";
        final String TMDB_RELEASE_DATE      = "release_date";
        final String TMDB_GENRE_IDS         = "genre_ids";
        final String TMDB_ID                = "id";
        final String TMDB_ORIGINAL_TITLE    = "original_title";
        final String TMDB_ORIGINAL_LANGUAGE = "original_language";
        final String TMDB_TITLE             = "title";
        final String TMDB_BACKDROP_PATH     = "backdrop_path";
        final String TMDB_POPULARITY        = "popularity";
        final String TMDB_VOTE_COUNT        = "vote_count";
        final String TMDB_VIDEO             = "video";
        final String TMDB_VOTE_AVERAGE      = "vote_average";

        JSONObject moviesJsonObj = new JSONObject(jsonString);

        movies.setPage(moviesJsonObj.getString(TMDB_PAGE));
        movies.setTotal_results(moviesJsonObj.getString(TMDB_TOTAL_RESULTS));
        movies.setTotal_pages(moviesJsonObj.getString(TMDB_TOTAL_PAGES));

        JSONArray moviesArray = moviesJsonObj.getJSONArray(TMDB_RESULTS);
        MovieInfo[] resultArray = new MovieInfo[moviesArray.length()];

        for(int i = 0; i < moviesArray.length(); i++) {
            resultArray[i] = new MovieInfo();

            // Get the JSON object representing a movie
            JSONObject movie = moviesArray.getJSONObject(i);

            // Extract data from the JSON object
            resultArray[i].setPoster_path(movie.getString(TMDB_POSTER_PATH));
            resultArray[i].setAdult(movie.getString(TMDB_ADULT));
            resultArray[i].setOverview(movie.getString(TMDB_OVERVIEW));
            resultArray[i].setRelease_date(movie.getString(TMDB_RELEASE_DATE));
            resultArray[i].setId(movie.getString(TMDB_ID));
            resultArray[i].setOriginal_title(movie.getString(TMDB_ORIGINAL_TITLE));
            resultArray[i].setOriginal_language(movie.getString(TMDB_ORIGINAL_LANGUAGE));
            resultArray[i].setTitle(movie.getString(TMDB_TITLE));
            resultArray[i].setBackdrop_path(movie.getString(TMDB_BACKDROP_PATH));
            resultArray[i].setPopularity(movie.getString(TMDB_POPULARITY));
            resultArray[i].setVote_count(movie.getString(TMDB_VOTE_COUNT));
            resultArray[i].setVideo(movie.getString(TMDB_VIDEO));
            resultArray[i].setVote_average(movie.getString(TMDB_VOTE_AVERAGE));

            JSONArray genreIdsArray = movie.getJSONArray(TMDB_GENRE_IDS);
            String[] genre_ids = new String[genreIdsArray.length()];

            for (int idIndex=0; idIndex < genreIdsArray.length(); idIndex++) {
                genre_ids[idIndex] = genreIdsArray.getString(idIndex);
            }

            resultArray[i].setGenre_ids(genre_ids);
        }

        movies.setResults(resultArray);

        return movies;
    }

    public static Trailers parseTrailerData(String jsonString)  throws JSONException {

        Trailers trailers = new Trailers();

        // These are the names of the JSON objects that need to be extracted
        // TMDB: The Movie DataBase
        final String TMDB_TRAILER_ID        = "id";
        final String TMDB_TRAILER_KEY       = "key";
        final String TMDB_TRAILER_NAME      = "name";
        final String TMDB_TRAILER_RESULTS   = "results";

        JSONObject trailersJsonObj = new JSONObject(jsonString);

        // Movie ID for which we are getting the trailers for
        trailers.setId(trailersJsonObj.getString(TMDB_TRAILER_ID));

        // Array of trailer info
        JSONArray trailersArray = trailersJsonObj.getJSONArray(TMDB_TRAILER_RESULTS);
        TrailerInfo[] resultArray = new TrailerInfo[trailersArray.length()];

        for(int i = 0; i < trailersArray.length(); i++) {
            resultArray[i] = new TrailerInfo();

            // Get the JSON object representing a movie
            JSONObject trailer = trailersArray.getJSONObject(i);

            // Extract data from the JSON object
            resultArray[i].setId(trailer.getString(TMDB_TRAILER_ID));
            resultArray[i].setKey(trailer.getString(TMDB_TRAILER_KEY));
            resultArray[i].setName(trailer.getString(TMDB_TRAILER_NAME));
        }

        trailers.setResults(resultArray);

        return trailers;
    }

    public static Reviews parseReviewsData(String jsonString) throws JSONException {

        Reviews reviews = new Reviews();

        // These are the names of the JSON objects that need to be extracted
        // TMDB: The Movie DataBase
        final String TMDB_REVIEW_ID         = "id";
        final String TMDB_REVIEW_PAGE       = "page";
        final String TMDB_REVIEW_RESULTS    = "results";
        final String TMDB_REVIEW_AUTHOR     = "author";
        final String TMDB_REVIEW_CONTENT    = "content";
        final String TMDB_REVIEW_URL        = "url";

        JSONObject reviewsJsonObj = new JSONObject(jsonString);

        // Movie ID for which we are getting the trailers for
        reviews.setId(reviewsJsonObj.getString(TMDB_REVIEW_ID));

        reviews.setPage(reviewsJsonObj.getString(TMDB_REVIEW_PAGE));

        // Array of review info
        JSONArray reviewsArray = reviewsJsonObj.getJSONArray(TMDB_REVIEW_RESULTS);
        Results[] resultArray = new Results[reviewsArray.length()];

        for(int i = 0; i < reviewsArray.length(); i++) {
            resultArray[i] = new Results();

            // Get the JSON object representing a movie
            JSONObject trailer = reviewsArray.getJSONObject(i);

            // Extract data from the JSON object
            resultArray[i].setId(trailer.getString(TMDB_REVIEW_ID));
            resultArray[i].setAuthor(trailer.getString(TMDB_REVIEW_AUTHOR));
            resultArray[i].setContent(trailer.getString(TMDB_REVIEW_CONTENT));
            resultArray[i].setUrl(trailer.getString(TMDB_REVIEW_URL));
        }

        reviews.setResults(resultArray);

        return reviews;
    }
}
