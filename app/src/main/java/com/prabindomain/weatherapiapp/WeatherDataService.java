package com.prabindomain.weatherapiapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataService {

    public static final String HTTPS_WWW_METAWEATHER_COM_API_LOCATION_SEARCH_QUERY = "https://www.metaweather.com/api/location/search/?query=";
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

    public List<WeatherReportModel> getCityForecastById(String cityId){

    }
    public List<WeatherReportModel> getCityForecastByName(String cityName){

    }

}
