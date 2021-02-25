package com.example.smash_card.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.smash_card.SmashCharacter;
import com.example.smash_card.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "HomeActivity";
    private List<SmashCharacter> characters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button startQuizButton = findViewById(R.id.startQuizButton);
        Button aboutButton = findViewById(R.id.aboutButton);
        Button charactersButton = findViewById(R.id.charactersButton);
        aboutButton.setOnClickListener(this);
        this.loadDataFromApi(charactersButton, startQuizButton);
    }

    private void loadDataFromApi(Button charactersButton, Button startQuizButton) {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("http://gryt.tech:8080/smashbros/")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = Objects.requireNonNull(response.body()).string();

                try {
                    JSONObject jsonObject = new JSONObject(body);
                    JSONArray datas = jsonObject.getJSONArray("datas");

                    for (int i = 0; i < datas.length() -1; i++){
                        JSONObject character = (JSONObject) datas.get(i);
                        characters.add(new SmashCharacter(character.getString("image"), character.getString("name"), character.getString("filename")));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                charactersButton.setOnClickListener(HomeActivity.this);
                startQuizButton.setOnClickListener(HomeActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();

        switch (v.getId()){
            case R.id.startQuizButton:
                try {
                    this.dialogGameMode(context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.aboutButton:
                Intent intentAbout = new Intent(HomeActivity.this, AboutActivity.class);
                HomeActivity.this.startActivity(intentAbout);
                break;
            case R.id.charactersButton:
                Intent charListIntent = new Intent(HomeActivity.this, CharacterListActivity.class);
                charListIntent.putParcelableArrayListExtra("characters", (ArrayList<? extends Parcelable>) characters);
                HomeActivity.this.startActivity(charListIntent);
                break;
        }
    }

    private void dialogGameMode(Context context) throws JSONException {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        String[] mode = new String[] {"Noob","Pro", "VIP"};
        ArrayList<String> selectedItems = new ArrayList<>();
        selectedItems.add(Arrays.asList(mode).get(0));
        builder1.setCancelable(true)
                .setTitle("Choix de la difficulté")
                .setSingleChoiceItems(mode, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItems.clear();
                        selectedItems.add(Arrays.asList(mode).get(which));
                        Log.i("test", "onClick: " + selectedItems);
                    }
                })
                .setPositiveButton(
                "Prêt au combat !",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(context, GameActivity.class);
                        intent.putExtra("mode", selectedItems.get(0));
                        intent.putParcelableArrayListExtra("characters", (ArrayList<? extends Parcelable>) characters);
                        context.startActivity(intent);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setMessage("Êtes-vous sûr de vouloir quitter Super Smash Card ?")
                .setCancelable(true)
                .setPositiveButton(
                        "Ok",
                        (dialog, id) -> {
                            dialog.cancel();
                            finishAffinity();
                        })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}