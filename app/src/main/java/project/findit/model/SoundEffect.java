package project.findit.model;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import project.findit.R;

public final class SoundEffect {

    public static int FOUND;
    public static int CORRECT;
    public static int INCORRECT;
    public static int START;
    public static int WIN;

    private SoundEffect(){
        throw new IllegalAccessError("Cannot access constructor");
    }

    public static SoundPool buildSoundPool(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        return new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();
    }

    public static void loadSounds(Context context, SoundPool soundPool){
        FOUND = soundPool.load(context, R.raw.sound_found, 1);
        CORRECT = soundPool.load(context, R.raw.sound_correct, 1);
        INCORRECT = soundPool.load(context, R.raw.sound_incorrect, 1);
        START = soundPool.load(context, R.raw.sound_start, 1);
        WIN = soundPool.load(context, R.raw.sound_win, 1);
    }

    public static void playSound(SoundPool soundPool, int sound){
        soundPool.play(sound, 1, 1, 0, 0, 1);
    }
}
