package com.lecomte.jessy.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lecomte.jessy.mynetworklib.NetworkUtils;

/**
 * Created by Jessy on 2016-03-27.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean bNetworkConnected = NetworkUtils.isInternetAvailable(context);
        Toast.makeText(context, "Network connected: " + bNetworkConnected, Toast.LENGTH_LONG).show();
    }
}
