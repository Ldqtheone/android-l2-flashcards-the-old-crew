package com.example.smash_card.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smash_card.MusicPlayerService;
import com.example.smash_card.SmashCharacter;
import com.example.smash_card.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

import static com.example.smash_card.Utils.playWavSound;

/**
 * Flash card activity show all infos of a given character
 */
public class FlashCardActivity extends AppCompatActivity implements View.OnClickListener, LifecycleObserver {
    private SmashCharacter character;
    private boolean isContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

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
            playWavSound(this.getApplicationContext()
                    .getResources()
                    .getAssets()
                    .openFd("SSBU_ANNOUNCE/"+ this.character.getFileName() +".wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is = this.getApplicationContext()
                    .getResources()
                    .getAssets()
                    .open("SSBU_IMAGES/" + this.character.getFileName() + ".png");
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
        Intent intent;
        try {
            switch (v.getId()) {
                case R.id.characterNoobImageView:
                    intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.character.getImage());
                    intent.putExtra("imagePro", this.character.getFileName() + ".png");
                    intent.putExtra("mode", "Noob");
                    intent.putExtra("url", "http://www.feplanet.net/files/scripts/music.php?song=1592");
                    context.startActivity(intent);
                    break;
                case R.id.characterProImageView:
                    intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.character.getImage());
                    intent.putExtra("imagePro", this.character.getFileName() + ".png");
                    intent.putExtra("mode", "Pro");
                    intent.putExtra("url", "http://www.feplanet.net/files/scripts/music.php?song=1592");
                    context.startActivity(intent);
                    break;

                case R.id.playSoundButton:
                    playWavSound(this.getApplicationContext()
                            .getResources()
                            .getAssets()
                            .openFd("SSBU_SOUNDS/" + this.character.getFileName() + ".wav"));

                    break;
            }
        } catch (IOException e) {
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
        stopService(new Intent(FlashCardActivity.this, MusicPlayerService.class));
        this.isContext = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        if(this.isContext) {
            Intent intent = new Intent(FlashCardActivity.this, MusicPlayerService.class);
            intent.putExtra("url", "http://www.feplanet.net/files/scripts/music.php?song=1592");
            startService(intent);
        }
    }

}