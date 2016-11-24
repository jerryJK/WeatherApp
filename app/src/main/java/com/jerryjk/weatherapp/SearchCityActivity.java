package com.jerryjk.weatherapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

public class SearchCityActivity extends AppCompatActivity {
    private String cityNameURL;
    private String cityName;

    private String citPrefs;
    private EditText locationEditText;
    private TextView defaultLocalization;
    private SharedPreference sharedPreference;
    Activity context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreference = new SharedPreference();

        String sharedCity = sharedPreference.getCityPrefs(context);

        if (sharedCity != null) {

            Button buttonCity = (Button) findViewById(R.id.buttonDefault);
            ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
            TextView textCity = (TextView) findViewById(R.id.textViewDefaultLocalization);

            textCity.setText("");
            buttonCity.setText(sharedCity);
            buttonCity.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationEditText =
                        (EditText) findViewById(R.id.locationEditText);
                cityName = locationEditText.getText().toString();

                cityNameURL = createCityUrl(locationEditText.getText().toString());


                if (cityNameURL != null) {
                    dismissKeyboard(locationEditText);
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra(MainActivity.CITY_NAME_URL, cityNameURL);
                    i.putExtra(MainActivity.CITY_NAME, cityName);
                    startActivity(i);
                    finish();
                }
            }


        });


        FloatingActionButton fabDefault = (FloatingActionButton) findViewById(R.id.fabDefault);
        fabDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationEditText =
                        (EditText) findViewById(R.id.locationEditText);
                citPrefs = locationEditText.getText().toString();

                sharedPreference.saveCityPrefs(context, citPrefs);

                defaultLocalization = (TextView) findViewById(R.id.textViewDefaultLocalization);
                defaultLocalization.setText("");

                Button localizationButton = (Button) findViewById(R.id.buttonDefault);
                localizationButton.setText(locationEditText.getText().toString());
                localizationButton.setVisibility(View.VISIBLE);

                ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
                deleteButton.setVisibility(View.VISIBLE);


                dismissKeyboard(locationEditText);

                Toast.makeText(context, "Zapisano domyślną lokalizację",
                        Toast.LENGTH_LONG).show();


            }

        });


        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog2();
            }

        });


    }

    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String createCityUrl(String city) {
        try {
            String urlCity = URLEncoder.encode(city, "UTF-8");  return new String(urlCity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showDialog2() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Czy chcesz usunąć domyślną lokalizację?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Button localizationButton = (Button) findViewById(R.id.buttonDefault);
                        sharedPreference.clearCityPrefs(context);

                        localizationButton.setVisibility(View.INVISIBLE);
                        localizationButton.setText("");

                        ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteButton);
                        deleteButton.setVisibility(View.INVISIBLE);


                        Toast.makeText(context, "Usunięto domyślną lokalizację",
                                Toast.LENGTH_LONG).show();


                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });

        dialog.show();


    }

}
