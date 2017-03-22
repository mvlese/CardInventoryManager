package net.leseonline.cardinventorymanager;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import net.leseonline.cardinventorymanager.db.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mlese on 3/21/2017.
 */
public class Utilities {
    private static final MediaPlayer mMediaPlayer = new MediaPlayer();

    private static void playSound(Context context, String soundFile) {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        if (mDatabaseHelper.isSoundEnabled()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }

            try {
                mMediaPlayer.reset();
                AssetFileDescriptor afd = context.getAssets().openFd(soundFile);
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void playPageFlip(Context context) {
        playSound(context, "page-flip-4.mp3");
    }

    public static void playClick(Context context) {
        playSound(context, "button-16.mp3");
    }

    public static void playShutter(Context context) {
        playSound(context, "camera-shutter-click-01.mp3");
    }


}
