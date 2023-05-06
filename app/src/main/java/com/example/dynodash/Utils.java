package com.example.dynodash;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.dynodash.ui.customer.CustomerActivity;
import com.example.dynodash.ui.customer.CustomerRestaurantActivity;
import com.example.dynodash.ui.customer.list.RestaurantListItem;
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

    private static final String CHANNEL_ID = "dynodash_channel";
    private static final String CHANNEL_NAME = "Dynodash";
    private static final String CHANNEL_DESC = "Order your food :)";

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

    // Overloading Method for forwarding QR code details
    public static void forwardUserOnRole(String uid, AppCompatActivity context, String restaurantID, String tableNumber) {
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
                        intent.putExtra("restaurantID", restaurantID);
                        intent.putExtra("tableNumber", tableNumber);
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

    // Forward User on QR Code
    public static void forwardUserOnQRCode(String restaurantID, String uid, AppCompatActivity context, String table) {

        FirebaseDatabase.getInstance().getReference().child("restaurants").child(restaurantID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Object> restaurantMap = (Map<String, Object>) snapshot.getValue();
                    String name = (String) restaurantMap.get("name");
                    String address = (String) restaurantMap.get("address");
                    String description = (String) restaurantMap.get("description");

                    // Create Restaurant List Item, check if already exists
                    DBHelper dbHelper = new DBHelper(context);
                    RestaurantListItem item = new RestaurantListItem(name, description, address, restaurantID);
                    if (!dbHelper.restaurantExists(item)) {
                        dbHelper.addRestaurant(item);
                    }

                    Log.d("Fetched Details", "onDataChange: name, desc and id" + name + description + address);

                    // Forward User
                    Intent intent = new Intent(context, CustomerRestaurantActivity.class);
                    intent.putExtra("restaurantID", restaurantID);
                    intent.putExtra("restaurantName", name);
                    intent.putExtra("restaurantDesc", description);
                    intent.putExtra("restaurantAddr", address);
                    intent.putExtra("tableNumber", table); // Pass the table number
                    context.startActivity(intent);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Utils QR Forward", "Error retrieving data", error.toException());
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

    // Generate a Collision Resistant User ID for Real Time Database use
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

    // Push notifications globally
    public static void pushNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // set priority to high
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }


}
