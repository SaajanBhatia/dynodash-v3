package com.example.dynodash.ui.restaurant;

import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;

public class RestaurantViewModel extends ViewModel {

    private FirebaseUser user;

    public RestaurantViewModel(FirebaseUser user) {
        this.user = user;
    }

    // TODO: Implement methods for restaurant-related logic (e.g., managing menu items, processing orders)
}
