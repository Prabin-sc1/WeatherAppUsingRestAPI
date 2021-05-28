package com.prabindomain.weatherapiapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String HTTPS_WWW_METAWEATHER_COM_API_LOCATION_SEARCH_QUERY = "https://www.metaweather.com/api/location/search/?query=";
    public static final String THREAD_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";
    Context context;
    String cityId;


    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);
        void onResponse(String cityId);
    }

    public void getCityId(String cityName, VolleyResponseListener volleyResponseListener){
        List<WeatherReportModel> weatherReportModels = new ArrayList<>();
        String url = HTTPS_WWW_METAWEATHER_COM_API_LOCATION_SEARCH_QUERY +cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                cityId = "";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityId = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               // Toast.makeText(context,"City ID = "+cityId,Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityId);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(context, "error occurred",Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something wrong");

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public interface ForeCastByIdResponse{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }


    public void getCityForecastById(String cityId,ForeCastByIdResponse foreCastByIdResponse){
        List<WeatherReportModel> report = new ArrayList<>();
        String url = THREAD_FOR_CITY_WEATHER_BY_ID+cityId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();

                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");

                    for(int i = 0; i < consolidated_weather_list.length(); i++) {
                        WeatherReportModel weatherReportModel = new WeatherReportModel();
                        JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);


                        weatherReportModel.setId(first_day_from_api.getInt("id"));
                        weatherReportModel.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                        weatherReportModel.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        weatherReportModel.setCreated(first_day_from_api.getString("created"));
                        weatherReportModel.setApplicable_date(first_day_from_api.getString("applicable_date"));

                        weatherReportModel.setMin_temp(first_day_from_api.getLong("min_temp"));
                        weatherReportModel.setMax_temp(first_day_from_api.getLong("max_temp"));
                        weatherReportModel.setThe_temp(first_day_from_api.getLong("the_temp"));
                        weatherReportModel.setWind_speed(first_day_from_api.getLong("wind_speed"));
                        weatherReportModel.setWind_direction(first_day_from_api.getLong("wind_direction"));


                        weatherReportModel.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                        weatherReportModel.setHumidity(first_day_from_api.getInt("humidity"));
                        weatherReportModel.setVisibility(first_day_from_api.getLong("visibility"));
                        weatherReportModel.setPredictability(first_day_from_api.getInt("predictability"));
                        report.add(weatherReportModel);
                    }
                    foreCastByIdResponse.onResponse(report);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
    public interface GetCityForecastByNameCallback{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }
    public void getCityForecastByName(String cityName,GetCityForecastByNameCallback getCityForecastByNameCallback){
        getCityId(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityId) {
                getCityForecastById(cityId, new ForeCastByIdResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        getCityForecastByNameCallback.onResponse(weatherReportModels);

                    }
                });
            }
        });
    }

}
