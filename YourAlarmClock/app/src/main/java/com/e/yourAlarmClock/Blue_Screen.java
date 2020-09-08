package com.e.yourAlarmClock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class Blue_Screen extends Activity {

    private String blueBeat;
    public static MediaPlayer mMediaPlayer = new MediaPlayer();
    private Vibrator vib;
    long[] pattern = {100,30,100,30,100,200,200,30,200,30,200,200,100,30,100,30,100}; // Morse Code SOS


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_screen);

        // For Android 10
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                setShowWhenLocked(true);
                setTurnScreenOn(true);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);




        loadPrefs();

        switch (blueBeat) {
            case "Beat 1":
                mMediaPlayer = MediaPlayer.create(this, R.raw.dark);
                break;
            case "Beat 2":
                mMediaPlayer = MediaPlayer.create(this, R.raw.tlou);
                break;
            case "Beat 3":
                mMediaPlayer = MediaPlayer.create(this, R.raw.beat_one);
                break;
            case "Beat 4":
                mMediaPlayer = MediaPlayer.create(this, R.raw.beat_two);
                break;
            case "Beat 5":
                mMediaPlayer = MediaPlayer.create(this, R.raw.re);
                break;
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);

        mMediaPlayer.setLooping(true);

//        not working but just in case
//        mMediaPlayer.setVolume(0.09f , 0.09f);


        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createWaveform(pattern,0));
        } else {
            vib.vibrate(pattern,0);
        }


        mMediaPlayer.start();






        Button dism = findViewById(R.id.Dismiss);
        final MediaPlayer finalMMediaPlayer = mMediaPlayer;

        dism.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                MainActivity.canc = true;
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);

                PrintWriter writer = null;
                try {
                    FileOutputStream fos = openFileOutput(MainActivity.FILE_NAME, MODE_PRIVATE);
                    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                writer.print("");
                writer.close();


                finalMMediaPlayer.stop();
                vib.cancel();

                finish();
            }
        });
    }


    private void loadPrefs() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(MainActivity.FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));


            String ss;

            ss = reader.readLine();
            ss = reader.readLine();
            ss = reader.readLine();
            blueBeat = ss;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        // null
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);

        // TODO gia android 10

    }


}
