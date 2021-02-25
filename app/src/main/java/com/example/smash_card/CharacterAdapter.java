package com.example.smash_card;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder>{

    private List<JSONObject> question;

    public CharacterAdapter(List<JSONObject> question) {
        this.question = question;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_character, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterAdapter.ViewHolder holder, int position) {

        try {
            JSONObject allchars = this.question.get(position);
            Picasso.get().load(allchars.getString("image")).into(holder.image);
            holder.charName.setText(allchars.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return this.question.size();
    }

   /* @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rootItem) {
            Context context = v.getContext();
            JSONObject character = (JSONObject) v.getTag();
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("question", character);
            context.startActivity(intent);
        }
    }*/

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView image;
        final TextView charName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Récupération des items de la view
            image = itemView.findViewById(R.id.characterImageView);
            charName = itemView.findViewById(R.id.characterTextView);
        }
    }
}
