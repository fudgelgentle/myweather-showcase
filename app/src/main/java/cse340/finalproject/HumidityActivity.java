package cse340.finalproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.ImageView;
import android.widget.TextView;

// * The HumidityActivity is the humidity page. It uses sensor to get humidity data.
public class HumidityActivity extends AbstractActivity implements SensorEventListener {

    private float current_humidity;

    private String humDesc = "";
    private String humidtipsNDesc = "";

    // * Instance variables for dealing with sensors
    private SensorManager mSensorManager;
    private Sensor humiditySensor;
    // How fast we want to update the display - for the whole activity
    private final int DELAY = SensorManager.SENSOR_DELAY_UI;
    //The last time the screen was updated - this is so we can actually see the values
    private long mLastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity);

        ImageView humidity_icon = findViewById(R.id.humidity_icon);
        humidity_icon.setImageResource(R.drawable.humidity_clicked);

        // ** Sets up the sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        humiditySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        // Start the clock
        mLastUpdate = System.currentTimeMillis();

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
        humDesc = (String) interpretHumidity(current_humidity, true).getData1();
        humidtipsNDesc =
                (String) interpretHumidity(current_humidity, true).getData2();

        TextView heading_text = findViewById(R.id.sfn_h1_title);
        String humidString = "" + current_humidity;
        String result = getResources().getString(R.string.humid_percentage, humidString);
        heading_text.setText(result);

        TextView desc_text = findViewById(R.id.sfn_h2_title);
        desc_text.setText(humDesc);

        TextView tips = findViewById(R.id.sfn_h3_title);
        SpannableString spannableString = new SpannableString(humidtipsNDesc);
        boldText(spannableString, humidtipsNDesc, "Tips:");
        tips.setText(spannableString);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Not implemented
    }
}