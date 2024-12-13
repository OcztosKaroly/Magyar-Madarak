package com.example.magyar_madarak.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ResourceAvailabilityUtils {
    private static final String LOG_TAG = "RESOURCES";

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            Log.i(LOG_TAG, "--Internet available.--");
            return true;
        }
        Log.w(LOG_TAG, "--Internet unavailable.--");
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.i(LOG_TAG, "--Wifi available.--");
                return true;
            }
            Log.w(LOG_TAG, "--Wifi unavailable. Only Internet available.--");
        }
        Log.w(LOG_TAG, "--Wifi unavailable. Internet unavailable.--");
        return false;
    }
}
