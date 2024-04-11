package com.example.spotify_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ActionCodeSettings;

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
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
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


        /*
        Button resetEmail = findViewById(R.id.reset_email);
        resetEmail.setOnClickListener(v -> {
            if (currentUser != null && currentUser.getEmail() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter New Email");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newEmailAddress = input.getText().toString().trim();
                        if (!isValidEmail(newEmailAddress)) {
                            Toast.makeText(SettingsActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        currentUser.verifyBeforeUpdateEmail(newEmailAddress)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Email address verification successful, proceed with updating
                                        currentUser.updateEmail(newEmailAddress)
                                                .addOnCompleteListener(emailUpdateTask -> {
                                                    if (emailUpdateTask.isSuccessful()) {
                                                        Log.d("RESET EMAIL", "Email updated to: " + newEmailAddress);
                                                    } else {
                                                        Log.e("RESET EMAIL", "Failed to update email.", emailUpdateTask.getException());
                                                    }
                                                });
                                    } else {
                                        // Email address verification failed
                                        Log.e("RESET EMAIL", "Failed to verify email address.", task.getException());
                                        Toast.makeText(SettingsActivity.this, "Failed to verify email address", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            } else {
                Log.e("RESET EMAIL", "User is not signed in or does not have an email address.");
            }
        });
            */

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

