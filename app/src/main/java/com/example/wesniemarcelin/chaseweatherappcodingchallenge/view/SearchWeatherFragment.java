package com.example.wesniemarcelin.chaseweatherappcodingchallenge.view;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wesniemarcelin.chaseweatherappcodingchallenge.R;
import com.example.wesniemarcelin.chaseweatherappcodingchallenge.presenter.SearchWeatherPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.CITY_NAME;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.CURRENT_TEMP;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.ICON;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.LAST_CITY_SEARCHED;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.NO_CITY_SEARCHED;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WEATHER_DESCRIPTION;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WEATHER_TYPE;
import static com.example.wesniemarcelin.chaseweatherappcodingchallenge.model.Constants.WIND_SPEED;

public class SearchWeatherFragment extends Fragment implements View.OnClickListener, WeatherView {
    public static final String MyPREFERENCES = "myprefs";
    //    Button submitCityBtn;
//    EditText cityEditText;
    String cityName;
    String getUserInput;
    Boolean savedInstanceStateExists;


    @BindView(R.id.submit_city)
    Button mSubmitCityBtn;
    @BindView(R.id.city_editText)
    EditText mCityEditText;
    SearchWeatherPresenter mSearchWeatherPresenter;
    String mCityName = "";
    SharedPreferences sharedpreferences;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mroot = inflater.inflate(R.layout.activity_search_weather_fragment, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ButterKnife.bind(this, mroot);
        mSubmitCityBtn.setOnClickListener(this);
        mSearchWeatherPresenter = new SearchWeatherPresenter(this);

        if (!sharedpreferences.getString(LAST_CITY_SEARCHED, NO_CITY_SEARCHED).equalsIgnoreCase(NO_CITY_SEARCHED)) {
            mSearchWeatherPresenter.fetchWeather(sharedpreferences.getString(LAST_CITY_SEARCHED, NO_CITY_SEARCHED));
        }
//        submitCityBtn = (Button) mroot.findViewById(R.id.submit_city);
//        cityEditText = (EditText) mroot.findViewById(R.id.city_editText);
//        viewIcon = (ImageView) mroot.findViewById(R.id.view_icon);
//        bundle = new Bundle();
//        savedInstanceStateExists = false;
//
//        if (savedInstanceState != null) {
//            savedInstanceStateExists = true;
//            getUserInput = savedInstanceState.getString("location");
//            if (!(getUserInput.isEmpty())) {
//                cityName = getUserInput;
//            } else {
//                savedInstanceStateExists = false;
//            }
//        }
//        Log.d("MY INSTANCE STATE", "The instance saved is " + getUserInput);

        return mroot;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        if (!mCityEditText.getText().toString().isEmpty()) {
            mCityName = mCityEditText.getText().toString();
            sharedpreferences.edit().putString(LAST_CITY_SEARCHED, mCityName).apply();

        } else {
            Toast.makeText(getContext(), "City hasn't been specified", Toast.LENGTH_LONG).show();
        }

        mSearchWeatherPresenter.fetchWeather(mCityName);
        Log.d("ONCLICK OF SEARCH WEATH", "onClick worked! for " + mCityName);
    }


    @Override
    public void showWeather(ArrayMap<String, String> weatherDetails) {
        Log.e(TAG, "Viewing Weather: ");
        Bundle bundle = new Bundle();
        Fragment newFragment = new ViewWeatherFragment();
        bundle.putString(ICON, weatherDetails.get(ICON));
        bundle.putString(WEATHER_DESCRIPTION, weatherDetails.get(WEATHER_DESCRIPTION));
        bundle.putString(WEATHER_TYPE, weatherDetails.get(WEATHER_TYPE));
        bundle.putString(WIND_SPEED, weatherDetails.get(WIND_SPEED));
        bundle.putString(CURRENT_TEMP, weatherDetails.get(CURRENT_TEMP));
        bundle.putString(CITY_NAME, weatherDetails.get(CITY_NAME));
        newFragment.setArguments(bundle);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_fragment, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        cityName = mCityEditText.getText().toString();
        outState.putString("location", cityName);
        Log.d("ON SAVED INSTANCE STATE", "onSaveInstanceState: " + cityName);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
