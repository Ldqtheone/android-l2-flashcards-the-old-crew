package com.example.smash_card;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Question {

    private InputStream dataJson;
    private JSONObject charactersData;

    public Question(InputStream dataJson) {
        this.dataJson = dataJson;
        this.charactersData = this.loadJSONFromAsset();
    }

    public JSONObject getCharactersData() {
        return charactersData;
    }

    private JSONObject loadJSONFromAsset() {

        String json;
        JSONObject datas = null;

        try {
            int size = this.dataJson.available();
            byte[] buffer = new byte[size];
            this.dataJson.read(buffer);
            this.dataJson.close();
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

    private void removeQuestion(int characterToRemove){
        try {
            this.charactersData.getJSONArray("datas").remove(characterToRemove);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<JSONObject> getRandomCharacter() {
        int datasLenght = 0;
        int charIndex = 0;
        List<JSONObject> charactersList = new ArrayList<>();

        try {
            datasLenght = this.charactersData.getJSONArray("datas").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 4; i++){
            int randomCharIndex = this.getRandomNumberInRange(0, datasLenght - 1);
            JSONObject character = null;

            try {
                character = (JSONObject) this.charactersData.getJSONArray("datas").get(randomCharIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(charactersList.isEmpty()){
                charIndex = randomCharIndex;
                charactersList.add(character);
            }else if(charactersList.contains(character)){
                i--;
            }else{
                charactersList.add(character);
            }
        }

        this.removeQuestion(charIndex);

        return charactersList;
    }

    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
