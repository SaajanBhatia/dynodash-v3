package com.example.dynodash.ui.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dynodash.R;
import com.google.firebase.auth.FirebaseUser;

public class CustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_layout);

        // Retrieve the UID
        Intent intent = getIntent();
        String userId = intent.getStringExtra("USER_ID");

        // Add your code here
    }
}
