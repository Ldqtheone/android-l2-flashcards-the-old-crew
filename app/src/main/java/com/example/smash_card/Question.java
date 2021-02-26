package com.example.smash_card;
import java.util.ArrayList;
import java.util.List;


import static com.example.smash_card.Utils.getRandomNumberInRange;

/**
 * Class Question
 */
public class Question {

    private List<SmashCharacter> charactersList;

    public Question(List<SmashCharacter> charactersList) {
        this.charactersList = charactersList;
    }

    /**
     * Return 4 random characters from character list
     * @return List<SmashCharacter> info fighters smash bros
     */
    public List<SmashCharacter> getRandomCharacter() {
        int dataLength;

        List<SmashCharacter> charactersListTemp = new ArrayList<>();
        dataLength = this.charactersList.size();

        for (int i = 0; i < 4; i++){
            int randomCharIndex = getRandomNumberInRange(0, dataLength - 1);
            SmashCharacter character = this.charactersList.get(randomCharIndex);

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


}
