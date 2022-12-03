package com.example.gaminglibrary.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.gaminglibrary.activity.WeatherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataService {
    //String urlForCity = "https://api.openweathermap.org/geo/1.0/direct?q=schmittweiler,de&appid=ff76494d7fec7ca86a761ecc9dd6b12a";
    public static final String QUERY_FOR_COORDINATES = "https://api.openweathermap.org/geo/1.0/direct?q=";
    public static final String COUNTRY_CODE = "de";
    public static final String APIKEY = "ff76494d7fec7ca86a761ecc9dd6b12a";

    Context context;
    Coordinates coordinates = new Coordinates();

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);
        void onResponse(Coordinates coordinates);
    }

    public void getCoordinates(String cityName, VolleyResponseListener volleyResponseListener){
        String url = QUERY_FOR_COORDINATES + cityName+","+COUNTRY_CODE+"&appid="+APIKEY;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject coord = response.getJSONObject(0);
                    coordinates.setLon(coord.getString("lon"));
                    coordinates.setLat(coord.getString("lat"));
                    //Toast.makeText(context,coordinates.toString(),Toast.LENGTH_SHORT).show();
                    volleyResponseListener.onResponse(coordinates);
                } catch (JSONException e) {
                    e.printStackTrace();
                    volleyResponseListener.onError("Nope");
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

    //String urlForWeather = "https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=ff76494d7fec7ca86a761ecc9dd6b12a";

/*    public List<WeatherReportModel> getCityForecastById(String cityID) {

    }

    public List<WeatherReportModel> getCitxyForcastByID(String cityID) {

    }*/
}
