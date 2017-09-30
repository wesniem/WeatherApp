package com.example.wesniemarcelin.chaseweatherappcodingchallenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Weather;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Weather_;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.network.WeatherService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchWeatherFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "YOOOO";
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "1eb0319a72231ce4d37d1da905ccbec2";
    public static final String MyPREFERENCES = "myprefs";
    SharedPreferences sharedpreferences;
    List<Weather_> mWeatherList;
    Button submitCityBtn;
    ImageView viewIcon;
    EditText cityEditText;
    String cityName;
    String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mroot = inflater.inflate(R.layout.activity_search_weather_fragment, container, false);
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        submitCityBtn = (Button) mroot.findViewById(R.id.submit_city);
        cityEditText = (EditText) mroot.findViewById(R.id.city_editText);
        viewIcon = (ImageView) mroot.findViewById(R.id.view_icon);
        submitCityBtn.setOnClickListener(this);
        return mroot;
    }

    private void fetchWeather() {
        //Retrofit Call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Service Call
        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<Weather> call = weatherService.getWeather(cityName, API_KEY);

        //Asynchronous Call to determine success or failure

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Log.d(TAG, "Success" + response.body());

                Weather weather = response.body();
                mWeatherList = weather.getWeather();

                Log.d("POJO", "Pojo " + mWeatherList.get(0).getDescription());
                Log.d("POJO", "Pojo " + mWeatherList.get(0).getIcon());

                url = "http://openweathermap.org/img/w/" + mWeatherList.get(0).getIcon() + ".png";
                downloadIconImage(url);
//                viewIcon.setImageURI(imageUri);



                Log.d(TAG, "Success, in thereeeee" + API_KEY);
                viewWeather();
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.d("MAYDAY!", t.getMessage());
            }
        });

    }

    private void downloadIconImage(String url) {
        Picasso.with(getContext())
                .load(url)
                .into(viewIcon);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_city:
                cityName = cityEditText.getText().toString();
                Log.d(TAG, "onClick worked! for " + cityName);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("cityName", cityName);
                editor.commit();
                fetchWeather();
                break;

//                fetchWeather();
        }
    }

    private void viewWeather() {
        // Create new fragment and transaction
        Log.e(TAG, "viewWeather: " );
        Fragment newFragment = new ViewWeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("icon", url);
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.activity_main_fragment, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }
}
