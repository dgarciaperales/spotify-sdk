package com.example.spotify_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        LinearLayout scrollContentLayout = findViewById(R.id.postLayout);

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

        Button refreshBtn = findViewById(R.id.refresh_btn);
        refreshBtn.setOnClickListener(v -> {

            for (int i = 1; i < 4; i++) {
                View cardView = LayoutInflater.from(this).inflate(R.layout.post_card, null);
                TextView topArtist = cardView.findViewById(R.id.topArtist);
                TextView topGenre = cardView.findViewById(R.id.topGenre);
                TextView topTrack1 = cardView.findViewById(R.id.topTrack1);
                TextView topTrackArtist1 = cardView.findViewById(R.id.topTrackArtist1);
                TextView topTrack2 = cardView.findViewById(R.id.topTrack2);
                TextView topTrackArtist2 = cardView.findViewById(R.id.topTrackArtist2);
                TextView topTrack3 = cardView.findViewById(R.id.topTrack3);
                TextView topTrackArtist3 = cardView.findViewById(R.id.topTrackArtist3);

                topArtist.setText("my top artist : " + i);
                topGenre.setText("my top genre : " + i);

                for (int f = 1; f < 4; f++) {
                    topTrack1.setText("my top track : " + f);
                    topTrackArtist1.setText("my top track artist: " + f);
                    topTrack2.setText("my top track : " + f);
                    topTrackArtist2.setText("my top track artist: " + f);
                    topTrack3.setText("my top track : " + f);
                    topTrackArtist3.setText("my top track artist: " + f);
                }
                scrollContentLayout.addView(cardView);
            }
        });

//        ImageButton commentButton =  (ImageButton) findViewById(R.id.comment_button);
//        commentButton.setOnClickListener(view -> {
//            Toast.makeText(FeedActivity.this, "Comments Page", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(FeedActivity.this, CommentViewActivity.class);
//            startActivity(intent);
//        });
    }
}