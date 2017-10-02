package com.example.wesniemarcelin.chaseweatherappcodingchallenge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainWeatherActivity extends AppCompatActivity {
    Fragment mContent;
    SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_layout);
        prefs = getSharedPreferences("com.example.wesniemarcelin.chaseweatherappcodingchallenge", MODE_PRIVATE);


        if(savedInstanceState != null){
            Log.d("onSaveInstanceState", "onCreate: of Activity");

            mContent = getSupportFragmentManager().getFragment(savedInstanceState,"SearchFragment");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState,"searchFragment",mContent);
//        Log.d("onSaveInstanceState", "onSaveInstanceState: of Activity");
//        outState.putString("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.activity_main_fragment, new SearchWeatherFragment())
                    .commit();

            Log.d("ON RESUME: ", "This is the first time");
            prefs.edit().putBoolean("firstrun", false).apply();
        }
        else {
            Log.d("ON RESUME: ", "THis is not the first time");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.activity_main_fragment, new ViewSavedWeatherFragment())
                    .commit();

        }

    }
}
