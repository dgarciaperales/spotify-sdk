package com.example.spotify_sdk;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        LinearLayout scrollContentLayout = findViewById(R.id.postLayout);
        ImageButton wrappedButton = findViewById(R.id.wrapped_btn);
        wrappedButton.setOnClickListener(v -> startActivity(new Intent(FeedActivity.this, MainActivity.class)));

        ImageButton settingsButton = findViewById(R.id.settings_btn);
        settingsButton.setOnClickListener(v -> startActivity(new Intent(FeedActivity.this, SettingsActivity.class)));

        Button refreshBtn = findViewById(R.id.refresh_btn);
        final Integer[] postCounter = {0};
        DocumentReference postStats = firestore.collection("posts").document("postStats");

        refreshBtn.setOnClickListener(v -> postStats.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                Long postCounterLong = documentSnapshot.getLong("postCounter");
                if (postCounterLong != null) {
                    postCounter[0] = postCounterLong.intValue();
                    updateFeed(postCounter[0], scrollContentLayout);
                }
            }
        }));
    }

    private void followUser(String followedUserId, Button followButton) {
        String currentUserId = auth.getCurrentUser().getEmail();
        DocumentReference currentUserRef = firestore.collection("users").document(currentUserId);

        currentUserRef.update("following", FieldValue.arrayUnion(followedUserId))
                .addOnSuccessListener(aVoid -> followButton.setText("Following"))
                .addOnFailureListener(e -> followButton.setText("Follow Failed"));
    }

    private void updateFeed(Integer totalPosts, LinearLayout scrollContentLayout) {
        for (int i = totalPosts - 1; i >= 0; i--) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.post_card, scrollContentLayout, false);
            TextView topArtist = cardView.findViewById(R.id.topArtist);
            TextView topGenre = cardView.findViewById(R.id.topGenre);
            TextView topTrack1 = cardView.findViewById(R.id.topTrack1);
            TextView topTrackArtist1 = cardView.findViewById(R.id.topTrackArtist1);
            TextView topTrack2 = cardView.findViewById(R.id.topTrack2);
            TextView topTrackArtist2 = cardView.findViewById(R.id.topTrackArtist2);
            TextView topTrack3 = cardView.findViewById(R.id.topTrack3);
            TextView topTrackArtist3 = cardView.findViewById(R.id.topTrackArtist3);
            TextView username = cardView.findViewById(R.id.username);
            ImageView ArtistImg = cardView.findViewById(R.id.feedArtistImg);
            Button followButton = cardView.findViewById(R.id.follow_button);

            CollectionReference posts = firestore.collection("posts");
            posts.document("post" + i).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot != null) {
                    List<String> topTracks = (List<String>) documentSnapshot.get("topTracks");
                    List<String> topTrackArtists = (List<String>) documentSnapshot.get("topTrackArtists");
                    String topArtistImg = documentSnapshot.getString("topArtistImg");
                    String topArtistString = documentSnapshot.getString("topArtist");
                    String topGenreString = documentSnapshot.getString("topGenre");
                    String usernameString = documentSnapshot.getString("author");

                    username.setText(usernameString);
                    topArtist.setText(topArtistString);
                    Glide.with(FeedActivity.this).load(topArtistImg).into(ArtistImg);
                    topGenre.setText(topGenreString);
                    topTrack1.setText("#1: " + topTracks.get(0));
                    topTrackArtist1.setText(topTrackArtists.get(0));
                    topTrack2.setText("#2: " + topTracks.get(1));
                    topTrackArtist2.setText(topTrackArtists.get(1));
                    topTrack3.setText("#3: " + topTracks.get(2));
                    topTrackArtist3.setText(topTrackArtists.get(2));
                    followButton.setOnClickListener(v -> followUser(usernameString, followButton));

                    scrollContentLayout.addView(cardView);
                }
            });
        }
    }
}
