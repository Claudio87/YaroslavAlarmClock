package com.example.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by клаудио on 19.03.2017.
 */

public class AlarmClass extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(AlarmClass.this,"Hello",Toast.LENGTH_LONG).show();
    }

}
