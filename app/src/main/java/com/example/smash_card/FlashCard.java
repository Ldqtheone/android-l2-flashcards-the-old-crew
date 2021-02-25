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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class FlashCard extends AppCompatActivity implements View.OnClickListener {
    private String image;
    private String imagePro;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);
        ImageView imageView = findViewById(R.id.imageView);
        ImageView imageView2 = findViewById(R.id.imageView2);
        Intent srcIntent = getIntent();
        this.image = srcIntent.getStringExtra("image");
        this.imagePro = srcIntent.getStringExtra("imagePro");
        this.filename = srcIntent.getStringExtra("filename");
        TextView nameText = findViewById(R.id.nameText);
        nameText.setText(srcIntent.getStringExtra("name"));
        Picasso.get().load(this.image).into(imageView);
        InputStream is = null;
        try {
            is = this.getApplicationContext()
                    .getResources()
                    .getAssets()
                    .open("image_SSBU/" + this.filename + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        imageView2.setImageBitmap(bitmap);

    }

    @Override
    public void onClick(View v) {

        Context context = v.getContext();
        MediaPlayer mediaPlayer = new MediaPlayer();
        Intent intent;
        try {
            switch (v.getId()) {
                case R.id.imageView:
                    intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.image);
                    intent.putExtra("imagePro", this.filename + ".png");
                    intent.putExtra("mode", "noob");
                    context.startActivity(intent);
                    break;
                case R.id.imageView2:
                    intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.image);
                    intent.putExtra("imagePro", this.filename + ".png");
                    intent.putExtra("mode", "pro");
                    context.startActivity(intent);
                    break;

                case R.id.playSoundButton:
                    AssetFileDescriptor sample = this.getApplicationContext()
                            .getResources()
                            .getAssets()
                            .openFd("SSBU_SOUNDS/" + this.filename + ".wav");
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