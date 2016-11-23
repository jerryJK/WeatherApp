package com.jerryjk.weatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jarek on 2016-11-15.
 */
public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Weather> data;
    Weather current;
    int currentPos = 0;

    // przechowuje pobrane bitmapy, umożliwiając ich ponowne użycie
    private Map<String, Bitmap> bitmaps = new HashMap<>();

    // create constructor to innitilize context and data sent from MainActivity
    public WeatherAdapter(Context context, List<Weather> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_weather, parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Weather current = data.get(position);


        myHolder.dt.setText(current.getWeatherDt());
        myHolder.desc.setText(current.getWeatherDescription());
        myHolder.day.setText(current.getWeatherTemp());
        myHolder.day.setText("Dzień " + current.getWeatherTemp());
        myHolder.min.setText("Min " + current.getTempMin());
        myHolder.max.setText("Max" + current.getTempMax());
        myHolder.night.setText("Noc " + current.getTempNight());
        myHolder.pres.setText("Ciśnienie " + current.getPressure());

        myHolder.im.setImageResource(current.getImgResource());
       // new BitmapLoader(myHolder.im).execute(
          //      current.getBitmapUrl());

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{


        TextView dt;
        TextView day;
        TextView desc;
        TextView min;
        TextView max;
        TextView night;
        TextView pres;
        ImageView im;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            dt = (TextView) itemView.findViewById(R.id.dt);
            desc = (TextView) itemView.findViewById(R.id.desc);
            day = (TextView) itemView.findViewById(R.id.day);
            min = (TextView) itemView.findViewById(R.id.min);
            max = (TextView) itemView.findViewById(R.id.max);
            night = (TextView) itemView.findViewById(R.id.night);
            pres = (TextView) itemView.findViewById(R.id.pres);
            im = (ImageView) itemView.findViewById(R.id.im);

        }

    }

}