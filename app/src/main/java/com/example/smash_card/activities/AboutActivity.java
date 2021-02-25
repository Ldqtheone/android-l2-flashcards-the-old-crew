package com.example.smash_card.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.smash_card.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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

}