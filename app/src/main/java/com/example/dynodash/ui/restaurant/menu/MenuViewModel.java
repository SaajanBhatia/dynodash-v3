package com.example.dynodash.ui.restaurant.menu;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends ViewModel {

    private DatabaseReference mDatabase;
    private MutableLiveData<List<MenuRestaurantItem>> mMenuItems;

    public MenuViewModel() {
        mMenuItems = new MutableLiveData<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public LiveData<List<MenuRestaurantItem>> getMenuItems(String restaurantUid) {
        mDatabase.child("restaurants").child(restaurantUid).child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MenuRestaurantItem> menuItems = new ArrayList<>();
                for (DataSnapshot menuItemSnapshot : snapshot.getChildren()) {

                    // Get Menu Item Attributes
                    String id = menuItemSnapshot.getKey();
                    String name = menuItemSnapshot.child("name").getValue(String.class);
                    String description = menuItemSnapshot.child("description").getValue(String.class);
                    Double price = menuItemSnapshot.child("price").getValue(Double.class);

                    MenuRestaurantItem menuItem = new MenuRestaurantItem(id, name, description, price);
                    menuItems.add(menuItem);
                }
                mMenuItems.postValue(menuItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        return mMenuItems;
    }
}
