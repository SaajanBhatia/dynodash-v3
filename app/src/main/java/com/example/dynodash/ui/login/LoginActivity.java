package com.example.dynodash.ui.login;

// Activity file for Login

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dynodash.R;
import com.example.dynodash.Utilities;
import com.example.dynodash.ui.customer.CustomerActivity;
import com.example.dynodash.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // UI Components
    private TextInputLayout mUsernameLayout;
    private TextInputLayout mPasswordLayout;
    private TextInputEditText mUsernameEditText;
    private TextInputEditText mPasswordEditText;
    private Button mLoginButton;
    private Button linkToRegisterButton;
    private TextView mErrorMessageTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Upon calling, inflate the Activity Login Layout
        setContentView(R.layout.activity_login);

        // Instance of Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Instances of UI components
        mUsernameLayout = findViewById(R.id.loginUsername);
        mPasswordLayout = findViewById(R.id.loginPassword);
        mUsernameEditText = (TextInputEditText) mUsernameLayout.getEditText();
        mPasswordEditText = (TextInputEditText) mPasswordLayout.getEditText();
        mLoginButton = findViewById(R.id.loginButton);
        mErrorMessageTextView = findViewById(R.id.errorMessageTextView);

        linkToRegisterButton = findViewById(R.id.linkToRegisterFrame);

        linkToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        // Event listener to button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                // If empty reject submission
                if (username.isEmpty()) {
                    mUsernameLayout.setError("Please enter your email");
                    mUsernameLayout.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    mPasswordLayout.setError("Please enter your password");
                    mPasswordLayout.requestFocus();
                    return;
                }

                // Sign in User with Email Username and Password
                signInWithEmailAndPassword(username, password);
            }
        });
    }


    // Sign in Function
    private void signInWithEmailAndPassword(String email, String password) {
        // Use the Firebase instance to sign in the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If the request returns success, create an instance of the user
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {

                                // Extract UID
                                String uid = new String(user.getUid());

                                // Forward the user to the correct component depending on role (Customer or Restaurant)
                                Utilities.forwardUserOnRole(uid,LoginActivity.this);

                            }
                        } else {
                            // Else return a toast indicating unsuccessful authentication
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
