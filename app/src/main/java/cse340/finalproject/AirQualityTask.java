package cse340.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// * This is an asynchronous task that handles API calls
public class AirQualityTask extends AsyncTask<Void, Void, JSONObject> {

    private String apiKey = "";
    private String city;

    private double latitude;
    private double longitude;


    private MyCallBack callBack;

    public AirQualityTask(String city, MyCallBack callBack) {
        this.callBack = callBack;
        this.city = city;
    }

    public AirQualityTask(Double latitude, Double longitude, MyCallBack callBack) {
        this.callBack = callBack;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AirQualityTask(String city) {
        this.city = city;
    }

    // Fetches the air quality API and returns a JSONObject of the data
    @Override
    protected JSONObject doInBackground(Void... voids) {

        try {
//            String url = "https://api.waqi.info/feed/" + city + "/?token=" + apiKey;
            String url =
                    "https://api.waqi.info/feed/geo:" + latitude + ";" + longitude + "/?token=" + apiKey;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            return new JSONObject(responseBody);
        } catch(IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Parses the jsonObject and extract the AQI value out of it
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                int aqi = data.getInt("aqi");

                if (callBack != null) {
                    callBack.onAqiDataReceived(aqi);
                }

            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
