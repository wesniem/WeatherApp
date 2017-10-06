package com.example.wesniemarcelin.chaseweatherappcodingchallenge.view;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wesniemarcelin.chaseweatherappcodingchallenge.R;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.presenter.SearchWeatherPresenter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.CITY_NAME;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.CURRENT_TEMP;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.ICON;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.LAST_CITY_SEARCHED;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.NO_CITY_SEARCHED;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WEATHER_DESCRIPTION;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WEATHER_TYPE;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WIND_SPEED;

/**
 * Created by wesniemarcelin on 10/2/17.
 */

public class ViewSavedWeatherFragment extends Fragment implements WeatherView {
    @BindView(R.id.saved_weather_icon)
    ImageView savedWeatherIcon;
    @BindView(R.id.saved_weather_city)
    TextView savedWeatherCity;
    @BindView(R.id.saved_weather_temp)
    TextView savedWeatherTemp;
    @BindView(R.id.saved_weather_description)
    TextView savedWeatherDescription;
    @BindView(R.id.saved_wind)
    TextView savedWind;
    SharedPreferences mySharedPrefs;

    String savedCityName;
    SearchWeatherPresenter mSearchWeatherPresenter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mSavedWeatherRoot = inflater.inflate(R.layout.view_saved_weather_fragment, container, false);
        ButterKnife.bind(this, mSavedWeatherRoot);
        mySharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSearchWeatherPresenter = new SearchWeatherPresenter(this);
        savedCityName = mySharedPrefs.getString("cityName", "defaultStringIfNothingFound");
        mSearchWeatherPresenter.fetchWeather(mySharedPrefs.getString(LAST_CITY_SEARCHED, NO_CITY_SEARCHED));
        if (!mySharedPrefs.getString(LAST_CITY_SEARCHED, NO_CITY_SEARCHED).equalsIgnoreCase(NO_CITY_SEARCHED)) {
            mSearchWeatherPresenter.fetchWeather(mySharedPrefs.getString(LAST_CITY_SEARCHED, NO_CITY_SEARCHED));
        }
        return mSavedWeatherRoot;
    }

    @Override
    public void showWeather(ArrayMap<String, String> weatherDetails) {
        Bundle bundle = new Bundle();
        Fragment newFragment = new ViewWeatherFragment();
        bundle.putString(ICON, weatherDetails.get(ICON));
        bundle.putString(WEATHER_DESCRIPTION, weatherDetails.get(WEATHER_DESCRIPTION));
        bundle.putString(WEATHER_TYPE, weatherDetails.get(WEATHER_TYPE));
        bundle.putString(WIND_SPEED, weatherDetails.get(WIND_SPEED));
        bundle.putString(CURRENT_TEMP, weatherDetails.get(CURRENT_TEMP));
        bundle.putString(CITY_NAME, weatherDetails.get(CITY_NAME));
        newFragment.setArguments(bundle);

        Picasso.with(getContext())
                .load(bundle.getString(ICON))
                .into(savedWeatherIcon);

        savedWeatherCity.setText(getString(R.string.city_name,mySharedPrefs.getString(LAST_CITY_SEARCHED, NO_CITY_SEARCHED)));
        savedWeatherTemp.setText(getString(R.string.temperature, bundle.getString(CURRENT_TEMP)));
        savedWeatherDescription.setText(getString(R.string.weather, bundle.getString(WEATHER_DESCRIPTION)));
        savedWind.setText(getString(R.string.wind, bundle.getString(WIND_SPEED)));
    }
}
