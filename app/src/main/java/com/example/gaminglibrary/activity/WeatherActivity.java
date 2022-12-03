package com.example.gaminglibrary.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gaminglibrary.API.Coordinates;
import com.example.gaminglibrary.API.MySingleton;
import com.example.gaminglibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    ////API WAS GEHT ALLA
    Button btn_getWeather;
    EditText et_dataInput;
    ListView lv_WeatherReport;
    ////API WAS GEHT ALLA

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ////API WAS GEHT ALLA
        btn_getWeather = (Button) findViewById(R.id.GET_WEATHERBUTTON);
        et_dataInput = (EditText) findViewById(R.id.EDITTEXTLOCATION);
        lv_WeatherReport = (ListView) findViewById(R.id.WeatherReport);

        btn_getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //RequestQueue queue = Volley.newRequestQueue(WeatherActivity.this);

                String urlForCity = "https://api.openweathermap.org/geo/1.0/direct?q=" + et_dataInput.getText().toString() + ",de &appid=ff76494d7fec7ca86a761ecc9dd6b12a";
                String urlForWeather = "https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=ff76494d7fec7ca86a761ecc9dd6b12a";

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlForCity, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject coord = response.getJSONObject(0);
                            Coordinates coordinates = new Coordinates(coord.getString("lon"), coord.getString("lat"));
                            //Toast.makeText(getApplicationContext(), coordinates.toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("HS_KL", error.toString());
                    }
                });

                MySingleton.getInstance(WeatherActivity.this).addToRequestQueue(request);

            }
        });


    }

}


