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

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> implements View.OnClickListener {

    private List<Characters> characters;

    public CharacterAdapter(List<Characters> characters) {
        this.characters = characters;
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

        Characters character = characters.get(position);
        Picasso.get().load(character.getImage()).into(holder.image);
        holder.charName.setText(character.getName());
        holder.itemView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return this.characters.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rootItem:
                Context context = v.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                break;
        }
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
