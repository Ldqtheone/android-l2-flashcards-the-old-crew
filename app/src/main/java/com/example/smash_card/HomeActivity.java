package com.example.smash_card;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);
        backgroundImageView.setBackgroundResource(R.drawable.ssbu_background);

        Button startQuizButton = findViewById(R.id.startQuizButton);
        Button aboutButton = findViewById(R.id.aboutButton);
        startQuizButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.startQuizButton:
                 intent = new Intent(HomeActivity.this, MainActivity.class);
                HomeActivity.this.startActivity(intent);
                break;
            case R.id.aboutButton:
                 intent = new Intent(HomeActivity.this, About.class);
                HomeActivity.this.startActivity(intent);
                break;
        }
    }
}