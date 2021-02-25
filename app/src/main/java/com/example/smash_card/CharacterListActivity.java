package com.example.smash_card;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CharacterListActivity extends AppCompatActivity {

    public static final String TAG = "CharacterListActivity";
    private List<Characters> characters;
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        characters = new ArrayList<>();

        loadCharsFromApi();

        adapter = new CharacterAdapter(characters);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadCharsFromApi() {
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

                    // J'ai modifié données donc rafraichie l'UI
                    runOnUiThread(() -> adapter.notifyDataSetChanged());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Log.i(TAG, "loadRatesFromApi: Started HTTP Request");
    }
}