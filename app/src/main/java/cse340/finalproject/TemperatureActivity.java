package cse340.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;


// * The TemperatureActivity is the temperature page. It calls an API that gets the temperature and
// * rain chance based on the current lat/long, which is retrieved by a location listener.
public class TemperatureActivity extends AbstractActivity implements MyCallBack, LocationListener {

    private static double current_temperature;
    private static double current_rain_chance;

    private double current_latitude;
    private double current_longitude;
    private String current_address;

    @Override
    public void onAqiDataReceived(int data) {
        // Not implemented.
    }

    // * For updating content on the page in response to API calls
    @Override
    public void onTemperatureDataReceived(TempAndRain data) {
        current_temperature = data.getCurrent_temperature();
        current_rain_chance = data.getCurrent_rain_chance();
        System.out.println("current_temperature = " + current_temperature);

        TextView temp = findViewById(R.id.sfn_h1_title);
        temp.setText("" + current_temperature + "° C");

        TextView rain = findViewById(R.id.sfn_h2_title);
        rain.setText("Precipitation chance: " + current_rain_chance + "%");

        TextView tips = findViewById(R.id.sfn_h3_title);


        String tempDesc = "";
        String rainDesc = "";

        // * Interpret different temperature level
        tempDesc = interpretTemperature(current_temperature, true);

        // * Interpret different precipitation level
        rainDesc = interpretRainChance(current_rain_chance, true);

        // * Bolding some part of the text
        String content = "Temperature: " + tempDesc + " \n\n" +  "Precipitation: " + rainDesc;
        SpannableString spannableString = new SpannableString(content);
        boldText(spannableString, content, "Temperature:");
        boldText(spannableString, content, "Precipitation:");
        tips.setText(spannableString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        ImageView temp_icon = findViewById(R.id.temperature_icon);
        temp_icon.setImageResource(R.drawable.temperature_clicked);

        TextView text = findViewById(R.id.sfn_h1_title);
        TextView text2 = findViewById(R.id.sfn_h2_title);
        text.setText("...");
        text2.setText("...");

        // ** Asks for location permission
        requestLocationPermission();
        getLocation();

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