package com.example.forrestgump;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int stepSound;
    private static int backgroundSound;
    public boolean backgroundOn = false;
    public SoundPlayer(Context context){
       // soundPool = new SoundPool(int maxStreams, int streamType, int srcQuality);
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        stepSound = soundPool.load(context,R.raw.step,2);
        backgroundSound = soundPool.load(context,R.raw.background,1);
    }

    public void playStepSound(){
        soundPool.play(stepSound,1.0f,1.0f,2,0,1.0f);
    }
    public void playBackgroundSound(){
        soundPool.play(backgroundSound,1.0f,1.0f,1,0,1.0f);
        backgroundOn = true;
    }
}
