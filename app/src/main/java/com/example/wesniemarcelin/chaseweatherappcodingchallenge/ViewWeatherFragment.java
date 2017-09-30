package com.example.wesniemarcelin.chaseweatherappcodingchallenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by wesniemarcelin on 9/30/17.
 */

public class ViewWeatherFragment extends Fragment {
    ImageView weatherIcon;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mWeatherViewroot = inflater.inflate(R.layout.view_weather_fragment,container,false);
        Log.d("VIEW WEATHERFRAG", "onCreateView: ");
        weatherIcon = (ImageView) mWeatherViewroot.findViewById(R.id.icon_image);
        Bundle bundle = this.getArguments();
        String myIconUrl = bundle.getString("icon");
        Log.d("URLL TO BE DISPLAYED", "this " + myIconUrl);
        Picasso.with(getContext())
                .load(myIconUrl)
                .into(weatherIcon);
        return mWeatherViewroot;
    }
}
