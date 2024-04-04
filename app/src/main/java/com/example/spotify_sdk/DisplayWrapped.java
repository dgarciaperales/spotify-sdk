package com.example.spotify_sdk;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.drawable.Drawable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.os.Handler;
import com.google.android.material.tabs.TabLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
public class DisplayWrapped extends AppCompatActivity {
    Button btnTopTrack, btnTopArtist, btnTopGenre;
    EditText name1, name2, name3, name4, name5;
    ImageView image1, image2, image3, image4, image5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.display_wrapped);
        Bundle bundle = getIntent().getExtras();
        btnTopTrack = findViewById(R.id.btn_TopTrack);
        btnTopArtist = findViewById(R.id.btn_TopArtist);
        btnTopGenre = findViewById(R.id.btn_Genre);
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        name4 = findViewById(R.id.name4);
        name5 = findViewById(R.id.name5);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);

        btnTopTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSlate();
                ArrayList<String> trackImage = bundle.getStringArrayList("topTrackImg");
                ArrayList<String> tracks = bundle.getStringArrayList("tracks");
                Glide.with(DisplayWrapped.this).load(trackImage.get(0)).into(image1);
                name1.setText(tracks.get(0));
                Glide.with(DisplayWrapped.this).load(trackImage.get(1)).into(image2);
                name2.setText(tracks.get(1));
                Glide.with(DisplayWrapped.this).load(trackImage.get(2)).into(image3);
                name3.setText(tracks.get(2));
                Glide.with(DisplayWrapped.this).load(trackImage.get(3)).into(image4);
                name4.setText(tracks.get(3));
                Glide.with(DisplayWrapped.this).load(trackImage.get(4)).into(image5);
                name5.setText(tracks.get(4));

            }
        });
        btnTopArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSlate();
                ArrayList<String> artistImg = bundle.getStringArrayList("topArtistImg");
                ArrayList<String> artists = bundle.getStringArrayList("artists");
                Glide.with(DisplayWrapped.this).load(artistImg.get(0)).into(image1);
                name1.setText(artists.get(0));
                Glide.with(DisplayWrapped.this).load(artistImg.get(1)).into(image2);
                name2.setText(artists.get(1));
                Glide.with(DisplayWrapped.this).load(artistImg.get(2)).into(image3);
                name3.setText(artists.get(2));
                Glide.with(DisplayWrapped.this).load(artistImg.get(3)).into(image4);
                name4.setText(artists.get(3));
                Glide.with(DisplayWrapped.this).load(artistImg.get(4)).into(image5);
                name5.setText(artists.get(4));

            }
        });
        btnTopGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSlate();
                ArrayList<String> genre = bundle.getStringArrayList("genres");
                name1.setText(genre.get(0));
                name2.setText(genre.get(1));
                name3.setText(genre.get(2));
                name4.setText(genre.get(3));
                name5.setText(genre.get(4));

            }
        });



    }
    public void clearSlate() {
        image1.setImageBitmap(null);
        image2.setImageBitmap(null);
        image3.setImageBitmap(null);
        image4.setImageBitmap(null);
        image5.setImageBitmap(null);
        name1.setText("");
        name2.setText("");
        name3.setText("");
        name4.setText("");
        name5.setText("");
    }


}