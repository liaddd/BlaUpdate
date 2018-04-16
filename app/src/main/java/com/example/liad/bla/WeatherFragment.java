package com.example.liad.bla;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherFragment extends Fragment {

    private String APP_ID = "d31cd0625650822fda879a128a362060";
    private MyArrayList<City> cities;
    private RecyclerView recyclerView1;
    private CityWeatherAdapter adapter;
    private String[] citiesNames = new String[]
            {"London",
                    "State of Israel", "Dutch Harbor",
                    "Kathmandu", "Laspi", "Merida", "Alupka",
                    "Bee House", "Morden", "Nasirotu"};
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        SnapHelperOneByOne snapHelperOneByOne = new SnapHelperOneByOne();

        cities = new MyArrayList<>();
        recyclerView1 = view.findViewById(R.id.weather_recycler_view);
        progressBar = view.findViewById(R.id.city_item_progress_bar);

        getCities();
        adapter = new CityWeatherAdapter(getActivity(), this, cities);

        snapHelperOneByOne.attachToRecyclerView(recyclerView1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        recyclerView1.setAdapter(adapter);

        return view;
    }

    public void getCountryData(final City city) {
        progressBar.setVisibility(View.VISIBLE);
        new HttpRequest.Builder(getActivity())
                .setHttpMethod(HttpMethod.GET)
                .setParams("q=" + city.getName())
                .setParams("APPID=" + APP_ID)
                .start(new OnServerResponseListener() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        progressBar.setVisibility(View.GONE);
                        JSONObject obj;
                        String description =  null;
                        try {
                            obj = data.getJSONObject("main");

                            float kelvin = Float.parseFloat(obj.getString("temp").toString());
                            float celsius = kelvin - 273.15F;

                            JSONArray arr = data.getJSONArray("weather");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj2 = arr.getJSONObject(i);
                                description = obj2.getString("main");
                            }
                            city.setDescription(description);
                            city.setTemp((int)celsius + "");
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("onSuccess", data.toString());
                    }

                    @Override
                    public void onFailure(JSONObject error) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("onFailure", error.toString());
                    }
                });
    }

    private void getCities() {

        for (int i = 0; i < citiesNames.length; i++) {
            cities.add(new City(citiesNames[i], null, ""));
        }
    }


    class SnapHelperOneByOne extends LinearSnapHelper {

        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {

            if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
                return RecyclerView.NO_POSITION;
            }

            final View currentView = findSnapView(layoutManager);

            if (currentView == null) {
                return RecyclerView.NO_POSITION;
            }

            final int currentPosition = layoutManager.getPosition(currentView);

            if (currentPosition == RecyclerView.NO_POSITION) {
                return RecyclerView.NO_POSITION;
            }

            return currentPosition;
        }
    }
}
