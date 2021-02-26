package com.example.smash_card.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.smash_card.MusicPlayerService;
import com.example.smash_card.SmashCharacter;
import com.example.smash_card.InfoGame;
import com.example.smash_card.Question;
import com.example.smash_card.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.smash_card.Utils.playWavSound;

/**
 * quiz activity
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, LifecycleObserver {

    private static final String TAG = "GameActibity";
    private SmashCharacter goodAnswer;
    private RadioGroup radioGroup;
    private Button confirmButton;
    private List<SmashCharacter> characters = new ArrayList<>();
    private List<SmashCharacter> charactersAnswers = new ArrayList<>();
    private InfoGame infoGame = new InfoGame();
    private String urlSong;
    private boolean isContext;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        this.getValueIntent();
        Question question = new Question(this.characters);
        TextView questionIndexTextView = findViewById(R.id.questionIndexTextView);
        questionIndexTextView.setText("Question " + this.infoGame.getNumberQuestion() + "/10");

        Button audioButton = findViewById(R.id.audioButton);
        audioButton.setVisibility(View.INVISIBLE);

        ImageView answeredImageView = findViewById(R.id.answeredImageView);
        answeredImageView.setVisibility(View.INVISIBLE);
        this.confirmButton = findViewById(R.id.confirmButton);
        this.confirmButton.setEnabled(false);
        if (this.infoGame.getNumberQuestion() == 10) {
            this.confirmButton.setText("Finish !");
        }
        this.radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButtonGenerated;

        this.charactersAnswers = question.getRandomCharacter();
        this.goodAnswer = this.charactersAnswers.get(0);
        this.characters.remove(this.goodAnswer);
        Collections.shuffle(charactersAnswers);

        switch (this.infoGame.getMode()) {
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
                            .open("SSBU_IMAGES/" + this.goodAnswer.getFileName() + ".png");
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
                    if (selected.getText().toString().equals(this.goodAnswer.getName())) {
                        this.infoGame.increaseScoreByOne();
                        handleConfirm(context);
                    } else {
                        alertWrongAnswer(context);
                    }
                    break;

                case R.id.answeredImageView:
                    Intent intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.goodAnswer.getImage());
                    intent.putExtra("imagePro", this.goodAnswer.getFileName() + ".png");
                    intent.putExtra("mode", this.infoGame.getMode());
                    context.startActivity(intent);
                    break;

                case R.id.audioButton:
                    playWavSound(this.getApplicationContext()
                            .getResources()
                            .getAssets()
                            .openFd("SSBU_SOUNDS/" + this.goodAnswer.getFileName() + ".wav"));
                    break;
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * handle if the quiz is over
     * redirect to next question if not
     * redirect to stats screen if over
     *
     * @param context
     */
    private void handleConfirm(Context context) {
        Intent intent;
        if (this.infoGame.getNumberQuestion() < 10) {
            this.infoGame.increaseNumberQuestionByOne();
            intent = new Intent(context, GameActivity.class);
        } else {
            intent = new Intent(context, StatsEndQuizActivity.class);
            stopService(new Intent(GameActivity.this, MusicPlayerService.class));
        }
        intent.putExtra("infoGame", infoGame);
        intent.putExtra("startMusic", true);
        intent.putParcelableArrayListExtra("characters", (ArrayList<? extends Parcelable>) this.characters);
        context.startActivity(intent);
    }

    /**
     * update game's and character's infos
     */
    private void getValueIntent() {
        Intent srcIntent = getIntent();
        if (srcIntent.getParcelableExtra("infoGame") != null) {
            this.infoGame = srcIntent.getParcelableExtra("infoGame");
        }
        if (srcIntent.getStringExtra("mode") != null) {
            this.infoGame.setMode(srcIntent.getStringExtra("mode"));
            Intent intent = new Intent(GameActivity.this, MusicPlayerService.class);

            switch (srcIntent.getStringExtra("mode")) {
                case "Noob":
                    this.urlSong = "http://www.feplanet.net/files/scripts/music.php?song=1595";
                    break;
                case "Pro":
                    this.urlSong = "http://www.feplanet.net/files/scripts/music.php?song=1606";
                    break;
                case "VIP":
                    this.urlSong = "http://www.feplanet.net/files/scripts/music.php?song=1596";
                    break;
            }
            intent.putExtra("url", this.urlSong);
            startService(intent);
        }


        this.characters = srcIntent.getParcelableArrayListExtra("characters");
    }

    /**
     * back to home activity
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setMessage("Êtes-vous sûr de vouloir retourner à l'accueil ?")
                .setCancelable(true)
                .setPositiveButton(
                        "Ok",
                        (dialog, id) -> {
                            dialog.cancel();
//                            GameActivity.this.musicPlayer.stopSound();
                            Intent intent = new Intent(GameActivity.this, HomeActivity.class);
                            stopService(new Intent(GameActivity.this, MusicPlayerService.class));
                            GameActivity.this.startActivity(intent);
                        })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * set confirm button enabled if a value is selected
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.confirmButton = findViewById(R.id.confirmButton);
        this.confirmButton.setEnabled(true);
    }

    /**
     * give correction if the answer was wrong
     */
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

    @Override
    protected void onStart() {
        super.onStart();
        this.isContext = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.isContext = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        stopService(new Intent(GameActivity.this, MusicPlayerService.class));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        if(this.isContext) {
            Intent intent = new Intent(GameActivity.this, MusicPlayerService.class);
            intent.putExtra("url", this.urlSong);
            startService(intent);
        }
    }
}