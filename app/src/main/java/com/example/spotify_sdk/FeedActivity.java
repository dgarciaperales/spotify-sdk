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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        final Integer[] postCounter = {0};
        DocumentReference postStats = firestore.collection("posts").document("postStats");

        refreshBtn.setOnClickListener(v -> {
            //getting number of posts
            postStats.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot != null){
                        Long postCounterLong = documentSnapshot.getLong("postCounter");
                        postCounter[0] = (Integer) postCounterLong.intValue();
                        updateFeed(postCounter[0], scrollContentLayout);
                    }
                }
            });
        });

//        ImageButton commentButton =  (ImageButton) findViewById(R.id.comment_button);
//        commentButton.setOnClickListener(view -> {
//            Toast.makeText(FeedActivity.this, "Comments Page", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(FeedActivity.this, CommentViewActivity.class);
//            startActivity(intent);
//        });
    }

    private void updateFeed(Integer totalPosts, LinearLayout scrollContentLayout){
        for (int i = 0; i <= totalPosts - 1; i++) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.post_card, null);
            TextView topArtist = cardView.findViewById(R.id.topArtist);
            TextView topGenre = cardView.findViewById(R.id.topGenre);
            TextView topTrack1 = cardView.findViewById(R.id.topTrack1);
            TextView topTrackArtist1 = cardView.findViewById(R.id.topTrackArtist1);
            TextView topTrack2 = cardView.findViewById(R.id.topTrack2);
            TextView topTrackArtist2 = cardView.findViewById(R.id.topTrackArtist2);
            TextView topTrack3 = cardView.findViewById(R.id.topTrack3);
            TextView topTrackArtist3 = cardView.findViewById(R.id.topTrackArtist3);
            TextView username = cardView.findViewById(R.id.username);

            CollectionReference posts = firestore.collection("posts");
            posts.document("post"+i).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot != null){
                        List<String> topTracks = (List<String>) documentSnapshot.get("topTracks");
                        //List<String> topTrackArtists = (List<String>) documentSnapshot.get("topTrackArtists");
                        String topArtistString = documentSnapshot.getString("topArtist");
                        String topGenreString = documentSnapshot.getString("topGenre");
                        String usernameString = documentSnapshot.getString("author");
                        username.setText(usernameString);
                        topArtist.setText("my top artist : " + topArtistString);
                        topGenre.setText("my top genre : " + topGenreString);
                        topTrack1.setText("my top track : " + topTracks.get(0));
                        //topTrackArtist1.setText("my top track artist: " + topTrackArtists.get(f));
                        topTrack2.setText("my top track : " + topTracks.get(1));
                        //topTrackArtist2.setText("my top track artist: " + topTrackArtists.get(f));
                        topTrack3.setText("my top track : " + topTracks.get(2));
                        //topTrackArtist3.setText("my top track artist: " + topTrackArtists.get(f));
                        scrollContentLayout.addView(cardView);
                    }

                }
            });
        }
    }
}