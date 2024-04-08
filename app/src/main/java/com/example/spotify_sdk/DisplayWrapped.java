package com.example.spotify_sdk;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout; // Add this line
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
import android.graphics.Canvas;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import android.widget.Toast;
import android.os.Environment;
import android.content.ContentValues;
import android.provider.MediaStore;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.content.ContentResolver;
import java.util.Objects;


public class DisplayWrapped extends AppCompatActivity {
    Button btnTopTrack, btnTopArtist, btnTopGenre, btnRecArtist;
    TextView name1, name2, name3, name4, name5;
    ImageView image1, image2, image3, image4, image5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.display_wrapped);
        Bundle bundle = getIntent().getExtras();
        btnTopTrack = findViewById(R.id.btn_TopTrack);
        btnTopArtist = findViewById(R.id.btn_TopArtist);
        ImageButton exitBtn = findViewById(R.id.exitBtn);
        btnTopGenre = findViewById(R.id.btn_Genre);
        btnRecArtist = findViewById(R.id.btn_RecArtist);
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
        clearSlate();
        ArrayList<String> trackImage = bundle.getStringArrayList("topTrackImg");
        ArrayList<String> tracks = bundle.getStringArrayList("tracks");
        ArrayList<String> trackArtists = bundle.getStringArrayList("trackArtists");

        Glide.with(DisplayWrapped.this).load(trackImage.get(0)).into(image1);
        String text0 = tracks.get(0) + " - " + trackArtists.get(0);
        name1.setText(text0);

        Glide.with(DisplayWrapped.this).load(trackImage.get(1)).into(image2);
        String text1 = tracks.get(1) + " - " + trackArtists.get(1);
        name2.setText(text1);

        Glide.with(DisplayWrapped.this).load(trackImage.get(2)).into(image3);
        String text2 = tracks.get(2) + " - " + trackArtists.get(2);
        name3.setText(text2);

        Glide.with(DisplayWrapped.this).load(trackImage.get(3)).into(image4);
        String text3 = tracks.get(3) + " - " + trackArtists.get(3);
        name4.setText(text3);

        Glide.with(DisplayWrapped.this).load(trackImage.get(4)).into(image5);
        String text4 = tracks.get(4) + " - " + trackArtists.get(4);
        name5.setText(text4);

        btnTopTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSlate();

                Glide.with(DisplayWrapped.this).load(trackImage.get(0)).into(image1);
                String text0 = tracks.get(0) + " - " + trackArtists.get(0);
                name1.setText(text0);

                Glide.with(DisplayWrapped.this).load(trackImage.get(1)).into(image2);
                String text1 = tracks.get(1) + " - " + trackArtists.get(1);
                name2.setText(text1);

                Glide.with(DisplayWrapped.this).load(trackImage.get(2)).into(image3);
                String text2 = tracks.get(2) + " - " + trackArtists.get(2);
                name3.setText(text2);

                Glide.with(DisplayWrapped.this).load(trackImage.get(3)).into(image4);
                String text3 = tracks.get(3) + " - " + trackArtists.get(3);
                name4.setText(text3);

                Glide.with(DisplayWrapped.this).load(trackImage.get(4)).into(image5);
                String text4 = tracks.get(4) + " - " + trackArtists.get(4);
                name5.setText(text4);

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

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayWrapped.this, MainActivity.class);
                startActivity(intent);
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
        btnRecArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSlate();
                ArrayList<String> recArtist = bundle.getStringArrayList("recArtists");
                ArrayList<String> recArtistImg = bundle.getStringArrayList("recArtistsImg");
                Glide.with(DisplayWrapped.this).load(recArtistImg.get(0)).into(image1);
                name1.setText(recArtist.get(0));
                Glide.with(DisplayWrapped.this).load(recArtistImg.get(1)).into(image2);
                name2.setText(recArtist.get(1));
                Glide.with(DisplayWrapped.this).load(recArtistImg.get(2)).into(image3);
                name3.setText(recArtist.get(2));
                Glide.with(DisplayWrapped.this).load(recArtistImg.get(3)).into(image4);
                name4.setText(recArtist.get(3));
                Glide.with(DisplayWrapped.this).load(recArtistImg.get(4)).into(image5);
                name5.setText(recArtist.get(4));
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

    public void exportBtn(View view) {

        LinearLayout layout = findViewById(R.id.myLayout);
        layout.setDrawingCacheEnabled(true);

        layout.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(layout.getDrawingCache());
        layout.setDrawingCacheEnabled(false); // clear drawing cache

        saveToGallery(bitmap);
    }


    private void saveToGallery(Bitmap bitmap) {
        OutputStream fos;
        File imageFile = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + System.currentTimeMillis());
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "YourAppName");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Objects.requireNonNull(fos);
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Image not saved: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + "YourAppName";
            File file = new File(imagesDir);
            if (!file.exists()) {
                file.mkdir();
            }
            String fileName = "image_" + System.currentTimeMillis() + ".png";
            imageFile = new File(imagesDir, fileName);
            try {
                fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
            } catch (Exception e) {
                Toast.makeText(this, "Image not saved: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}







