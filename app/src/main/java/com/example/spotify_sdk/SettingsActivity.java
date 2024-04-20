package com.example.spotify_sdk;
import android.text.InputType;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;


import android.os.Bundle;

import android.widget.ImageButton;
import android.content.Intent;
import android.widget.Button;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsActivity extends AppCompatActivity {
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //navigation to home/wrapped page
        ImageButton wrappedButton = findViewById(R.id.wrapped_btn);
        wrappedButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        //navigation to feed page
        ImageButton feedButton = findViewById(R.id.feed_btn);
        feedButton.setOnClickListener( v -> {
            Intent intent = new Intent(SettingsActivity.this, FeedActivity.class);
            startActivity(intent);
        });

        //log out of account --> navigates to first page StartActivity
        Button logOut = findViewById(R.id.log_out);
        logOut.setOnClickListener( v -> {
            Intent intent = new Intent(SettingsActivity.this, StartActivity.class);
            startActivity(intent);
        });

        //reset password functionality -->
        Button resetPassword = findViewById(R.id.update_profile);
        resetPassword.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.getEmail() != null) {
                String emailAddress = user.getEmail();
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("RESET USER", "Email sent.");
                            } else {
                                Log.e("RESET USER", "Failed to send password reset email.", task.getException());
                            }
                        });
            } else {
                Log.e("RESET USER", "User is not signed in or does not have an email address.");
            }
        });

        //reset email functionality -->
        //reset email functionality -->
        //reset email functionality -->

        Button changeEmailButton = findViewById(R.id.change_email);
        changeEmailButton.setOnClickListener(v -> {
            // Create an EditText dialog for user input
            final EditText input = new EditText(SettingsActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

            // Create an AlertDialog to prompt the user to enter their new email address
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Enter New Email Address");
            builder.setView(input);

            // Set up the buttons for positive (OK) and negative (Cancel) actions
            builder.setPositiveButton("OK", (dialog, which) -> {
                // Get the new email address entered by the user
                String newEmailAddress = input.getText().toString().trim();

                // Get the current user from Firebase Authentication
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                // Check if a user is signed in
                if (currentUser != null) {
                    // Call verifyBeforeUpdateEmail with the new email address
                    currentUser.verifyBeforeUpdateEmail(newEmailAddress)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Email can be updated, send verification email
                                    currentUser.sendEmailVerification()
                                            .addOnCompleteListener(sendEmailTask -> {
                                                if (sendEmailTask.isSuccessful()) {
                                                    // Email verification sent successfully
                                                    // Notify user to check their email for verification link
                                                    Toast.makeText(getApplicationContext(), "Verification email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Failed to send verification email
                                                    // Handle error
                                                    Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // Failed to verify email before update
                                    // Handle error
                                    Toast.makeText(getApplicationContext(), "Failed to verify email before update.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // No user is signed in
                    // Redirect to sign in or handle accordingly
                    Toast.makeText(getApplicationContext(), "No user signed in.", Toast.LENGTH_SHORT).show();
                }
            });

            // Negative button action (Cancel)
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            // Show the AlertDialog
            builder.show();
        });

        //delete account functionality -->
        Button deleteAccount = findViewById(R.id.delete_account);
        deleteAccount.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                currentUser.delete()
                        .addOnSuccessListener(aVoid -> {
                            Intent intent = new Intent(SettingsActivity.this, StartActivity.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Log.e("TAG", "Error deleting account", e);
                        });
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}

