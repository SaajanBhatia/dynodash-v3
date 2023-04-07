package com.example.dynodash.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dynodash.R;
import com.example.dynodash.Utils;
import com.example.dynodash.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextInputLayout mEmailTextInputLayout, mNameTextInputLayout, mPasswordTextInputLayout,
            mRestaurantNameLayout, mRestaurantDescLayout, mRestaurantAddressLayout;
    private TextInputEditText mEmailEditText, mNameEditText, mPasswordEditText,
            mRestaurantNameText, mRestaurantDescText, mRestaurantAddressText;
    private SwitchMaterial restaurantSwitch;
    private Button registerButton;
    private Button linkToLoginButton;
    private TextView errorMessageTextView;

    // Firebase Role String Types
    private String restaurantType = new String("restaurant");
    private String customerType = new String("customer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get references to all the UI elements
        mEmailTextInputLayout = findViewById(R.id.registerEmail);
        mNameTextInputLayout = findViewById(R.id.registerName);
        mPasswordTextInputLayout = findViewById(R.id.registerPassword);

        mEmailEditText = (TextInputEditText) mEmailTextInputLayout.getEditText();
        mNameEditText = (TextInputEditText) mNameTextInputLayout.getEditText();
        mPasswordEditText = (TextInputEditText) mPasswordTextInputLayout.getEditText();

        restaurantSwitch = findViewById(R.id.restaurantSwitch);
        registerButton = findViewById(R.id.registerButton);
        errorMessageTextView = findViewById(R.id.errorMessageTextView);
        linkToLoginButton = findViewById(R.id.linkToLoginFrame);

        mRestaurantNameLayout = findViewById(R.id.restaurantName);
        mRestaurantDescLayout = findViewById(R.id.restaurantDescription);
        mRestaurantAddressLayout = findViewById(R.id.restaurantAddress);

        mRestaurantNameText = (TextInputEditText) mRestaurantNameLayout.getEditText();
        mRestaurantDescText = (TextInputEditText) mRestaurantDescLayout.getEditText();
        mRestaurantAddressText = (TextInputEditText) mRestaurantAddressLayout.getEditText();

        // Inflate the restaurant fields
        restaurantSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update visibility of TextView views based on switch state
                if (isChecked) {
                    mRestaurantNameLayout.setVisibility(View.VISIBLE);
                    mRestaurantDescLayout.setVisibility(View.VISIBLE);
                    mRestaurantAddressLayout.setVisibility(View.VISIBLE);
                } else {
                    mRestaurantNameLayout.setVisibility(View.GONE);
                    mRestaurantDescLayout.setVisibility(View.GONE);
                    mRestaurantAddressLayout.setVisibility(View.GONE);
                }
            }
        });

        // Link to Login on click listener
        linkToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Set a click listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the input fields
                String email = mEmailEditText.getText().toString().trim();
                String name = mNameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                String restaurantName = mRestaurantNameText.getText().toString().trim();
                String restaurantDesc = mRestaurantDescText.getText().toString().trim();
                String restaurantAddress = mRestaurantAddressText.getText().toString().trim();

                // Validate the input fields
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmailTextInputLayout.setError("Please enter a valid email address");
                    return;
                } else {
                    mEmailTextInputLayout.setError(null);
                }

                if (name.isEmpty()) {
                    mNameTextInputLayout.setError("Please enter a name");
                    return;
                } else {
                    mNameTextInputLayout.setError(null);
                }

                if (password.isEmpty() || password.length() < 6) {
                    mPasswordTextInputLayout.setError("Password must be at least 6 characters long");
                    return;
                } else {
                    mPasswordTextInputLayout.setError(null);
                }

                // Get the value of the restaurant switch
                boolean isRestaurantAccount = restaurantSwitch.isChecked();

                // Reject restaurant submission if state checked
                if (isRestaurantAccount) {
                    if (restaurantName.isEmpty()) {
                        mRestaurantNameLayout.setError("Please enter a name");
                        return;
                    } else {
                        mRestaurantNameLayout.setError(null);
                    }

                    if (restaurantDesc.isEmpty()) {
                        mRestaurantDescLayout.setError("Please enter a description");
                        return;
                    } else {
                        mRestaurantDescLayout.setError(null);
                    }

                    if (restaurantAddress.isEmpty()) {
                        mRestaurantAddressLayout.setError("Please enter an address");
                        return;
                    } else {
                        mRestaurantAddressLayout.setError(null);
                    }
                }

                // Registration Logic
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registration successful, get user object
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (isRestaurantAccount) {
                                        // Set Restaurant Account Type
                                        Utils.setUserRole(user.getUid(), restaurantType, name);
                                        // Set the restaurant details in the database
                                        Utils.addRestaurantToDatabase(user.getUid(), restaurantName, restaurantDesc, restaurantAddress);
                                    } else {
                                        // Else set customer account type
                                        Utils.setUserRole(user.getUid(), customerType, name);
                                    }
                                    Utils.forwardUserOnRole(user.getUid(), RegisterActivity.this);
                                } else {
                                    // User registration failed, display an error message
                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                // Show a success message
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
