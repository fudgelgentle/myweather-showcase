package cse340.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.*;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


// * The MainActivity is the home page of the app. This page has all of the information from
// * other pages (temperature, rain chance, humidity level, aqi) and uses them to give a
// * short summary on today's weather. It also tracks the device's current location using
// * location listener in order to get accurate information on temperature, rain chance, and aqi.
public class MainActivity extends AbstractActivity implements MyCallBack, SensorEventListener, LocationListener {

    private int current_aqi;

    private float current_humidity;

    private double current_temperature;

    private double current_rain_chance;

    private double current_latitude;
    private double current_longitude;
    private String current_address;

    // This variable compiles all of the description for temp, aqi, rain, humidity
    private String contentDescription;

    private String aqiDesc;
    private String tempDesc;
    private String rainDesc;
    private String humidityDesc;


    // * Instance variables for dealing with sensors
    //The last time the screen was updated - this is so we can actually see the values
    private long mLastUpdate;
    private SensorManager mSensorManager;
    private Sensor humiditySensor;
    // How fast we want to update the display - for the whole activity
    private final int DELAY = SensorManager.SENSOR_DELAY_UI;

    // * For updating content on the page in response to API calls
    @Override
    public void onAqiDataReceived(int data) {
        // Do something with the data
        System.out.println("data = " + data);
        current_aqi = data;
        aqiDesc = "Air Quality is " + interpretAQI(current_aqi, false).getData1();

        createContentDescription();
        TextView text = findViewById(R.id.h2_title);
        text.setText(contentDescription);
    }

    @Override
    public void onTemperatureDataReceived(TempAndRain data) {
        current_temperature = data.getCurrent_temperature();
        current_rain_chance = data.getCurrent_rain_chance();

        tempDesc = interpretTemperature(current_temperature, false);
        tempDesc = tempDesc.substring(0, tempDesc.length() - 1) + " Temperature";

        rainDesc = interpretRainChance(current_rain_chance, false);
        rainDesc = rainDesc.substring(0, rainDesc.length() - 1);

        createContentDescription();
        TextView text = findViewById(R.id.h2_title);
        text.setText(contentDescription);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView home_icon = findViewById(R.id.home_icon);
        home_icon.setImageResource(R.drawable.home_clicked);


        // ** Asks for location permission
        requestLocationPermission();
        getLocation();

        // initialize lat,long to Seattle by default
//        current_latitude = 47.6062;
//        current_longitude = -122.3321;
//        current_address = "123 ABC Street";
//        System.out.println("current_lat = " + current_latitude);
//        System.out.println("current_long = " + current_longitude);
//        System.out.println("current_address = " + current_address);

        // ** initializing private variables
        aqiDesc = "...";
        tempDesc = "...";
        rainDesc = "...";
        humidityDesc = "...";


        // ** Calling the AirQualityTask
//        AirQualityTask airQualityTask = new AirQualityTask(current_latitude, current_longitude, this);
//        airQualityTask.execute();

        // ** Sets the current day of the week
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String currentDay = daysOfWeek[day - 1];
        TextView text = findViewById(R.id.h1_title);
        text.setText(currentDay);


        // ** Sets up the humidity sensor
        // ** NOTE: Only humidity will be called in onCreate.
        // ********* Temperature and AirQuality will be called in onLocationChanged because
        // ********* they rely on the current location
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        humiditySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        // Start the clock
        mLastUpdate = System.currentTimeMillis();


        // ** Calling TemperatureApiClient
//        TemperatureTask temperatureTask = new TemperatureTask(current_latitude, current_longitude,
//                this);
//        temperatureTask.execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, humiditySensor, DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        current_humidity = event.values[0];

        humidityDesc = (String) interpretHumidity(current_humidity, false).getData1();
        createContentDescription();
        TextView text = findViewById(R.id.h2_title);
        text.setText(contentDescription);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Not implemented
    }

    private void createContentDescription() {
        this.contentDescription = aqiDesc + "\n" + tempDesc + "\n" + rainDesc + "\n" + humidityDesc;
    }

    //************************* Everything below is for location *********************************

    public void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION }, 100);

        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        try {
            LocationManager locationManager =
                    (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("got in onLocationChanged");
        current_latitude = location.getLatitude();
        current_longitude = location.getLongitude();
        // Geocoder helps transform street address or any address into lat/long format
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(current_latitude,
                    current_longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            current_address = address;
        } catch(Exception e) {
            e.printStackTrace();
        }

        // ** Calling TemperatureApiClient
        TemperatureTask temperatureTask = new TemperatureTask(current_latitude, current_longitude,
                this);
        temperatureTask.execute();

        // ** Calling the AirQualityTask
        AirQualityTask airQualityTask = new AirQualityTask(current_latitude, current_longitude, this);
        airQualityTask.execute();

        // ** Replaces default logo with location information
        ImageView img = findViewById(R.id.header_icon);
        img.setImageResource(R.drawable.location2);
        img.setContentDescription(getResources().getString(R.string.pin_desc));
        TextView txt = findViewById(R.id.street_address);
        txt.setText(current_address);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}

}