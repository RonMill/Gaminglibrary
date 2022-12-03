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
import com.example.gaminglibrary.API.WeatherDataService;
import com.example.gaminglibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    Button btn_getWeather;
    EditText et_dataInput;
    ListView lv_WeatherReport;

    WeatherDataService weatherDataService = new WeatherDataService(WeatherActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ////API WAS GEHT ALLA
        btn_getWeather = (Button) findViewById(R.id.GET_WEATHERBUTTON);
        et_dataInput = (EditText) findViewById(R.id.EDITTEXTLOCATION);
        lv_WeatherReport = (ListView) findViewById(R.id.WeatherReport);
        et_dataInput.setText("meisenheim");


        btn_getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherDataService.getCoordinates(et_dataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(WeatherActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Coordinates coordinates) {
                        Toast.makeText(WeatherActivity.this, coordinates.toString(), Toast.LENGTH_SHORT).show();
                        weatherDataService.getWeatherReportByLonLat();
                    }
                });
            }
        });
    }
}


