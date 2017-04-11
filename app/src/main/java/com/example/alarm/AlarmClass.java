package com.example.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by клаудио on 19.03.2017.
 */

public class AlarmClass extends Activity{

    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toast.makeText(AlarmClass.this,"Hello",Toast.LENGTH_LONG).show();
        mediaPlayer = new MediaPlayer();
        bellRinging();
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
    }
}
