package com.example.smash_card.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.example.smash_card.R;

public class ThemeActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        Log.i("video", "onCreate: " + "android.resource://" + getPackageName() + R.raw.lifelight);

        VideoView videoView = findViewById(R.id.themeVideoView);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lifelight));
        videoView.requestFocus();
        videoView.start();

        videoView.setOnClickListener(this);
        videoView.setOnPreparedListener(this);
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();

        switch (v.getId()) {
            case R.id.themeVideoView:
                Intent intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setVolume(0.3f,0.3f);
    }
}