package com.example.spotify_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.ImageButton;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //navigation to home/wrapped page
        ImageButton wrappedButton = findViewById(R.id.wrapped_btn);
        wrappedButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intent);
        });

        //navigation to settings page
        ImageButton settingsButton = findViewById(R.id.settings_btn);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        ImageButton addFriendsBtn=  (ImageButton) findViewById(R.id.addFriendsButton);

        // Below code is for setting a click listener on the image
        addFriendsBtn.setOnClickListener(view -> {
            // Creating a toast to display the message
            Toast.makeText(FeedActivity.this, "Add Friends Page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FeedActivity.this, AddFriendsActivity.class);
            startActivity(intent);
        });
    }
}