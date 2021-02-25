package com.example.smash_card;

import android.os.Parcel;
import android.os.Parcelable;

public class InfoGame implements Parcelable {

    private String mode;
    private int score;
    private int numberQuestion;

    public InfoGame() {
        this.mode = "Noob";
        this.score = 0;
        this.numberQuestion = 1;
    }

    protected InfoGame(Parcel in) {
        mode = in.readString();
        score = in.readInt();
        numberQuestion = in.readInt();
    }

    public String getMode() {
        return this.mode;
    }

    public int getScore() {
        return this.score;
    }

    public int getNumberQuestion() {
        return this.numberQuestion;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNumberQuestion(int numberQuestion) {
        this.numberQuestion = numberQuestion;
    }

    public void increaseScore() {
        this.score += 1;
    }

    public void increaseNumberQuestion() {
        this.numberQuestion += 1;
    }

    public static final Creator<InfoGame> CREATOR = new Creator<InfoGame>() {
        @Override
        public InfoGame createFromParcel(Parcel in) {
            return new InfoGame(in);
        }

        @Override
        public InfoGame[] newArray(int size) {
            return new InfoGame[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mode);
        dest.writeInt(score);
        dest.writeInt(numberQuestion);
    }
}
