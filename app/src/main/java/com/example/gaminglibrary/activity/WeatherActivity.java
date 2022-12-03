package com.example.gaminglibrary.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gaminglibrary.API.Coordinates;
import com.example.gaminglibrary.API.WeatherDataService;
import com.example.gaminglibrary.API.WeatherReportModel;
import com.example.gaminglibrary.R;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    Button btn_getWeather;
    EditText et_dataInput;
    ImageView weatherImage;
    TextView temperature;

    WeatherDataService weatherDataService = new WeatherDataService(WeatherActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ////API WAS GEHT ALLA
        btn_getWeather = (Button) findViewById(R.id.GET_WEATHERBUTTON);
        et_dataInput = (EditText) findViewById(R.id.EDITTEXTLOCATION);
        weatherImage = (ImageView) findViewById(R.id.WEATHERIMAGE);
        temperature = (TextView) findViewById(R.id.TEMPERATURE);

        btn_getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherDataService.getCoordinates(et_dataInput.getText().toString(), new WeatherDataService.VolleyResponseListenerForCoordinates() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(WeatherActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Coordinates coordinates) {
                        weatherDataService.getWeatherReportByLonLat(new WeatherDataService.VolleyResponseListenerForWeaterReport() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(WeatherActivity.this, message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(WeatherReportModel weatherReportModel) {

                                switch(weatherReportModel.getDescription()){
                                    case "Bedeckt": weatherImage.setImageResource(R.drawable.icons8_cloud_48); break;
                                    case "Sonnig": weatherImage.setImageResource(R.drawable.icons8_sun_48); break;
                                    case "Regen": weatherImage.setImageResource(R.drawable.icons8_torrential_rain_48); break;
                                    default:
                                }

                                temperature.setText("Aktuell: "+weatherReportModel.getTemperature().getCurrent()+" °C"
                                +"\nGefühlt: "+weatherReportModel.getTemperature().getFeels_like_temp()+" °C"
                                                + "\nMin: "+weatherReportModel.getTemperature().getMin()+" °C"
                                                +"\nMax: "+weatherReportModel.getTemperature().getMax()+" °C"
                                );
                                //TODO: Fill lv_WeatherReport with weatherReportModel
                            }
                        });
                    }
                });
            }
        });
    }
}


