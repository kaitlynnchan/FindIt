package project.findit.model;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import project.findit.R;

public final class Sound {

    public static int FOUND;
    public static int INCORRECT;
    public static int START;
    public static int WIN;

    public static SoundPool soundPool;

    public static void setupAttributes(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();
    }

    public static void setSounds(Context context){
        FOUND = soundPool.load(context, R.raw.sound_found, 1);
        INCORRECT = soundPool.load(context, R.raw.sound_incorrect, 1);
        START = soundPool.load(context, R.raw.sound_start, 1);
        WIN = soundPool.load(context, R.raw.sound_win, 1);
    }

    public static void playSound(int sound){
        soundPool.play(sound, 1, 1, 0, 0, 1);
    }

    public static void destroy(){
        soundPool.release();
        soundPool = null;
    }
}
