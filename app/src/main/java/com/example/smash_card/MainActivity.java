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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView answeredImageView = findViewById(R.id.answeredImageView);
        Picasso.get().load("https://static.wikia.nocookie.net/ssb/images/4/44/Mario_SSBU.png/revision/latest/scale-to-width-down/985?cb=20180612204958").into(answeredImageView);
        Button confirmButton = findViewById(R.id.confirmButton);
        String goodAnswer = "Mario";

        confirmButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        RadioGroup radioButtonGroup=findViewById(R.id.radioGroup);
        RadioButton selected = findViewById(radioButtonGroup.getCheckedRadioButtonId());

        switch (v.getId()){
            case R.id.confirmButton:
                if(selected.getText().toString().equals("Mario")){
                    Log.i("answer","victory");
                }else{
                    Log.i("answer","tu pue ct mario");

                }
                break;
        }
    }
}