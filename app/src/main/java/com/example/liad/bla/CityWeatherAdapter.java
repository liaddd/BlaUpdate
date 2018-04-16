package com.example.liad.bla;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.MyViewHolder> {

    private final WeatherFragment weatherFragment;
    private Context context;
    private ArrayList<City> cities;


    public CityWeatherAdapter(Context context, WeatherFragment weatherFragment, ArrayList<City> cities) {
        this.context = context;
        this.cities = cities;
        this.weatherFragment = weatherFragment;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView cityTitleTV , cityTempTV;
        private ImageView cityIV;
        private RelativeLayout relativeLayout;


        public MyViewHolder(View view) {
            super(view);
            cityTitleTV = view.findViewById(R.id.city_item_title);
            cityTempTV = view.findViewById(R.id.city_item_temp);
            cityIV = view.findViewById(R.id.city_image_indicator);
            relativeLayout = view.findViewById(R.id.city_item_layout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final City city = cities.get(position);

        holder.cityTitleTV.setText(city.getName());
        if (city.getTemp() != null) {
            holder.cityTempTV.setVisibility(View.VISIBLE);
            int tempAsInt = Integer.parseInt(city.getTemp());

            if (tempAsInt > 20) {
                holder.cityTempTV.setTextColor(Color.RED);
            } else if (tempAsInt < 0) {
                holder.cityTempTV.setTextColor(Color.BLUE);
            }else{
                holder.cityTempTV.setTextColor(Color.GREEN);
            }
        }
        holder.cityTempTV.setText(city.getTemp());
        holder.cityIV.setImageResource(city.getDescImage());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherFragment.getCountryData(city);
            }
        });

    }


    @Override
    public int getItemCount() {
        return cities.size();
    }


}


