package com.example.smash_card.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.smash_card.MusicPlayerService;
import com.example.smash_card.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

/**
 * image in full screen
 */
public class FullScreenImageActivity extends AppCompatActivity implements LifecycleObserver {

    private boolean isContext;
    private String urlSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        Intent srcIntent = this.getIntent();

        this.urlSong = srcIntent.getStringExtra("url");

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

    @Override
    protected void onStart() {
        super.onStart();
        this.isContext = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        stopService(new Intent(FullScreenImageActivity.this, MusicPlayerService.class));
        this.isContext = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        if(this.isContext) {
            Intent intent = new Intent(FullScreenImageActivity.this, MusicPlayerService.class);
            intent.putExtra("url", this.urlSong);
            startService(intent);
        }
    }
}