package cse340.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

// * The AQIActivity is the AQI page. It uses API to get the aqi or air quality level based
// * on the current lat/long, which is retrieved by a location listener.
public class AQIActivity extends AbstractActivity implements MyCallBack, LocationListener {

    private int current_aqi;

    private String aqiDesc;
    private String aqiTips;

    private double current_latitude;
    private double current_longitude;
    private String current_address;

    @Override
    public void onTemperatureDataReceived(TempAndRain data) {
        // Not implemented. Only need it on temperature page.
    }

    // * For updating content on the page in response to API calls
    @Override
    public void onAqiDataReceived(int data) {
        // Do something with the data
        System.out.println("data = " + data);
        current_aqi = data;
        TextView titleText = findViewById(R.id.sfn_h1_title);
//        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 42);
        titleText.setText("" + current_aqi);

        TextView descText = findViewById(R.id.sfn_h2_title);
        TextView tipsText = findViewById(R.id.sfn_h3_title);
        aqiDesc = (String) interpretAQI(current_aqi, true).getData1();
        aqiTips = (String) interpretAQI(current_aqi, true).getData2();
        descText.setText(aqiDesc);
        tipsText.setText(aqiTips);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqi);

        ImageView AQI_icon = findViewById(R.id.aqi_icon);
        AQI_icon.setImageResource(R.drawable.aqi_clicked);

        // ** Asks for location permission
        requestLocationPermission();
        getLocation();

        TextView text = findViewById(R.id.sfn_h1_title);
        text.setText("...");
        TextView text2 = findViewById(R.id.sfn_h2_title);
        text2.setText("...");

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

        // ** Gets the street address of the current lat/long
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(current_latitude,
                    current_longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            current_address = address;
        } catch(Exception e) {
            e.printStackTrace();
        }

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

    // Not implemented.
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    // Not implemented.
    @Override
    public void onProviderEnabled(String s) {}

    // Not implemented.
    @Override
    public void onProviderDisabled(String s) {}

}