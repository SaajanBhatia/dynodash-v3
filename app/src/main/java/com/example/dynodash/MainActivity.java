package com.example.dynodash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.dynodash.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "dynodash_channel";
    private static final String CHANNEL_NAME = "Dynodash";
    private static final String CHANNEL_DESC = "Order your food :)";

    // Permission Grants
    private static final int WRITE_EXTERNAL_STORAGE = 123;
    private static final int INTERNET = 123;
    private static final int ACCESS_NETWORK_STATE = 132;
    private static final int POST_NOTIFICATIONS = 123;
    private static final int VIBRATE = 123;

    private NetworkChangeReceiver networkChangeReceiver;

    private String restaurantIDQR;
    private String tableNumberQR;

    public boolean verifyPermissions() {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                        &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED
                        &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED
                        &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
                        &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                == PackageManager.PERMISSION_GRANTED)
        {
            // If all permissions granted return true
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();

        // Check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET)) {
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        INTERNET);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_NETWORK_STATE)) {
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE},
                        ACCESS_NETWORK_STATE);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        POST_NOTIFICATIONS);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.VIBRATE)) {
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.VIBRATE},
                        VIBRATE);
            }
        }

        // Set the Main Activity Instance on startup (blank)
        setContentView(R.layout.activity_main);

        // DEBUG SIGN OUT
//        FirebaseAuth.getInstance().signOut();

        Uri data = getIntent().getData();
        if (data != null) {
            // Attach to intent
            restaurantIDQR = data.getQueryParameter("restaurantID");
            tableNumberQR = data.getQueryParameter("table");
        }

        // Check for Network (Broadcast Receiver)
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);


        // Check if user is logged in, if not start LoginActivity
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // To prevent them going backwards bypassing the LoginActivity
        }
        else {
            // Forward the user to the correct component depending on role (Customer or Restaurant)
            if (restaurantIDQR != null && tableNumberQR != null) {
                Utils.forwardUserOnRole(currentUser.getUid(), this, restaurantIDQR, tableNumberQR);
            } else {
                Utils.forwardUserOnRole(currentUser.getUid(),this);
            }
        }
    }

    // Declare Notification Channels
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(networkChangeReceiver);
    }
}
