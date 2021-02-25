package com.example.smash_card.activities;

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
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.smash_card.Characters;
import com.example.smash_card.Question;
import com.example.smash_card.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.smash_card.Utils.generateMediaplayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Characters goodAnswer;
    private RadioGroup radioGroup;
    private Button confirmButton;
    private List<Characters> characters = new ArrayList<>();
    private List<Characters> charactersAnswers = new ArrayList<>();
    private int score;
    private int numberQuestion;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.getValueIntent();
        Question question = new Question(this.characters);
        TextView questionIndexTextView = findViewById(R.id.questionIndexTextView);
        questionIndexTextView.setText("Question " + this.numberQuestion + "/10");

        Button audioButton = findViewById(R.id.audioButton);
        audioButton.setVisibility(View.INVISIBLE);

        ImageView answeredImageView = findViewById(R.id.answeredImageView);
        answeredImageView.setVisibility(View.INVISIBLE);
        this.confirmButton = findViewById(R.id.confirmButton);
        this.confirmButton.setEnabled(false);
        if (this.numberQuestion == 10) {
            this.confirmButton.setText("Finish !");
        }
        this.radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButtonGenerated;

        this.charactersAnswers = question.getRandomCharacter();
        this.goodAnswer = this.charactersAnswers.get(0);
        this.characters.remove(this.goodAnswer);
        Collections.shuffle(charactersAnswers);


        switch (this.mode) {
            case "Noob":
                answeredImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(this.goodAnswer.getImage()).into(answeredImageView);
                break;
            case "Pro":
                answeredImageView.setVisibility(View.VISIBLE);
                InputStream is = null;
                try {
                    is = this.getApplicationContext()
                            .getResources()
                            .getAssets()
                            .open("image_SSBU/" + this.goodAnswer.getFileName() + ".png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                answeredImageView.setImageBitmap(bitmap);
                break;
            case "VIP":
                audioButton.setVisibility(View.VISIBLE);
                break;
        }


        for (int i = 0; i < 4; i++) {
            radioButtonGenerated = new RadioButton(this);
            radioButtonGenerated.setText(charactersAnswers.get(i).getName());
            this.radioGroup.addView(radioButtonGenerated);
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


        try {
            switch (v.getId()) {
                case R.id.confirmButton:
                    this.getValueIntent();
                    if (selected.getText().toString().equals(this.goodAnswer.getName())) {
                        this.score += 1;
                        handleConfirm(context);
                    } else {
                        alertWrongAnswer(context);
                    }
                    break;

                case R.id.answeredImageView:
                    Intent intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.goodAnswer.getImage());
                    intent.putExtra("imagePro", this.goodAnswer.getFileName() + ".png");
                    intent.putExtra("mode", this.mode);
                    context.startActivity(intent);
                    break;

                case R.id.audioButton:
                    generateMediaplayer(this.getApplicationContext()
                            .getResources()
                            .getAssets()
                            .openFd("SSBU_SOUNDS/" + this.goodAnswer.getFileName() + ".wav"));
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
            intent = new Intent(context, StatsEndQuizActivity.class);
        }
        intent.putExtra("score", this.score);
        intent.putExtra("numberQuestion", this.numberQuestion);
        intent.putExtra("mode", this.mode);
        intent.putParcelableArrayListExtra("characters", (ArrayList<? extends Parcelable>) this.characters);
        context.startActivity(intent);
    }

    private void getValueIntent() {
        Intent srcIntent = getIntent();
        this.mode = srcIntent.getStringExtra("mode");
        this.score = srcIntent.getIntExtra("score", 0);
        this.numberQuestion = srcIntent.getIntExtra("numberQuestion", 1);
        this.characters = srcIntent.getParcelableArrayListExtra("characters");
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setMessage("Êtes-vous sûr de vouloir retourner à l'accueil ?")
                .setCancelable(true)
                .setPositiveButton(
                        "Ok",
                        (dialog, id) -> {
                            dialog.cancel();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            MainActivity.this.startActivity(intent);
                        })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.confirmButton = findViewById(R.id.confirmButton);
        this.confirmButton.setEnabled(true);
    }

    private void alertWrongAnswer(Context context) throws JSONException {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Mauvaise réponse , la réponse est : " + this.goodAnswer.getName())
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