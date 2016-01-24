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

        // TMDB: The Movie DataBase

        // Max number of results per page
        final int TMDB_RESULTS_PER_PAGE = 20;

        // These are the names of the JSON objects that need to be extracted
        final String TMDB_RESULTS       = "results";
        final String TMDB_POSTER_PATH   = "poster_path";
        final String TMDB_TITLE         = "title";

        JSONObject moviesJson = new JSONObject(jsonString);
        JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        /*Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();*/

        Results[] resultArray = new Results[moviesArray.length()];

        for(int i = 0; i < moviesArray.length(); i++) {

            resultArray[i] = new Results();

            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            // Get the JSON object representing a movie
            JSONObject movie = moviesArray.getJSONObject(i);

            resultArray[i].setTitle(movie.getString(TMDB_TITLE));
            resultArray[i].setPoster_path(movie.getString(TMDB_POSTER_PATH));

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            /*long dateTime;
            // Cheating to convert this to UTC time, which is what we want anyhow
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;*/
        }

        movies.setResults(resultArray);

        return movies;
    }
}
