package com.example.spotify_sdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GenerateWrapped extends AppCompatActivity {
    private String mAccessToken;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;

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

                        // Make a separate API call for artists
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
                                    ArrayList<String> artists = new ArrayList<>();
                                    for (int i = 0; i < artistsArray.length(); i++) {
                                        artists.add(artistsArray.getJSONObject(i).getString("name"));
                                    }

                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList("tracks", tracks);
                                    bundle.putStringArrayList("artists", artists);

                                    TracksFragment tracksFragment = new TracksFragment();
                                    tracksFragment.setArguments(bundle);

                                    // Start the DisplayWrapped activity
                                    Intent intent = new Intent(GenerateWrapped.this, DisplayWrapped.class);
                                    startActivity(intent);

                                    // setTextAsync("Top Tracks: " + tracks.toString() + "\nTop Artists: " + artists.toString(), profileTextView);
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
}
