package com.example.spotify_sdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GenerateWrapped extends AppCompatActivity {
    private String mAccessToken;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;

    private String firstArtistId;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_wrapped);

        if (getIntent().hasExtra("access_token")) {
            mAccessToken = getIntent().getStringExtra("access_token");
        }

        Spinner dropdown = findViewById(R.id.spinner);
        Button getRequestBtn = findViewById(R.id.enter_btn);
        TextView profileTextView = findViewById(R.id.profile_text_view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.timeSpans, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dropdown.setAdapter(adapter);



        getRequestBtn.setOnClickListener(v -> {
            String selectedTimeSpan = dropdown.getSelectedItem().toString();
            String timeRange;
            switch (selectedTimeSpan) {
                case "4 weeks":
                    timeRange = "short_term";
                    break;
                case "6 months":
                    timeRange = "medium_term";
                    break;
                case "Several years":
                    timeRange = "long_term";
                    break;
                default:
                    return;
            }

            final Request tracksRequest = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/tracks?time_range=" + timeRange + "&limit=5")
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .build();

            final Request artistsRequest = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/artists?time_range=" + timeRange + "&limit=5")
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .build();

            cancelCall();
            mCall = mOkHttpClient.newCall(tracksRequest);

            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("HTTP", "Failed to fetch data: " + e);
                    showToast("Failed to fetch data, watch Logcat for more details");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        final JSONObject jsonObject = new JSONObject(response.body().string());
                        final JSONArray jsonArray = jsonObject.getJSONArray("items");
                        ArrayList<String> tracks = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            tracks.add(jsonArray.getJSONObject(i).getString("name"));
                        }

                        mCall = mOkHttpClient.newCall(artistsRequest);

                        mCall.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("HTTP", "Failed to fetch artists: " + e);
                                showToast("Failed to fetch artists, watch Logcat for more details");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    final JSONObject artistsObject = new JSONObject(response.body().string());
                                    final JSONArray artistsArray = artistsObject.getJSONArray("items");
                                    firstArtistId = artistsArray.getJSONObject(0).getString("id");
                                    ArrayList<String> artists = new ArrayList<>();
                                    for (int i = 0; i < artistsArray.length(); i++) {
                                        artists.add(artistsArray.getJSONObject(i).getString("name"));
                                    }

                                    mCall = mOkHttpClient.newCall(new Request.Builder()
                                            .url("https://api.spotify.com/v1/me")
                                            .addHeader("Authorization", "Bearer " + mAccessToken)
                                            .build());

                                    mCall.enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.d("HTTP", "Failed to fetch user info: " + e);
                                            showToast("Failed to fetch user info, watch Logcat for more details");
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            try {
                                                final JSONObject userObject = new JSONObject(response.body().string());
                                                final String userId = userObject.getString("id");

                                                final Request genresRequest = new Request.Builder()
                                                        .url("https://api.spotify.com/v1/me/top/artists?time_range=" + timeRange + "&limit=50")
                                                        .addHeader("Authorization", "Bearer " + mAccessToken)
                                                        .build();

                                                mCall = mOkHttpClient.newCall(genresRequest);

                                                mCall.enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        Log.d("HTTP", "Failed to fetch genres: " + e);
                                                        showToast("Failed to fetch genres, watch Logcat for more details");
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        try {
                                                            final JSONObject genresObject = new JSONObject(response.body().string());
                                                            final JSONArray genresArray = genresObject.getJSONArray("items");
                                                            ArrayList<String> genres = new ArrayList<>();
                                                            for (int i = 0; i < genresArray.length() && genres.size() < 5; i++) {
                                                                JSONArray artistGenres = genresArray.getJSONObject(i).getJSONArray("genres");
                                                                for (int j = 0; j < artistGenres.length() && genres.size() < 5; j++) {
                                                                    String genre = artistGenres.getString(j);
                                                                    if (!genres.contains(genre)) {
                                                                        genres.add(genre);
                                                                    }
                                                                }
                                                            }

                                                            final Request relatedArtistsRequest = new Request.Builder()
                                                                    .url("https://api.spotify.com/v1/artists/" + firstArtistId + "/related-artists")
                                                                    .addHeader("Authorization", "Bearer " + mAccessToken)
                                                                    .build();

                                                            mOkHttpClient.newCall(relatedArtistsRequest).enqueue(new Callback() {
                                                                @Override
                                                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                                    Log.d("JSON", "Failed process recs API: " + e);
                                                                    showToast("Failed to process recs API, watch Logcat for more details");
                                                                }

                                                                @Override
                                                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                                    try {
                                                                        if (!response.isSuccessful()) {
                                                                            Log.e("Response", "Unsucessful response code: " + response.code());
                                                                            showToast("Unsucessful response code: " + response.code());
                                                                            return;
                                                                        }
                                                                        String responseBody = response.body().string();
                                                                        Log.d("Response", "Response body: " + responseBody);

                                                                        JSONObject relatedArtistsObject = new JSONObject(responseBody);
                                                                        JSONArray relatedArtistsArray = relatedArtistsObject.getJSONArray("artists");
                                                                        ArrayList<String> relatedArtists = new ArrayList<>();
                                                                        int count = Math.min(relatedArtistsArray.length(), 5);
                                                                        for (int i = 0; i < count; i++) {
                                                                            JSONObject artistObject = relatedArtistsArray.getJSONObject(i);
                                                                            String artistName = artistObject.getString("name");
                                                                            relatedArtists.add(artistName);
                                                                        }

                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putStringArrayList("tracks", tracks);
                                                                        bundle.putStringArrayList("artists", artists);
                                                                        bundle.putStringArrayList("genres", genres);
                                                                        bundle.putStringArrayList("Recommended Artists", relatedArtists);

                                                                        setTextAsync("\nTop Tracks: " + tracks.toString() + "\n\nTop Artists: " + artists.toString() + "\n\nTop Genres: " + genres.toString() + "\n\n Recommended Artists: " + relatedArtists.toString(), profileTextView);

                                                                    }
                                                                    catch (JSONException e) {
                                                                        Log.d("JSON", "Failed to parse recommendations: " + e);
                                                                        showToast("Failed to parse recommendations, watch Logcat for more details");
                                                                    }
                                                                }
                                                            });



                                                        } catch (JSONException e) {
                                                            Log.d("JSON", "Failed to parse genres: " + e);
                                                            showToast("Failed to parse genres, watch Logcat for more details");
                                                        }
                                                    }
                                                });

                                            } catch (JSONException e) {
                                                Log.d("JSON", "Failed to parse user info: " + e);
                                                showToast("Failed to parse user info, watch Logcat for more details");
                                            }
                                        }
                                    });

                                } catch (JSONException e) {
                                    Log.d("JSON", "Failed to parse artists: " + e);
                                    showToast("Failed to parse artists, watch Logcat for more details");
                                }
                            }
                        });

                    } catch (JSONException e) {
                        Log.d("JSON", "Failed to parse tracks: " + e);
                        showToast("Failed to parse tracks, watch Logcat for more details");
                    }
                }
            });
        });
    }

    private void setTextAsync(String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(GenerateWrapped.this, message, Toast.LENGTH_SHORT).show());
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    private void addWrapped(List<String> recArtists, List<String> recArtistImgs, List<String> topArtists,
                            List<String> topArtistImgs, List<String> topGenres, List<String> topTracks, List<String> topTrackArtists,
                            List<String> topTrackImgs, Timestamp timestamp, String timeframe){
        String docId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference userDocRef = firestore.collection("users").document(docId);
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

        String wrapName = "wrapped" + wrappedCount[0].toString();

        //user document -> collection -> document -> data
        Map <String,Object> info = new HashMap<>();
        info.put("recArtists", recArtists);
        info.put("recArtistImgs", recArtistImgs);
        info.put("topArtists", topArtists);
        info.put("topArtistImgs", topArtistImgs);
        info.put("topGenres", topGenres);
        info.put("topTracks", topTracks);
        info.put("topTrackArtists", topTrackArtists);
        info.put("topTrackImgs", topTrackImgs);
        info.put("timestamp", timestamp);
        info.put("timeframe", timeframe);

        CollectionReference wrappedCollectionRef = userDocRef.collection("wrapped");
        wrappedCollectionRef.document(wrapName)
                .set(info)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "Wrapped data document added");
                        // Handle success
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error adding document", e);
                        // Handle failure
                    }
                });

    }
}

