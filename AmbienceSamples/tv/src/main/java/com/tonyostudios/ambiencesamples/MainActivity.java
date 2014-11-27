package com.tonyostudios.ambiencesamples;

import android.app.Activity;
import android.os.Bundle;

import com.tonyostudios.ambience.Ambience;


public class MainActivity extends Activity {

    public static final String packageName = "com.tonyostudios.ambiencesamples";
    public static final String activityName = "com.tonyostudios.ambiencesamples.MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //turn ambienceOn
        Ambience.turnOn(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //turn the ambience service off
        Ambience.turnOff();
    }
}
