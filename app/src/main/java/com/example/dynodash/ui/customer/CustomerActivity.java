package com.example.dynodash.ui.customer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dynodash.R;

public class CustomerActivity<User> extends AppCompatActivity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get the user object passed from LoginActivity
        mUser = getIntent().getParcelableExtra("user");
    }
}
