package com.example.alarm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by клаудио on 13.03.2017.
 */

public class TestAlarm extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "WakeUp",Toast.LENGTH_LONG).show();
    }
}
