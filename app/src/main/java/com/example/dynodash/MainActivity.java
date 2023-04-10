package com.example.dynodash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.dynodash.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the Main Activity Instance on startup (blank)
        setContentView(R.layout.activity_main);

        // DEBUG SIGN OUT
        FirebaseAuth.getInstance().signOut();

        // Check if user is logged in, if not start LoginActivity
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // To prevent them going backwards bypassing the LoginActivity
        }
        else {
            // Get the intent carried by QR code if exists
            Intent intent = getIntent();
            if (intent.getData() != null) {
                String restaurantId = intent.getStringExtra("restaurantId");
                String table = intent.getStringExtra("table");
                // Store values to local storage
            }

            // Get account type
            // Forward the user to the correct component depending on role (Customer or Restaurant)
            Utils.forwardUserOnRole(currentUser.getUid(),this);
        }
    }
}
