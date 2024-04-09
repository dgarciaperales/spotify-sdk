package com.example.spotify_sdk;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.util.Log;






public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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



        //update profile functionality -->
        Button updateProfile = findViewById(R.id.update_profile);
        updateProfile.setOnClickListener( v -> {





        });
        //delete account functionality -->
        Button deleteAccount  = findViewById(R.id.delete_account);
        deleteAccount.setOnClickListener(v -> {
            // Get the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            // Check if the user is signed in
            if (currentUser != null) {
                // Delete the user's account
                currentUser.delete()
                        .addOnSuccessListener(aVoid -> {
                            // Account deleted successfully
                            // Redirect to the start activity or any other appropriate screen
                            Intent intent = new Intent(SettingsActivity.this, StartActivity.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            // Handle errors
                            // For example, display a toast message or log the error
                            Log.e("TAG", "Error deleting account", e);
                        });
            }
        });

    }

}