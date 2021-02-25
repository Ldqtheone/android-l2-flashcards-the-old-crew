package com.example.smash_card;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

public class Utils {
    /**
     * Play sound from Wav File
     * @param assetFileDescriptor from asset wav file
     * @throws IOException
     */
    public static void playWavSound(AssetFileDescriptor assetFileDescriptor) throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        AssetFileDescriptor sample = assetFileDescriptor;
        mediaPlayer.setDataSource(sample.getFileDescriptor(), sample.getStartOffset(), sample.getLength());
        mediaPlayer.prepare();
        mediaPlayer.start();

    }

        /**
         * Get Random Number In Range
         * @param min
         * @param max
         * @return int
         */
    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}

