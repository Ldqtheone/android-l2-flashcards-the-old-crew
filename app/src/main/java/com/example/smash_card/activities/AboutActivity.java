package com.example.smash_card.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.smash_card.MusicPlayerService;
import com.example.smash_card.R;

/**
 * About Activity get info of the app and the godlike crew who made it
 */
public class AboutActivity extends AppCompatActivity implements LifecycleObserver {

    private boolean isContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView versionText = findViewById(R.id.versionText);
            versionText.setText(versionName);
            String appName = (String) getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(getPackageName(), 0));
            TextView titleText = findViewById(R.id.titleText);
            titleText.setText(appName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.isContext = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        stopService(new Intent(AboutActivity.this, MusicPlayerService.class));
        this.isContext = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        if(this.isContext) {
            Intent intent = new Intent(AboutActivity.this, MusicPlayerService.class);
            intent.putExtra("url", "http://www.feplanet.net/files/scripts/music.php?song=1592");
            startService(intent);
        }
    }

}