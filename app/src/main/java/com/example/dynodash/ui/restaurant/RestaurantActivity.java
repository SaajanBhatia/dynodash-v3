package com.example.dynodash.ui.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dynodash.R;
import com.example.dynodash.databinding.ActivityMainBinding;
import com.example.dynodash.databinding.RestaurantLayoutBinding;
import com.example.dynodash.ui.restaurant.code.CodeFragment;
import com.example.dynodash.ui.restaurant.menu.MenuFragment;
import com.example.dynodash.ui.restaurant.orders.OrdersFragment;
import com.example.dynodash.ui.restaurant.tables.TablesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class RestaurantActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_layout);

        // Get the NavController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
