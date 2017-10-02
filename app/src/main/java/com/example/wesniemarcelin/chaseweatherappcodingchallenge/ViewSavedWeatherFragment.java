package com.example.wesniemarcelin.chaseweatherappcodingchallenge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Main;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Weather;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Weather_;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Wind;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.network.WeatherService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wesniemarcelin on 10/2/17.
 */

public class ViewSavedWeatherFragment extends Fragment {
    ImageView savedWeatherIcon;
    TextView savedWeatherCity;
    TextView savedWeatherDescription;
    SharedPreferences mySharedPrefs;
    private static String TAG = "YOOOO";
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "1eb0319a72231ce4d37d1da905ccbec2";
    String savedCityName;
    List<Weather_> mWeatherList;
    Float mWindSpeed;
    Float currentTemp;
    String weatherDescription;
    String mainWeatherType;
    String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mSavedWeatherRoot = inflater.inflate(R.layout.view_saved_weather_fragment, container, false);
        savedWeatherIcon = (ImageView) mSavedWeatherRoot.findViewById(R.id.saved_weather_icon);
        savedWeatherCity = (TextView) mSavedWeatherRoot.findViewById(R.id.saved_weather_city);
        savedWeatherDescription = (TextView) mSavedWeatherRoot.findViewById(R.id.saved_weather_temp);
        mySharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        savedCityName = mySharedPrefs.getString("cityName", "defaultStringIfNothingFound");
        findCurrentSavedWeather();
        return mSavedWeatherRoot;
    }

    private void findCurrentSavedWeather() {
        Log.d("FIND SAVED WEATHER", "SAVED LOCATION: " + mySharedPrefs.getString("cityName1", "defaultStringIfNothingFound"));

        //Retrofit Call
        //Retrofit Call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Service Call
        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<Weather> call = weatherService.getWeather(savedCityName, API_KEY);
        final Call<Wind> windCall = weatherService.getWind(savedCityName, API_KEY);
        final Call<Main> mainCall = weatherService.getMainConditions(savedCityName, API_KEY);


        //Asynchronous Calls to determine success or failure
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Log.d(TAG, "Success" + response.body());

                Weather weather = response.body();
                mWeatherList = weather.getWeather();

                Log.d("POJO", "Pojo " + mWeatherList.get(0).getDescription());
                Log.d("POJO", "Pojo " + mWeatherList.get(0).getIcon());
                Log.d("POJO", "Pojo " + mWeatherList.get(0).getMain());

                url = "http://openweathermap.org/img/w/" + mWeatherList.get(0).getIcon() + ".png";
                mainWeatherType = mWeatherList.get(0).getMain();
                weatherDescription = mWeatherList.get(0).getDescription();

                displayCurrentSavedWeather(url);
//                viewIcon.setImageURI(imageUri);

                Log.d(TAG, "Success, in thereeeee" + API_KEY);

                windCall.enqueue(new Callback<Wind>() {
                    @Override
                    public void onResponse(Call<Wind> call, Response<Wind> response) {
                        Log.d("WIND CALL", "In the wind" + response.body());

                        Wind wind = response.body();
                        mWindSpeed = wind.getSpeed();

                        Log.d("Wind is", "Pojo " + mWindSpeed);
                    }

                    @Override
                    public void onFailure(Call<Wind> call, Throwable t) {
                        Log.d("MAYDAY FOR WIND CALL!", t.getMessage());

                    }
                });


                mainCall.enqueue(new Callback<Main>() {
                    @Override
                    public void onResponse(Call<Main> call, Response<Main> response) {
                        Log.d("MAIN CALL", "in Main Call.... " + response.body());
                        Main main = response.body();
                        currentTemp = main.getTemp();
                        String currentTempString = currentTemp.toString();
                        Log.d("Current Temp is", "Pojo " + currentTemp);

                    }

                    @Override
                    public void onFailure(Call<Main> call, Throwable t) {
                        Log.d("MAYDAY FOR MAIN CALL!", t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.d("MAYDAY!", t.getMessage());
            }
        });
    }

    private void displayCurrentSavedWeather(String url) {
        Picasso.with(getContext())
                .load(url)
                .into(savedWeatherIcon);

        savedWeatherCity.setText(savedCityName);
        savedWeatherDescription.setText(weatherDescription);
    }
}
