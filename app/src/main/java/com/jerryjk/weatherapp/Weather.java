package com.jerryjk.weatherapp;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by jarek on 2016-11-09.
 */
public class Weather {

    private String cityName;
    private String weatherDt;
    private String tempDay;
    private String weatherDescription;
    private String weatherIcon;
    //private String bitmapUrl;
    private String tempMin;
    private String tempMax;
    private String tempNight;
    private String pressure;
    private int imgResource;


    public Weather ( String cityName, long weatherDt, double tempDay, String weatherDescription,
                     String weatherIcon, double tempMin, double tempMax, double tempNight,
                     double pressure) {
        // convert temperature double to integer
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        this.cityName = cityName;
        this.weatherDt = convertTime(weatherDt);
        this.tempDay = numberFormat.format(tempDay) + " "+  "\u00B0C";
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
        //this.bitmapUrl = "http://openweathermap.org/img/w/" + weatherIcon + ".png";
        this.tempMin = numberFormat.format(tempMin) + " "+  "\u00B0C";
        this.tempMax = numberFormat.format(tempMax) + " "+  "\u00B0C";
        this.tempNight = numberFormat.format(tempNight) + " "+  "\u00B0C";
        this.pressure = numberFormat.format(pressure) + " "+  "Pa";

        if(weatherIcon.equals("01d")) {
            this.imgResource = R.drawable.icon01d;
        }
        else if(weatherIcon.equals("01n")) {
            this.imgResource = R.drawable.icon01n;
        }

        else if(weatherIcon.equals("02d")) {
            this.imgResource = R.drawable.icon02d;
        }
        else if(weatherIcon.equals("02n")) {
            this.imgResource = R.drawable.icon02n;
        }
        else if(weatherIcon.equals("03d")) {
            this.imgResource = R.drawable.icon03d;
        }
        else if(weatherIcon.equals("03n")) {
            this.imgResource = R.drawable.icon03n;
        }
        else if(weatherIcon.equals("04d")) {
            this.imgResource = R.drawable.icon04d;
        }
        else if(weatherIcon.equals("04n")) {
            this.imgResource = R.drawable.icon04n;
        }
        else if(weatherIcon.equals("09d")) {
            this.imgResource = R.drawable.icon09d;
        }
        else if(weatherIcon.equals("09n")) {
            this.imgResource = R.drawable.icon09n;
        }
        else if(weatherIcon.equals("10d")) {
            this.imgResource = R.drawable.icon10d;
        }
        else if(weatherIcon.equals("10n")) {
            this.imgResource = R.drawable.icon10n;
        }
        else if(weatherIcon.equals("11d")) {
            this.imgResource = R.drawable.icon11d;
        }
        else if(weatherIcon.equals("11n")) {
            this.imgResource = R.drawable.icon11n;
        }
        else if(weatherIcon.equals("13d")) {
            this.imgResource = R.drawable.icon13d;
        }
        else if(weatherIcon.equals("13n")) {
            this.imgResource = R.drawable.icon13n;
        }
        else if(weatherIcon.equals("50d")) {
            this.imgResource = R.drawable.icon50d;
        }
        else if(weatherIcon.equals("50n")) {
            this.imgResource = R.drawable.icon50n;
        }

    }


    public int getImgResource() {return imgResource;}

    public String getCityName() {
        return cityName;}

    public String getWeatherDt() {
        return weatherDt;}

    public String getWeatherTemp() {
        return tempDay;}

    public String getWeatherDescription() {
        return weatherDescription;}

    public String getWeatherIcon() {
        return weatherIcon;}

    //public String getBitmapUrl() {
      //  return bitmapUrl;}

    public String getTempMin() {
        return tempMin;}

    public String getTempMax() {
        return tempMax;}

    public String getTempNight() {
        return tempNight;}

    public String getPressure() {
        return pressure;}



    // convert Dt to day name
    private static String convertTime(long timeDt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeDt * 1000);
        TimeZone tz = TimeZone.getDefault();

        // adjust time to mobile time zone
        calendar.add(Calendar.MILLISECOND,
                tz.getOffset(calendar.getTimeInMillis()));

        // return day name
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, d MMM, yyyy");
        return dateFormatter.format(calendar.getTime());
    }

}
