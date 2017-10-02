package com.example.wesniemarcelin.chaseweatherappcodingchallenge.network;

import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Main;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Weather;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Wind;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by wesniemarcelin on 9/29/17.
 */

public interface WeatherService {
    @GET("weather")
    Call<Weather> getWeather(@Query("q") String city, @Query("APPID") String apiKey);
    @GET("weather")
    Call<Wind> getWind(@Query("q") String city, @Query("APPID") String apiKey);
    @GET("weather")
    Call<Main> getMainConditions(@Query("q") String city, @Query("APPID") String apiKey);
}
