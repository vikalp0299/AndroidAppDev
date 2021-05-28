package com.example.connect.AuthenticationActivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkNotifier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }
//    private static final String TAG = "NetworkNotifier";
//    public static boolean isActive = false;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//    }
//
//
//    //returns internet connection
//    public boolean isOnline(Context context) {
//        try {
//            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            connectivityManager.registerDefaultNetworkCallback();
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
}
