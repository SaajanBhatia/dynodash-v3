package com.example.dynodash.ui.restaurant.menu;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dynodash.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends ViewModel {

    private final DatabaseReference databaseReference;
    private final MutableLiveData<List<MenuRestaurantItem>> menuItemsLiveData;

    public MenuViewModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        menuItemsLiveData = new MutableLiveData<>();
    }

    public void addMenuItem(String restaurantId, MenuRestaurantItem newItem) {
        DatabaseReference menuRef = databaseReference.child("restaurants").child(restaurantId).child("menu").child(newItem.getId());
        menuRef.child("name").setValue(newItem.getName());
        menuRef.child("description").setValue(newItem.getDescription());
        menuRef.child("price").setValue(newItem.getPrice());
    }

    public void updateMenuItem(String restaurantId, MenuRestaurantItem itemToUpdate) {
        DatabaseReference menuRef = databaseReference.child("restaurants").child(restaurantId).child("menu").child(itemToUpdate.getId());
        menuRef.child("name").setValue(itemToUpdate.getName());
        menuRef.child("description").setValue(itemToUpdate.getDescription());
        menuRef.child("price").setValue(itemToUpdate.getPrice());
    }

    public void deleteMenuItem(String restaurantId, String menuRestaurantItemID) {
        // Get a reference to the key you want to delete
        DatabaseReference keyRef =databaseReference.child("restaurants").child(restaurantId).child("menu").child(menuRestaurantItemID);

        // Call removeValue() to delete the key
        keyRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MenuViewModel:56", "onSuccess: Key Successfully Removed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MenuViewModel:62", "onFailure: Key Unable to delete: " + e.toString());
                    }
                });
    }

    public LiveData<List<MenuRestaurantItem>> getMenuItems(String restaurantId) {
        Query query = databaseReference.child("restaurants").child(restaurantId).child("menu");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MenuRestaurantItem> menuItems = new ArrayList<>();
                for (DataSnapshot menuItemSnapshot : snapshot.getChildren()) {
                    String id = menuItemSnapshot.getKey();
                    String name = menuItemSnapshot.child("name").getValue(String.class);
                    String description = menuItemSnapshot.child("description").getValue(String.class);
                    Double price = menuItemSnapshot.child("price").getValue(Double.class);
                    MenuRestaurantItem menuItem = new MenuRestaurantItem(id, name, description, price);
                    menuItems.add(menuItem);
                }
                menuItemsLiveData.postValue(menuItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
        return menuItemsLiveData;
    }
}