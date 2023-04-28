package com.example.dynodash.ui.restaurant;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.dynodash.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class RestaurantActivity extends AppCompatActivity {

    private NavController navController;
    private String restaurantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_layout);

        // Retrieve the UID from the Intent
        Intent intent = getIntent();
        restaurantID = intent.getStringExtra("USER_ID");

        // Get the NavController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public String getUid() {
        return restaurantID;
    }
}
