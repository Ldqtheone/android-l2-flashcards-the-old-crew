package com.example.smash_card;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private JSONObject goodAnswer;
    private RadioGroup radioGroup;
    private Button confirmButton;

    private int score;
    private int numberQuestion;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream datas;
        Question question = null;
        try {
            datas = this.getApplicationContext().getResources().getAssets().open("datas.json");
            question = new Question(datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<JSONObject> characters = question.getRandomCharacter();

        this.getValueIntent();
        TextView questionIndexTextView = findViewById(R.id.questionIndexTextView);
        questionIndexTextView.setText("Question " + this.numberQuestion + "/10");

        Button audioButton = findViewById(R.id.audioButton);
        audioButton.setVisibility(View.INVISIBLE);

        ImageView answeredImageView = findViewById(R.id.answeredImageView);
        answeredImageView.setVisibility(View.INVISIBLE);
        this.confirmButton = findViewById(R.id.confirmButton);
        this.confirmButton.setEnabled(false);

        this.radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButtonGenerated;

        this.goodAnswer = characters.get(0);

        Collections.shuffle(characters);

        try {
            switch (this.mode) {
                case "Noob":
                    answeredImageView.setVisibility(View.VISIBLE);
                    Picasso.get().load(this.goodAnswer.getString("image")).into(answeredImageView);
                    break;
                case "Pro":
                    answeredImageView.setVisibility(View.VISIBLE);
                    InputStream is = this.getApplicationContext()
                            .getResources()
                            .getAssets()
                            .open("image_SSBU/" + this.goodAnswer.getString("filename") + ".png");
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    answeredImageView.setImageBitmap(bitmap);
                    break;
                case "VIP":
                    audioButton.setVisibility(View.VISIBLE);
                    break;
            }


            for (int i = 0; i < 4; i++) {
                radioButtonGenerated = new RadioButton(this);
                radioButtonGenerated.setText(characters.get(i).getString("name"));
                this.radioGroup.addView(radioButtonGenerated);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        confirmButton.setOnClickListener(this);
        answeredImageView.setOnClickListener(this);
        audioButton.setOnClickListener(this);
        this.radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        this.radioGroup = findViewById(R.id.radioGroup);
        RadioButton selected = findViewById(this.radioGroup.getCheckedRadioButtonId());
        Context context = v.getContext();
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            switch (v.getId()) {
                case R.id.confirmButton:
                    this.getValueIntent();
                    if (selected.getText().toString().equals(this.goodAnswer.getString("name"))) {
                        this.score += 1;
                        handleConfirm(context);
                    } else {
                        alertWrongAnswer(context);
                    }
                    break;

                case R.id.answeredImageView:
                    Intent intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.goodAnswer.getString("image"));
                    intent.putExtra("imagePro", this.goodAnswer.getString("filename") + ".png");
                    intent.putExtra("mode", this.mode);
                    context.startActivity(intent);
                    break;

                case R.id.audioButton:
                    AssetFileDescriptor sample = this.getApplicationContext()
                            .getResources()
                            .getAssets()
                            .openFd("SSBU_SOUNDS/" + this.goodAnswer.getString("filename") + ".wav");
                    mediaPlayer.setDataSource(sample.getFileDescriptor(), sample.getStartOffset(), sample.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    break;
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConfirm(Context context) {
        Intent intent;
        if (this.numberQuestion < 10) {
            this.numberQuestion += 1;
            intent = new Intent(context, MainActivity.class);
        } else {
            intent = new Intent(context, StatsEndQuiz.class);
        }
        intent.putExtra("score", this.score);
        intent.putExtra("numberQuestion", this.numberQuestion);
        intent.putExtra("mode", this.mode);
        context.startActivity(intent);
    }

    private void getValueIntent() {
        Intent srcIntent = getIntent();
        this.mode = srcIntent.getStringExtra("mode");
        this.score = srcIntent.getIntExtra("score", 0);
        this.numberQuestion = srcIntent.getIntExtra("numberQuestion", 1);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.confirmButton = findViewById(R.id.confirmButton);
        this.confirmButton.setEnabled(true);
    }

    private void alertWrongAnswer(Context context) throws JSONException {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Mauvaise réponse , la réponse est : " + this.goodAnswer.getString("name"))
                .setCancelable(false)
                .setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                handleConfirm(context);
                            }
                        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}