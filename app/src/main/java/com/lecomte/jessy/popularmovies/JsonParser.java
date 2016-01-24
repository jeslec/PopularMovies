package com.lecomte.jessy.popularmovies;

import com.lecomte.jessy.popularmovies.data.Movies;
import com.lecomte.jessy.popularmovies.data.Results;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jessy on 2016-01-24.
 */
public class JsonParser {
    private static final String TAG = JsonParser.class.getSimpleName();

    public static Movies parse(String jsonString)  throws JSONException {

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
        Results[] resultArray = new Results[moviesArray.length()];

        for(int i = 0; i < moviesArray.length(); i++) {
            resultArray[i] = new Results();

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
}
