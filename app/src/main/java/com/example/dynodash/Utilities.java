package com.example.dynodash;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Utilities {

    // Get User Role from Real Time Database
    public static String getUserRole(String uid) {
        // Check if the user ID exists in the "customer" object of "user_roles"
        if (FirebaseDatabase.getInstance().getReference("user_roles/customer").child(uid).getKey() != null) {
            return "customer";
        }

        // Check if the user ID exists in the "restaurant" object of "user_roles"
        if (FirebaseDatabase.getInstance().getReference("user_roles/restaurant").child(uid).getKey() != null) {
            return "restaurant";
        }

        // Return "unknown" if the user ID doesn't exist in either "customer" or "restaurant"
        return "unknown";
    }

    // Set User Role in Real Time Database
    public static void setUserRole(String uid, String role, String name) {
        DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference("user_roles").child(role).child(uid);
        userRoleRef.setValue(name);
        setName(uid, name);
    }

    // Set User Name in Real Time Database
    public static void setName(String uid, String name) {
        DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference("users").child(uid).child("name");
        nameRef.setValue(name);
    }

    // Get Name from Real Time Database
    public static void getName(String uid, final OnGetNameListener listener) {
        String userRole = getUserRole(uid);
        if (userRole.equals("customer")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user_roles/customer").child(uid);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.getValue(String.class);
                    listener.onGetName(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listener.onGetNameError(databaseError.getMessage());
                }
            });
        } else if (userRole.equals("restaurant")) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user_roles/restaurant").child(uid);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.getValue(String.class);
                    listener.onGetName(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listener.onGetNameError(databaseError.getMessage());
                }
            });
        } else {
            listener.onGetNameError("User not found");
        }
    }

    // Add a restaurant to the Real Time Database on account creation
    public static void addRestaurantToDatabase(String uid, String name, String description, String address) {
        // Get a reference to the restaurants node in the database
        DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference().child("restaurants");

        // Create a new child node with the restaurant's user ID as the key
        DatabaseReference newRestaurantRef = restaurantsRef.child(uid);

        // Set the values of the child node to the provided name, description, and address
        newRestaurantRef.child("name").setValue(name);
        newRestaurantRef.child("description").setValue(description);
        newRestaurantRef.child("address").setValue(address);
    }

    public interface OnGetNameListener {
        void onGetName(String name);

        void onGetNameError(String errorMessage);
    }




}
