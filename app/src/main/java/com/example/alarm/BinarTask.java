package com.example.alarm;

import android.util.Log;

/**
 * Created by клаудио on 21.05.2017.
 */

public class BinarTask {

    StringBuilder strB = new StringBuilder();
    int []box = new int[4];

    private void test(){
    for(int i = 0; i<=3; i++){
        long a = System.nanoTime();
        System.out.println(a);
        if(a%42 == 0 || a%24 == 0 || a%13 == 0 || a%6 == 0 || a%21 == 0) {
            box[i] = 0;
            Log.d("Test","if 1 "+box[i]);
        }
        else{
            box[i]=1;
            Log.d("Test","if 2 "+box[i]);
        }
    }

        for (int a = 0; a<=3; a++){
            int b = box[a];
            System.out.println(b);
            strB.append(b);
        }
        Log.d("Test","strB = "+strB);;
    }
}
