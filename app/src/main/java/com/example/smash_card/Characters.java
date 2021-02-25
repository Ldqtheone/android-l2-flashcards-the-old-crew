package com.example.smash_card;

import android.os.Parcel;
import android.os.Parcelable;

public class Characters implements Parcelable{

    private final String image;
    private final String name;
    private final String fileName;

    public Characters(String image, String name, String fileName) {
        this.image = image;
        this.name = name;
        this.fileName = fileName;
    }

    protected Characters(Parcel in) {
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

    public static final Parcelable.Creator<Characters> CREATOR = new Parcelable.Creator<Characters>() {
        @Override
        public Characters createFromParcel(Parcel in) {
            return new Characters(in);
        }

        @Override
        public Characters[] newArray(int size) {
            return new Characters[size];
        }
    };
}
