package com.example.smash_card;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

        List<JSONObject> characters =  question.getRandomCharacter();

        this.getValueIntent();
        TextView questionIndexTextView = findViewById(R.id.questionIndexTextView);
        questionIndexTextView.setText("Question " + this.numberQuestion + "/10");


        ImageView answeredImageView = findViewById(R.id.answeredImageView);
        this.confirmButton = findViewById(R.id.confirmButton);
        this.confirmButton.setEnabled(false);

        this.radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButtonGenerated;

        this.goodAnswer = characters.get(0);

        Collections.shuffle(characters);

        try {
            Picasso.get().load(this.goodAnswer.getString("image")).into(answeredImageView);

            for(int i = 0; i < 4; i++) {
                radioButtonGenerated = new RadioButton(this);
                radioButtonGenerated.setText(characters.get(i).getString("name"));
                this.radioGroup.addView(radioButtonGenerated);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        confirmButton.setOnClickListener(this);
        answeredImageView.setOnClickListener(this);
        this.radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        this.radioGroup = findViewById(R.id.radioGroup);
        RadioButton selected = findViewById(this.radioGroup.getCheckedRadioButtonId());
        Context context = v.getContext();

        try{
            switch (v.getId()){
                case R.id.confirmButton:
                    this.getValueIntent();
                    this.numberQuestion += 1;

                    if(this.numberQuestion <= 10) {
                        if (selected.getText().toString().equals(this.goodAnswer.getString("name"))) {
                            Log.i("answer", "Victory");
                            this.score += 1;
                            Log.i("Score", "onClick: " + this.score);
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("score", this.score);
                            intent.putExtra("numberQuestion", this.numberQuestion);
                            context.startActivity(intent);
                        } else {
                            alertWrongAnswer(context, this.score, this.numberQuestion);
                        }
                    }
                    else {
                        Log.i("Score", "onClick: Ecran de score");
                    }
                    
                    break;
                case R.id.answeredImageView:
                    Intent intent = new Intent(context, FullScreenImageActivity.class);
                    intent.putExtra("image", this.goodAnswer.getString("image"));
                    context.startActivity(intent);
                    break;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getValueIntent() {
        Intent srcIntent = getIntent();
        this.score = srcIntent.getIntExtra("score", 0);
        this.numberQuestion = srcIntent.getIntExtra("numberQuestion", 1);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.confirmButton = findViewById(R.id.confirmButton);
        this.confirmButton.setEnabled(true);
    }

    private void alertWrongAnswer(Context context, int score, int numberQuestion) throws JSONException {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Mauvaise réponse , la réponse est : " + this.goodAnswer.getString("name"));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("score", score);
                        intent.putExtra("numberQuestion", numberQuestion);
                        context.startActivity(intent);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}