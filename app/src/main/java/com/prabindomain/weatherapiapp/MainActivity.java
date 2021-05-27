package com.prabindomain.weatherapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button btnCityId, btnWeatherById, btnWeatherByName;
    private EditText dataInput;
    ListView  listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCityId = findViewById(R.id.btn_getCityId);
        btnWeatherById = findViewById(R.id.btn_getWeatherByCityId);
        btnWeatherByName = findViewById(R.id.btn_getWeatherByCityName);
        dataInput = findViewById(R.id.et_dataInput);
        listView = findViewById(R.id.listViewId);


        WeatherDataService weatherDataService = new WeatherDataService(getApplicationContext());
        btnWeatherById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDataService.getCityForecastById(dataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(),"Something wrong",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityId) {
                        Toast.makeText(getApplicationContext(),"Returned an ID of "+cityId,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"You typed "+dataInput.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        btnCityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 weatherDataService.getCityId(dataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(),"Something wrong",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityId) {
                        Toast.makeText(getApplicationContext(),"Returned an ID of "+cityId,Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }

}