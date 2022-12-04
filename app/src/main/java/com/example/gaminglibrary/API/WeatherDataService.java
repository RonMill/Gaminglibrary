package com.example.gaminglibrary.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataService {
    //String urlForCity = "https://api.openweathermap.org/geo/1.0/direct?q=schmittweiler,de&appid=ff76494d7fec7ca86a761ecc9dd6b12a";
    public static final String QUERY_FOR_COORDINATES = "https://api.openweathermap.org/geo/1.0/direct?q=";
    public static final String QUERY_FOR_WEATHER = "https://api.openweathermap.org/data/2.5/weather?lat=";
    public static final String COUNTRY_CODE = "de";
    public static final String APIKEY = "ff76494d7fec7ca86a761ecc9dd6b12a";
    public static final String UNITS = "&units=metric&lang=de";

    Context context;
    Coordinates coordinates = new Coordinates();

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListenerForCoordinates {
        void onError(String message);
        void onResponse(Coordinates coordinates);
    }

    public void getCoordinates(String cityName, VolleyResponseListenerForCoordinates volleyResponseListenerForCoordinates){
        String url = QUERY_FOR_COORDINATES + cityName+","+COUNTRY_CODE+"&appid="+APIKEY;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject coord = response.getJSONObject(0);
                    coordinates.setCityName(cityName);
                    coordinates.setLon(coord.getString("lon"));
                    coordinates.setLat(coord.getString("lat"));
                    //Toast.makeText(context,coordinates.toString(),Toast.LENGTH_SHORT).show();
                    volleyResponseListenerForCoordinates.onResponse(coordinates);
                } catch (JSONException e) {
                    e.printStackTrace();
                    volleyResponseListenerForCoordinates.onError("something went wrong to get the coordinates");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HS_KL", error.toString());
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface VolleyResponseListenerForWeaterReport {
        void onError(String message);
        void onResponse(WeatherReportModel weatherReportModel);
    }

    public void getWeatherReportByLonLat(VolleyResponseListenerForWeaterReport volleyResponseListenerForWeaterReport){
       String url = QUERY_FOR_WEATHER + coordinates.getLat()+"&lon="+coordinates.getLon()+"&appid="+APIKEY+UNITS;
        WeatherReportModel weatherReportModel = new WeatherReportModel();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weatherReportModel.setCoordinates(coordinates);
                try {
                    JSONArray weatherData = response.getJSONArray("weather");
                    JSONObject tempData = response.getJSONObject("main");
                    weatherReportModel.setDescription(weatherData.getJSONObject(0).getString("description"));
                    weatherReportModel.setTemperature(new Temperature(tempData.getString("temp"),tempData.getString("feels_like"),tempData.getString("temp_min"),tempData.getString("temp_max")));
                    volleyResponseListenerForWeaterReport.onResponse(weatherReportModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                    volleyResponseListenerForWeaterReport.onError("something went wrong to get the Weather Report");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HS_KL", error.toString());
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
