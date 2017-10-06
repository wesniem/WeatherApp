package com.example.wesniemarcelin.chaseweatherappcodingchallenge.view;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.wesniemarcelin.chaseweatherappcodingchallenge.R;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.presenter.SearchWeatherPresenter;

import butterknife.ButterKnife;

import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WEATHER_ACTIVITY;

public class MainWeatherActivity extends AppCompatActivity {
    SearchWeatherPresenter mSearchWeatherPresenter;
    String mCityName= "";
    Fragment mContent;
    SharedPreferences prefs = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_layout);
        FragmentManager fm = getSupportFragmentManager();
        mContent = fm.findFragmentByTag("fragmentTag");
        prefs = getSharedPreferences(WEATHER_ACTIVITY, MODE_PRIVATE);
        ButterKnife.bind(this);
//        mSearchWeatherPresenter = new SearchWeatherPresenter(this);


//        if (mContent == null) {
//            FragmentTransaction ft = fm.beginTransaction();
//            mContent = new SearchWeatherFragment();
//            ft.add(android.R.id.content, mContent, "fragmentTag");
//            ft.commit();
//        }

        if(savedInstanceState != null){
            Log.d("onSaveInstanceState", "onCreate: of Activity");

            mContent = getSupportFragmentManager().getFragment(savedInstanceState,"SearchFragment");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, "searchFragment", mContent);
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
                    .replace(R.id.activity_main_fragment, new SearchWeatherFragment())
                    .addToBackStack(null)
                    .commit();

            Log.d("ON RESUME: ", "This is the first time");
            prefs.edit().putBoolean("firstrun", false).apply();
        } else {
            Log.d("ON RESUME: ", "This is not the first time");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main_fragment, new ViewSavedWeatherFragment())
                    .addToBackStack(null)
                    .commit();

        }

    }
}
