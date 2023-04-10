package com.example.dynodash.ui.restaurant.code;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class CodeViewModel extends ViewModel {

    // Database reference
    private final DatabaseReference databaseReference;

    public CodeViewModel() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void getTableList(String restaurantId) {
        ArrayList<CodeListItem> tableItems = new ArrayList<CodeListItem>();
        // Make reference
        databaseReference.child("restaurants").child(restaurantId).child("tables").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("CodeViewModel", "onComplete: Failed to retrieve number of tables" );
                } else {
                    // Recurse through number of tables, create n objects
                    for (int i = 1; i < (Integer) task.getResult().getValue(); i++) {
                        tableItems.add( new CodeListItem(i) );
                    }
                }
            }
        });
    }
}
