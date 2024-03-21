package com.example.spotify_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;
import android.content.Intent;
import android.widget.Button;


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
    }

}