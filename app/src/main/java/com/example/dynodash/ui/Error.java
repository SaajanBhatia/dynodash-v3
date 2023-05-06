package com.example.dynodash.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dynodash.R;

public class Error extends AppCompatActivity {

    private Button retryButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        retryButton = findViewById(R.id.retry_button);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectedToInternet()) {
                    // Dismiss the error message and continue with the operation
                    finish();
                } else {
                    // Show a message indicating that the device is not connected to the internet
                    Toast.makeText(Error.this, "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
