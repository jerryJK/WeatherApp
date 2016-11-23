package com.jerryjk.weatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String CITY_NAME = "cityName";
    public static final String CITY_NAME_URL = "cityNameURL";

    private ViewPager mViewPager;
    private RecyclerView mRVFishPrice;
    private WeatherAdapter mAdapter;

    private SharedPreference sharedPreference;
    Activity context = this;

    private String prefsCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Intent intent = getIntent();
        String cityNameURL = intent.getStringExtra(CITY_NAME_URL);

        sharedPreference = new SharedPreference();
        prefsCity = sharedPreference.getCityPrefs(context);
        String prefsCityURL = createCityUrl(prefsCity);


        //start WebServiceTask
        WebServiceTask start = new WebServiceTask();




         if (prefsCity == null & cityNameURL == null) {

             Intent intent2 = new Intent(MainActivity.this, SearchCityActivity.class);
             startActivity(intent2);


          } else if (prefsCity != null & cityNameURL == null) {


            start.execute("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + prefsCityURL +
             ",pl&mode=json&units=metric&lang=pl&cnt=10&APPID=" + getString(R.string.APPID));
           }

            else if (cityNameURL != null) {
             start.execute("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityNameURL +
                     ",pl&mode=json&units=metric&lang=pl&cnt=10&APPID=" + getString(R.string.APPID));
           }


        //check internet connection
        if (internetConnectionCheck(MainActivity.this) ==false) {
            Toast.makeText(getApplicationContext(), "Upss..brak połączenia z internetem", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchCityActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment1DayWeather(), "DZISIAJ");
        adapter.addFragment(new Fragment10DaysWeather(), "10 DNI");
        //adapter.addFragment(new FragmentFavorites(), "ULUBIONE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    /**
     *  WeatherServiceTask for Getting Data From Web Service and Setting Views with Data
     */
    private class WebServiceTask extends AsyncTask<String, Void, String> {


        //new Progress Dialog
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {

            // show Progrees Dialog

            dialog.setTitle("Ładowanie danych pogodowych ..."); //set Title
            dialog.setMessage("Czekaj ...");  // set Message
            dialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {


            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();

                // download data to InputStream
                InputStream in = new BufferedInputStream(
                        connection.getInputStream());
                return streamToString(in);

            } catch (Exception e) {
                // handle exception
                Log.d(MainActivity.class.getSimpleName(), e.toString());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {

            List<Weather> data = new ArrayList<>();

            // dismiss Progress Dialog
            dialog.dismiss();


            try {
                // get JSON object
                JSONObject json = new JSONObject(result);

                JSONObject city = json.getJSONObject("city");
                JSONArray list = json.getJSONArray("list");
                String cityName = city.getString("name");

                for (int i = 0; i < list.length(); ++i) {
                    JSONObject day = list.getJSONObject(i); // odczytaj dane dotyczące jednego dnia

                    // odczytaj z obiektu JSONObject dane dotyczące temperatury danego dnia ("temp")
                    JSONObject temperatures = day.getJSONObject("temp");

                    // odczytaj z obiektu JSONObject opis i ikonę "weather"
                    JSONObject weather =
                            day.getJSONArray("weather").getJSONObject(0);

                    // dodaj nowy obiekt Weather do listy weatherList
                    data.add(new Weather(
                            city.getString("name"),
                            day.getLong("dt"),
                            temperatures.getDouble("day"),
                            weather.getString("description"), // warunki pogodowe
                            weather.getString("icon"), // nazwa ikony
                            temperatures.getDouble("min"), // temperatura minimalna
                            temperatures.getDouble("max"), // temperatura maksymalna
                            temperatures.getDouble("night"), // temperatura maksymalna
                            day.getDouble("pressure")));


                }

                // new Weather object
                Weather weather1 = data.get(0);

                // set Views with Weather data
                ImageView bitViev1 = (ImageView) findViewById(R.id.weather_image);
                bitViev1.setImageResource(weather1.getImgResource());
               // BitmapLoader loader = new BitmapLoader(bitViev1);
               // loader.execute(weather1.getBitmapUrl());



                TextView cName = (TextView) findViewById(R.id.city_name);
                TextView tDay = (TextView) findViewById(R.id.tempDay);
                TextView wDescription = (TextView) findViewById(R.id.weather_description);
                TextView wDt = (TextView) findViewById(R.id.weather_dt);
                TextView tMin = (TextView) findViewById(R.id.temp_min);
                TextView tMax = (TextView) findViewById(R.id.temp_max);
                TextView tNight = (TextView) findViewById(R.id.temp_night);
                TextView dPressure = (TextView) findViewById(R.id.pressure);

                if (weather1.getCityName().equals("Śródmieście")){
                    cName.setText("Kraków");
                }else
                    cName.setText(weather1.getCityName());

                tDay.setText(weather1.getWeatherTemp());
                wDescription.setText(weather1.getWeatherDescription());
                wDt.setText(weather1.getWeatherDt());
                tMin.setText("Dzień Min  " + weather1.getTempMin());
                tMax.setText("Dzień Max  " + weather1.getTempMax());
                tNight.setText("Noc  " + weather1.getTempNight());
                dPressure.setText("Ciśnienie  " + weather1.getPressure());

                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.weatherList);
                mAdapter = new WeatherAdapter(MainActivity.this, data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainActivity.this));



                sharedPreference = new SharedPreference();
                prefsCity = sharedPreference.getCityPrefs(context);



            } catch (Exception e) {
                // handle exception
                Log.d(MainActivity.class.getSimpleName(), e.toString());
            }
        }
    }



    /**
     * convert Stream to String
     */
    public static String streamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        try {

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            reader.close();

        } catch (IOException e) {
            // handle exception
            Log.d(MainActivity.class.getSimpleName(), e.toString());
        }

        return stringBuilder.toString();
    }




    public static boolean internetConnectionCheck(Activity CurrentActivity) {
        Boolean Connected = false;
        ConnectivityManager connectivity = (ConnectivityManager) CurrentActivity.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) for (int i = 0; i < info.length; i++)
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    Log.e("My Network is: ", "Connected ");
                    Connected = true;
                } else {}

        } else {
            Log.e("My Network is: ", "Not Connected");

            Toast.makeText(CurrentActivity.getApplicationContext(),
                    "Please Check Your internet connection",
                    Toast.LENGTH_LONG).show();
            Connected = false;

        }
        return Connected;

    }


    private void showDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Wybór lokalizacji")
                .setMessage("W celu wyświetlenia prognozy pogody konieczne jest ustawienie lokalizacji")
                .setPositiveButton("Wybierz lokalizację", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent intent = new Intent(MainActivity.this, SearchCityActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Zamknij aplikajcę", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });

        dialog.show();


    }

    private String createCityUrl(String city) {
        try {
            String urlCity = URLEncoder.encode(city, "UTF-8");  return new String(urlCity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}







