package com.example.dynodash.ui.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dynodash.R;

/*
 Main Page for Customer
 TODO recyclerview for cached locations
 TODO Search for restaurant name
*/

public class CustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_layout);

        // Retrieve the UID
        Intent intent = getIntent();
        String customerID = intent.getStringExtra("USER_ID");

        // Add your code here
    }
}
