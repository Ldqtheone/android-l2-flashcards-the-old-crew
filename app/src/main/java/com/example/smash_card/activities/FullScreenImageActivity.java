package com.example.smash_card.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.smash_card.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Intent srcIntent = this.getIntent();

        ImageView fullScreenImageView = findViewById(R.id.fullScreenImageView);

        switch (srcIntent.getStringExtra("mode")) {
            case "Noob":
                Picasso.get().load(srcIntent.getStringExtra("image")).into(fullScreenImageView);
                break;
            case "Pro":
                try {
                    InputStream is = this.getApplicationContext()
                    .getResources()
                    .getAssets()
                    .open("SSBU_IMAGES/" + srcIntent.getStringExtra("imagePro"));
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    fullScreenImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}