package com.example.dynodash.ui.customer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerViewModel extends ViewModel {

    private DatabaseReference databaseReference;

    public CustomerViewModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public LiveData<List<RestaurantListItem>> getSearchResults(String searchQuery) {
        MutableLiveData<List<RestaurantListItem>> searchResultsLiveData = new MutableLiveData<>();

        Query query = databaseReference.child("restaurants");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<RestaurantListItem> searchResultsList = new ArrayList<>();

                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                    String id = restaurantSnapshot.getKey();
                    String name = restaurantSnapshot.child("name").getValue(String.class);
                    String description = restaurantSnapshot.child("description").getValue(String.class);
                    String address = restaurantSnapshot.child("address").getValue(String.class);

                    if (name != null && name.toLowerCase().startsWith(searchQuery.toLowerCase())) {
                        RestaurantListItem restaurant = new RestaurantListItem(
                                name,
                                description,
                                address,
                                id
                        );
                        searchResultsList.add(restaurant);
                    }
                }

                searchResultsLiveData.setValue(searchResultsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("OnCancelled@CustomerViewModel:57", "getSearchResults:onCancelled", error.toException());
                searchResultsLiveData.setValue(null);
            }
        });

        return searchResultsLiveData;
    }
}
