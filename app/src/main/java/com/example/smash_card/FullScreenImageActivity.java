package com.example.smash_card;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Intent srcIntent = this.getIntent();

        ImageView fullScreenImageView = findViewById(R.id.fullScreenImageView);
        Picasso.get().load(srcIntent.getStringExtra("image")).into(fullScreenImageView);
    }
}