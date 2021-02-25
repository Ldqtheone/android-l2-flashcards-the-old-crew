package com.example.smash_card;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Class Smash Character
 */
public class SmashCharacter implements Parcelable{

    private final String image;
    private final String name;
    private final String fileName;

    public SmashCharacter(String image, String name, String fileName) {
        this.image = image;
        this.name = name;
        this.fileName = fileName;
    }

    protected SmashCharacter(Parcel in) {
        image = in.readString();
        name = in.readString();
        fileName = in.readString();
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(fileName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SmashCharacter> CREATOR = new Parcelable.Creator<SmashCharacter>() {
        @Override
        public SmashCharacter createFromParcel(Parcel in) {
            return new SmashCharacter(in);
        }

        @Override
        public SmashCharacter[] newArray(int size) {
            return new SmashCharacter[size];
        }
    };
}
