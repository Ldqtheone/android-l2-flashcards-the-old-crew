package com.example.smash_card.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;

import com.example.smash_card.CharacterAdapter;
import com.example.smash_card.MusicPlayerService;
import com.example.smash_card.SmashCharacter;
import com.example.smash_card.R;

import java.util.List;

/**
 * Activity to display all characters in banners
 */
public class CharacterListActivity extends AppCompatActivity implements LifecycleObserver {

    public static final String TAG = "CharacterListActivity";
    private List<SmashCharacter> characters;
    private CharacterAdapter adapter;
    private boolean isContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        Intent srcIntent = getIntent();

        characters =  srcIntent.getParcelableArrayListExtra("characters");

        adapter = new CharacterAdapter(characters);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.isContext = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        stopService(new Intent(CharacterListActivity.this, MusicPlayerService.class));
        this.isContext = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        if(this.isContext) {
            Intent intent = new Intent(CharacterListActivity.this, MusicPlayerService.class);
            intent.putExtra("url", "http://www.feplanet.net/files/scripts/music.php?song=1592");
            startService(intent);
        }
    }


}