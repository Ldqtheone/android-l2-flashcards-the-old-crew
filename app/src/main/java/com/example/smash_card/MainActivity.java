package com.example.smash_card;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView answeredImageView = findViewById(R.id.answeredImageView);
        Picasso.get().load("https://static.wikia.nocookie.net/ssb/images/4/44/Mario_SSBU.png/revision/latest/scale-to-width-down/985?cb=20180612204958").into(answeredImageView);

    }
}