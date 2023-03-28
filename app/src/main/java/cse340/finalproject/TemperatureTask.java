package cse340.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


// * This is an asynchronous task that handles passing data from API to an activity using
// * callbacks
public class TemperatureTask extends AsyncTask<Void, Void, TempAndRain> {

    private double latitude;
    private double longitude;

    private MyCallBack callBack;

    public TemperatureTask(double latitude, double longitude, MyCallBack callBack) {
        this.callBack = callBack;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Fetches the temperature API and returns a TempAndRain object containing the temperature
    // level and rain chance
    @Override
    protected TempAndRain doInBackground(Void... voids) {
        try {
            TempAndRain temperature = TemperatureApiClient.getTemperature(latitude, longitude);
            return temperature;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Uses callback to extract the temperature and rain chance value out of it
    @Override
    protected void onPostExecute(TempAndRain temperature) {
        if (temperature != null) {
            Log.d("MainActivity", "Temperature: " + temperature);

            if (callBack != null) {
                callBack.onTemperatureDataReceived(temperature);
            }
        }
    }
}
