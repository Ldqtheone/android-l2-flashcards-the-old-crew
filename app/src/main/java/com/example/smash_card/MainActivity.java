package com.example.smash_card;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private JSONObject goodAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<JSONObject> characters =  getRandomCharacter();
        ImageView answeredImageView = findViewById(R.id.answeredImageView);
        Button confirmButton = findViewById(R.id.confirmButton);

        RadioButton radioButton = findViewById(R.id.radioButton);
        RadioButton radioButton2 = findViewById(R.id.radioButton2);
        RadioButton radioButton3 = findViewById(R.id.radioButton3);
        RadioButton radioButton4 = findViewById(R.id.radioButton4);

        this.goodAnswer = characters.get(0);

        Collections.shuffle(characters);

        try {
            Picasso.get().load(this.goodAnswer.getString("image")).into(answeredImageView);
            radioButton.setText(characters.get(0).getString("name"));
            radioButton2.setText(characters.get(1).getString("name"));
            radioButton3.setText(characters.get(2).getString("name"));
            radioButton4.setText(characters.get(3).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RadioGroup radioButtonGroup=findViewById(R.id.radioGroup);
        RadioButton selected = findViewById(radioButtonGroup.getCheckedRadioButtonId());

        switch (v.getId()){
            case R.id.confirmButton:
                try {
                    if(selected.getText().toString().equals(this.goodAnswer.getString("name"))){
                        Log.i("answer","Victory");
                    }else{
                        Log.i("answer","Tu pus , c'était " + this.goodAnswer.getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private JSONObject loadJSONFromAsset() {

        String json = null;
        JSONObject datas = null;

        try {
            InputStream is = this.getApplicationContext().getResources().getAssets().open("datas.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        try {
            datas = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return datas;
    }

    private List<JSONObject> getRandomCharacter() {
        JSONObject datas = loadJSONFromAsset();
        int datasLenght = 0;
        List<JSONObject> charactersList = new ArrayList<>();

        try {
            datasLenght = datas.getJSONArray("datas").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 4; i++){
            int randomChar = this.getRandomNumberInRange(0, datasLenght - 1);
            JSONObject character = null;

            try {
                character = (JSONObject) datas.getJSONArray("datas").get(randomChar);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(charactersList.isEmpty()){
                charactersList.add(character);
            }else if(charactersList.contains(character)){
                i--;
            }else{
                charactersList.add(character);
            }
        }

        return charactersList;
    }
}