package com.example.smash_card;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class Utils {
    /**
     *
     * @param assetFileDescriptor
     * @throws IOException
     */
    public static void generateMediaplayer(AssetFileDescriptor assetFileDescriptor) throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        AssetFileDescriptor sample = assetFileDescriptor;
        mediaPlayer.setDataSource(sample.getFileDescriptor(), sample.getStartOffset(), sample.getLength());
        mediaPlayer.prepare();
        mediaPlayer.start();

    }
}

