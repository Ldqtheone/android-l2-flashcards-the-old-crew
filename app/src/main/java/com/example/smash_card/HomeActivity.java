package com.example.smash_card;
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
    private List<Characters> characters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button startQuizButton = findViewById(R.id.startQuizButton);
        Button aboutButton = findViewById(R.id.aboutButton);
        Button charactersButton = findViewById(R.id.charactersButton);
        startQuizButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);

        this.loadDataFromApi(charactersButton);

    }

    private void loadDataFromApi(Button charactersButton) {
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
                        characters.add(new Characters(character.getString("image"), character.getString("name"), character.getString("filename")));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                charactersButton.setOnClickListener(HomeActivity.this);
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
                Intent intent = new Intent(HomeActivity.this, About.class);
                HomeActivity.this.startActivity(intent);
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
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("mode", selectedItems.get(0));
                        context.startActivity(intent);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}