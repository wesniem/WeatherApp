package com.example.wesniemarcelin.chaseweatherappcodingchallenge.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;
import android.util.Log;

import com.example.wesniemarcelin.chaseweatherappcodingchallenge.view.WeatherView;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.WeatherResponse;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.presenter.network.WeatherService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.API_KEY;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.CITY_NAME;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.CURRENT_TEMP;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.ICON;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WEATHER_DESCRIPTION;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WEATHER_TYPE;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WIND_SPEED;


/**
 * Created by wesniemarcelin on 10/5/17.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SearchWeatherPresenter {
    List<WeatherResponse.Weather> mWeatherList;
    double currentTemp;

    WeatherView mWeatherView;
    ArrayMap<String, String> mWeather = new ArrayMap<>();

    public SearchWeatherPresenter(WeatherView weatherView){
        this.mWeatherView = weatherView;
    }

    public void fetchWeather(String cityName){
        //Retrofit Call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Service Call to retrieve response from Open Weather API
        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = weatherService.getWeather(cityName,API_KEY);

        //Asynchronous calls to determine whether there's a success or failure
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherResponse = response.body();
                mWeatherList = weatherResponse.getWeather();

                //url = "http://openweathermap.org/img/w/" + mWeatherList.get(0).getIcon() + ".png";
                mWeather.put(ICON, "http://openweathermap.org/img/w/" + mWeatherList.get(0).getIcon() + ".png");
                mWeather.put(WEATHER_TYPE, mWeatherList.get(0).getMain());
                //mainWeatherType = mWeatherList.get(0).getMain();
                mWeather.put(WEATHER_DESCRIPTION, mWeatherList.get(0).getDescription());
                //weatherDescription = mWeatherList.get(0).getDescription();

                mWeather.put(WIND_SPEED, Double.toString(weatherResponse.getWind().getSpeed()));
                mWeather.put(CITY_NAME, weatherResponse.getName());
                currentTemp = weatherResponse.getMain().getTemp();

                mWeather.put(CURRENT_TEMP, Double.toString(currentTemp));
                Log.d(CURRENT_TEMP,Double.toString(currentTemp));

                mWeatherView.showWeather(mWeather);


            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.d("MAYDAY", "Call has failed!");
            }
        });


    }
}
