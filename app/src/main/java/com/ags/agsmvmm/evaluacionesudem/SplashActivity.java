package com.ags.agsmvmm.evaluacionesudem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by USUARIO on 25/03/2018.
 */

public class SplashActivity extends AppCompatActivity {

    private static final long SPLAH_ACTIVITY_TIME = 2000;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity( intent );
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask,SPLAH_ACTIVITY_TIME);
    }
}
