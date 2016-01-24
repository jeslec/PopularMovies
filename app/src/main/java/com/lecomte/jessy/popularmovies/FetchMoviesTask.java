package com.lecomte.jessy.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import com.lecomte.jessy.myhttpurlconnectionlib.MyHttpUrlConnectionUtils;

/**
 * Created by Jessy on 2016-01-23.
 */
// Uses AsyncTask to create a task away from the main UI thread. This task takes a
// URL string and uses it to create an HttpUrlConnection. Once the connection
// has been established, the AsyncTask downloads the contents of the webpage as
// an InputStream. Finally, the InputStream is converted into a string, which is
// displayed in the UI by the AsyncTask's onPostExecute method.
public class FetchMoviesTask extends AsyncTask<String, Void, String> {

    private static final String TAG = FetchMoviesTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        //try {
            return MyHttpUrlConnectionUtils.downloadWebPage(urls[0]);
        //} catch (IOException e) {
        //    return "Unable to retrieve web page. URL may be invalid.";
        //}
    }

    // Displays the results of the AsyncTask
    @Override
    protected void onPostExecute(String jsonString) {
        //textView.setText(result);
        Log.d(TAG, jsonString);
    }
}
