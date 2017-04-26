package com.example.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by клаудио on 19.03.2017.
 */

public class AlarmClass extends Activity{

    MediaPlayer mediaPlayer;
    public static final int NEW_ALARM_TASK = 1;
    AlarmService alarmService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
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
//        alarmService.cancelAlarm();
//        desSerializAlSer();
//        AlarmService alarmService = new AlarmService();
//        alarmService.testCancelAlarm();
        Intent serviceIntent = new Intent(getBaseContext(), AlarmService.class);
        serviceIntent.putExtra("Flag",NEW_ALARM_TASK);
        startService(serviceIntent);
        Log.i("onStopButtonClick", "method end...");
        view.setVisibility(View.INVISIBLE);
    }

//    private void desSerializAlSer(){
//        Log.i("desSerializAlSer", "Desserialize start...");
//        try {
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sender.dat"));
//            try {
//                AlarmService aService = (AlarmService) ois.readObject();
//                PendingIntent pIntent = aService.getSender();
//                Log.i("desSerializAlSer", "pIntent = "+pIntent);
//                AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                aManager.cancel(pIntent);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.i("desSerializAlSer", "Desserialize finished...");
//    }
}
