package com.example.smash_card;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

public class FlashCard extends AppCompatActivity implements View.OnClickListener {
    private Characters character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);
        ImageView characterNoobImageView = findViewById(R.id.characterNoobImageView);
        ImageView characterProImageView = findViewById(R.id.characterProImageView);
        Button playSoundButton = findViewById(R.id.playSoundButton);
        Intent srcIntent = getIntent();
        this.character = srcIntent.getParcelableExtra("character");
        TextView nameText = findViewById(R.id.characterNameTextView);
        nameText.setText(this.character.getName());
        Picasso.get().load(this.character.getImage()).into(characterNoobImageView);
        InputStream is = null;
        try {
            is = this.getApplicationContext()
                    .getResources()
                    .getAssets()
                    .open("image_SSBU/" + this.character.getFileName() + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        characterProImageView.setImageBitmap(bitmap);
        playSoundButton.setOnClickListener(this);
        characterNoobImageView.setOnClickListener(this);
        characterProImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Context context = v.getContext();
        MediaPlayer mediaPlayer = new MediaPlayer();
        Intent intent;
        try {
            switch (v.getId()) {
                case R.id.characterNoobImageView:
                    intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.character.getImage());
                    intent.putExtra("imagePro", this.character.getFileName() + ".png");
                    intent.putExtra("mode", "Noob");
                    context.startActivity(intent);
                    break;
                case R.id.characterProImageView:
                    intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.character.getImage());
                    intent.putExtra("imagePro", this.character.getFileName() + ".png");
                    intent.putExtra("mode", "Pro");
                    context.startActivity(intent);
                    break;

                case R.id.playSoundButton:
                    AssetFileDescriptor sample = this.getApplicationContext()
                            .getResources()
                            .getAssets()
                            .openFd("SSBU_SOUNDS/" + this.character.getFileName() + ".wav");
                    mediaPlayer.setDataSource(sample.getFileDescriptor(), sample.getStartOffset(), sample.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}