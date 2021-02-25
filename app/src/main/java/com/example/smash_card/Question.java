package com.example.smash_card;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Question {

    private List<Characters> charactersList;

    public Question(List<Characters> charactersList) {
        this.charactersList = charactersList;
    }



    public List<Characters> getRandomCharacter() {
        int dataLength;

        List<Characters> charactersListTemp = new ArrayList<>();
        dataLength = this.charactersList.size();

        for (int i = 0; i < 4; i++){
            int randomCharIndex = this.getRandomNumberInRange(0, dataLength - 1);
            Characters character = this.charactersList.get(randomCharIndex);

            if(charactersListTemp.isEmpty()){
                charactersListTemp.add(character);
            }else if(charactersListTemp.contains(character)){
                i--;
            }else{
                charactersListTemp.add(character);
            }
        }
        return charactersListTemp;
    }

    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
