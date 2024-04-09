package com.example.spotify_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
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

//        ImageButton commentButton =  (ImageButton) findViewById(R.id.comment_button);
//        commentButton.setOnClickListener(view -> {
//            Toast.makeText(FeedActivity.this, "Comments Page", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(FeedActivity.this, CommentViewActivity.class);
//            startActivity(intent);
//        });
    }
}