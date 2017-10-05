package com.example.wesniemarcelin.chaseweatherappcodingchallenge.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.example.wesniemarcelin.chaseweatherappcodingchallenge.R;
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

public class SearchWeatherFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "YOOOO";
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "1eb0319a72231ce4d37d1da905ccbec2";
    public static final String MyPREFERENCES = "myprefs";
    SharedPreferences sharedpreferences;
    List<Weather_> mWeatherList;
    Bundle bundle;
    Float mWindSpeed;
    Float currentTemp;
    Button submitCityBtn;
    ImageView viewIcon;
    EditText cityEditText;
    String cityName;
    String weatherDescription;
    String mainWeatherType;
    String url;
    String getUserInput;
    Boolean savedInstanceStateExists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mroot = inflater.inflate(R.layout.activity_search_weather_fragment, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        submitCityBtn = (Button) mroot.findViewById(R.id.submit_city);
        cityEditText = (EditText) mroot.findViewById(R.id.city_editText);
        viewIcon = (ImageView) mroot.findViewById(R.id.view_icon);
        bundle = new Bundle();
        savedInstanceStateExists = false;
        submitCityBtn.setOnClickListener(this);

        if (savedInstanceState != null) {
            savedInstanceStateExists = true;
            getUserInput = savedInstanceState.getString("location");
            if (!(getUserInput.isEmpty())) {
                cityName = getUserInput;
            } else {
                savedInstanceStateExists = false;
            }
        }
//        Log.d("MY INSTANCE STATE", "The instance saved is " + getUserInput);

        return mroot;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_city:
                if (savedInstanceStateExists) {
                    Log.d(TAG, "onClick worked! for " + cityName);
                    sharedpreferences.edit().putString("cityName", cityName).apply();
                    bundle.putString("location", cityName);
                    fetchWeather();
                    break;
                }
                Toast.makeText(getContext(), "Please enter a valid input", Toast.LENGTH_LONG).show();
                cityName = cityEditText.getText().toString();


        }
    }


    private void fetchWeather() {
        //Retrofit Call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Service Call to retrieve response from OpenWeather API
        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<Weather> call = weatherService.getWeather(cityName, API_KEY);
        final Call<Wind> windCall = weatherService.getWind(cityName, API_KEY);
        final Call<Main> mainCall = weatherService.getMainConditions(cityName, API_KEY);


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

                downloadIconImage(url);
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


                        viewWeather();

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

    private void downloadIconImage(String url) {
        Picasso.with(getContext())
                .load(url)
                .into(viewIcon);
    }

    private void viewWeather() {
        // Create new fragment and transaction to allow user to view the weather for the location provided
        Log.e(TAG, "Viewing Weather: ");
        Fragment newFragment = new ViewWeatherFragment();
        bundle.putString("icon", url);
        bundle.putString("description", weatherDescription);
        bundle.putString("main", mainWeatherType);
        bundle.putFloat("windSpeed", mWindSpeed);
        bundle.putFloat("currentTemp", currentTemp);
        newFragment.setArguments(bundle);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_fragment, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        cityName = cityEditText.getText().toString();
        outState.putString("location", cityName);
        Log.d("ON SAVED INSTANCE STATE", "onSaveInstanceState: " + cityName);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
