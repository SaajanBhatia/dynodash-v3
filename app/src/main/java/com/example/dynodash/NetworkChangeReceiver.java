package com.example.dynodash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

import com.example.dynodash.ui.Error;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            // The device is connected to the internet
        } else {
            // The device is not connected to the internet, show an error screen
            Intent errorIntent = new Intent(context, Error.class);
            errorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(errorIntent);
        }
    }
}
