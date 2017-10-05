package com.example.wesniemarcelin.chaseweatherappcodingchallenge.model;

import java.util.List;

/**
 * Created by wesniemarcelin on 10/5/17.
 */

public class WeatherResponse {
    List<Weather> weather;
    Main main;
    Wind wind;
    long id;
    String name;
    int cod;

    public class Weather{
        int id;
        String main;
        String description;
        String icon;

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public class Main{
        Double temp;
        Double pressure;
        Double humidity;
        Double temp_min;
        Double temp_max;

        public Double getTemp() {
            return temp;
        }

        public Double getPressure() {
            return pressure;
        }

        public Double getHumidity() {
            return humidity;
        }

        public Double getTemp_min() {
            return temp_min;
        }

        public Double getTemp_max() {
            return temp_max;
        }
    }
    public class Wind{
        Double speed;

        public Double getSpeed() {
            return speed;
        }
    }
    public List<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }
}
