package com.example.alarm;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.IOException;

/**
 * Created by клаудио on 19.03.2017.
 */

public class AlarmClass extends Activity{
    private MediaPlayer mediaPlayer;
    private AlarmService alarmService;
    private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        //включает экран, когда он залочен
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
       //--------//
        Toast.makeText(AlarmClass.this,"Hello",Toast.LENGTH_LONG).show();
        mediaPlayer = new MediaPlayer();
        bellRinging();
        alarmService = new AlarmService();
    }

    private void bellRinging(){
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        try {
            mediaPlayer.setDataSource(AlarmClass.this,alert);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlayer(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public void onStopButtonClick(View view) {
        stopPlayer();
        serviceIntent = new Intent(getApplicationContext(), AlarmService.class);
        serviceIntent.addFlags(MainActivity.NEW_ALARM_EVENT);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startService(serviceIntent);
            }
        });
        thread.start();

        Log.i("onStopButtonClick", "method end...");
        view.setVisibility(View.INVISIBLE);
    }
}
