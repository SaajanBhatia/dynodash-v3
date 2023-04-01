package com.example.dynodash.ui.restaurant.tables;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TablesViewModel extends ViewModel {

    public void updateTables(String uid, int newTablesValue, TextView tableCountTextView) {
        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(uid).child("tables");
        restaurantRef.setValue(newTablesValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Tables value updated successfully
                        Log.d("TAG", "Tables value updated successfully");

                        // Update UI Component
                        readInstanceAndUpdateTextView(uid, tableCountTextView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while updating tables value
                        Log.e("TAG", "Error updating tables value: " + e.getMessage());
                    }
                });
    }

    public void readInstanceAndUpdateTextView(String uid, TextView tableCountTextView) {
        // Make a read request to Firebase Real Time Database
        Log.d("readInstanceAndUpdateTextView:45", "readInstanceAndUpdateTextView: UID amd Textview: " + uid + " " + tableCountTextView.toString());
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("restaurants").child(uid).child("tables").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("TablesViewModel:47", "Error getting number of tables", task.getException());
                } else {
                    Log.d("TablesViewModel:49", "onComplete: Value of tables is " + String.valueOf(task.getResult().getValue()));

                    tableCountTextView.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });


    }

}
