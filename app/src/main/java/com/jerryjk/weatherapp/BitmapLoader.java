package com.jerryjk.weatherapp;

/**
 * Created by jarek on 2016-11-11.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  LoadBitmapFromURL for Loading BitMap using AsyncTask
 */
public class BitmapLoader extends AsyncTask<String, Void, Bitmap> {


    private ImageView imageView;

    public BitmapLoader(ImageView imageView) {

        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        try {
            // download bitmap
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap myBitmap) {
        //set image Bitmap
        imageView.setImageBitmap(myBitmap);

    }

}