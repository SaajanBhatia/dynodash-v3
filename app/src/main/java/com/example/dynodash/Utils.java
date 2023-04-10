package com.example.dynodash;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dynodash.ui.customer.CustomerActivity;
import com.example.dynodash.ui.login.LoginActivity;
import com.example.dynodash.ui.restaurant.RestaurantActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Utils {

    // Get User Role from Real Time Database
    public static void forwardUserOnRole(String uid, AppCompatActivity context) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(uid).child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("getUserRole: firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("getUserRole: firebase", "ROLE: " + String.valueOf(task.getResult().getValue()));

                    if (String.valueOf(task.getResult().getValue()).equals("CUSTOMER")) {
                        // If user is customer inflate CustomerActivity
                        Intent intent = new Intent(context, CustomerActivity.class);
                        intent.putExtra("USER_ID", uid);
                        context.startActivity(intent);
                        context.finish();
                    } else if (String.valueOf(task.getResult().getValue()).equals("RESTAURANT")) {
                        // If user is restaurant inflate RestaurantActivity
                        Intent intent = new Intent(context, RestaurantActivity.class);
                        intent.putExtra("USER_ID", uid);
                        context.startActivity(intent);
                        context.finish();
                    } else {
                        Log.d("MyApp", "The account type was unknown");
                        Toast.makeText(context, "Could not find account role for " + uid, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }



    // Set User Role and Name in Real Time Database
    public static void setUserRole(String uid, String role, String name) {
        // Get a reference to the restaurants node in the database
        DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Create a new child node with the restaurant's user ID as the key
        DatabaseReference newRestaurantRef = restaurantsRef.child(uid);

        newRestaurantRef.child("name").setValue(name);
        newRestaurantRef.child("role").setValue(role.toUpperCase());
    }

    // Get Name from Real Time Database


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

        // Set default number of tables as 10
        newRestaurantRef.child("tables").setValue(10);
    }

    public static String generateCUID() {
        StringBuilder sb = new StringBuilder();

        // Append the device model name
        sb.append(Build.MODEL);

        // Append the Android version number
        sb.append(Build.VERSION.RELEASE);

        // Append a random UUID
        sb.append(UUID.randomUUID().toString());

        // Hash the resulting string using MD5
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(sb.toString().getBytes());
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02X", b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the error here
            e.printStackTrace();
            return null;
        }
    }




}
