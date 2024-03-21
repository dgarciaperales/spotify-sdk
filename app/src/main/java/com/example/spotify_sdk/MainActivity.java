package com.example.spotify_sdk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import android.view.View;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "336c8fa19efd4066947c6f2bf97d1003";
    public static final String REDIRECT_URI = "spotify-sdk://auth";
    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.login_btn);
        Button generateWrappedBtn = findViewById(R.id.generate_wrapped_btn);

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
}
