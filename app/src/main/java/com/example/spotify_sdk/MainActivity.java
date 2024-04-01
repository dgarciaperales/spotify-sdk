package com.example.spotify_sdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "336c8fa19efd4066947c6f2bf97d1003";
    public static final String REDIRECT_URI = "spotify-sdk://auth";
    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.login_btn);
        Button generateWrappedBtn = findViewById(R.id.generate_wrapped_btn);

        LinearLayout scrollContentLayout = findViewById(R.id.pastWrappedLayout);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //get current user's email to access their account
        String docId = auth.getCurrentUser().getEmail();
        DocumentReference userDocRef = firestore.collection("users").document(docId);

        //get user's wrapped counter, use as loop condition
        final Integer[] wrappedCount = {0};
        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> userData = documentSnapshot.getData();
                            if (userData != null) {
                                Object wrappedCounterObj = userData.get("wrappedCounter");
                                //had to do some long conversion here? firebase storing as long not integer
                                Long wrappedCounterVal = (Long) wrappedCounterObj;
                                wrappedCount[0] = (Integer) wrappedCounterVal.intValue();
                                Log.d("Wrapped Count", "" + wrappedCount[0]);
                                //String wrapName = "wrapped" + wrappedCount[0].toString();
                                updatePastWrapped(wrappedCount[0], userDocRef, scrollContentLayout);
                            } else {
                                Log.d("Firestore", "User data is null");
                            }
                        } else {
                            Log.d("Firestore", "No such document");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting document", e);
                    }
                });

        loginBtn.setOnClickListener(v -> {
            getToken();
        });

        // generateWrappedBtn.setEnabled(false);

        generateWrappedBtn.setOnClickListener(v -> {
            if (mAccessToken != null) {
                Intent intent = new Intent(MainActivity.this, GenerateWrapped.class);
                intent.putExtra("access_token", mAccessToken);
                startActivity(intent);
            } else {
                showToast("Error: Please log in with Spotify first.");
            }
        });


        //navigation to settings page
        ImageButton settingsButton = findViewById(R.id.settings_btn);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        //navigation to feed page
        ImageButton feedButton = findViewById(R.id.feed_btn);
        feedButton.setOnClickListener( v -> {
            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(intent);
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(MainActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        if (AUTH_TOKEN_REQUEST_CODE == requestCode && response.getType() == AuthorizationResponse.Type.TOKEN) {
            mAccessToken = response.getAccessToken();
        }
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{"user-top-read"})
                .setCampaign("your-campaign-token")
                .build();
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void updatePastWrapped(Integer wrappedTotal, DocumentReference userDocRef, LinearLayout scrollContentLayout){
        for (int i = 1; i <= wrappedTotal - 1; i++) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.past_wrapped_card, null);
            TextView TimeFrameView = cardView.findViewById(R.id.time_frame_view);
            TextView TimeStampView = cardView.findViewById(R.id.time_stamp_view);

            //get timestamp and timeframe value for each wrapped

            TimeFrameView.setText("Wrapped " + (i));
            CollectionReference wrappedCol = userDocRef.collection("wrapped");
            final String[] timestamp = {""};
            String timeframe = "";
            String wrapName = "Wrapped " + i;
            wrappedCol.document("wrapped"+i)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot document) {
                            if (document != null) {
                                Timestamp fullTimestamp = document.getTimestamp("timestamp");
                                String timeframe = document.getString("timeframe");
                                if(fullTimestamp != null){
                                    Date date = fullTimestamp.toDate();
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY", Locale.getDefault());
                                    timestamp[0] = sdf.format(date);
                                    TimeFrameView.setText(wrapName + ": " + timestamp[0]);
                                } else {
                                    Log.d("Firestore", "Unable to get timestamp value");
                                }
                                if(timeframe != null){
                                    TimeStampView.setText("Wrapped spanning " + timeframe);
                                } else {
                                    Log.d("Firestore", "Unable to get timeframe value");
                                }
                                scrollContentLayout.addView(cardView);
                            } else {
                                Log.d("Firestore", "No such document");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Firestore", "get failed with ", e);
                        }
                    });
        }
    }
}
