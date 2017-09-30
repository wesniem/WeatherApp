package com.example.wesniemarcelin.chaseweatherappcodingchallenge;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainWeatherActivity extends AppCompatActivity {
    Button submitCityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.activity_main_fragment, new SearchWeatherFragment())
                    .commit();

//        submitCityButton = (Button) findViewById(R.id.submit_city);
//        submitCityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onSubmitCity();
//            }
//        });


//        if(savedInstanceState == null){
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .add(R.id.activity_main_fragment, new SearchWeatherFragment())
//                    .commit();
//        }
    }

    public void onSubmitCity() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.activity_main_fragment, new SearchWeatherFragment())
                .commit();
    }

    public void onSubmitCity(View view) {
    }
}
