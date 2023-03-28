package cse340.finalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

// * This abstract activity class contains methods that help interpret different temperature, rain
// * chance, humidity, and aqi levels and explain to the user what they mean.
public class AbstractActivity extends AppCompatActivity {

    /**
     * Returns a string with a temperature description based on the current temperature
     * @param current_temperature The value of the current_temperature (e.g. 14°C)
     * @param includeTips boolean specifying whether the user wants additional tips or not
     * @return A string with a temperature description based on the current temperature
     */
    public String interpretTemperature(double current_temperature, boolean includeTips) {
        String tempDesc = "";
        if (current_temperature < -20) {
            tempDesc = getResources().getString(R.string.extremely_cold);
            if (includeTips) {
                tempDesc = tempDesc + " " + getResources().getString(R.string.extremely_cold_tips);
            }
        } else if (current_temperature >= -20 && current_temperature < -10) {
            tempDesc = getResources().getString(R.string.very_cold);
            if (includeTips) {
                tempDesc = tempDesc + " " + getResources().getString(R.string.very_cold_tips);
            }
        } else if (current_temperature >= -10 && current_temperature < 0) {
            tempDesc = getResources().getString(R.string.cold);
            if (includeTips) {
                tempDesc = tempDesc + " " + getResources().getString(R.string.cold_tips);
            }
        } else if (current_temperature >= 0 && current_temperature < 10) {
            tempDesc = getResources().getString(R.string.cool);
            if (includeTips) {
                tempDesc = tempDesc + " " + getResources().getString(R.string.cool_tips);
            }
        } else if (current_temperature >= 10 && current_temperature < 20) {
            tempDesc = getResources().getString(R.string.mild);
            if (includeTips) {
                tempDesc = tempDesc + " " + getResources().getString(R.string.mild_tips);
            }
        } else if (current_temperature >= 20 && current_temperature < 30) {
            tempDesc = getResources().getString(R.string.warm);
            if (includeTips) {
                tempDesc = tempDesc + " " + getResources().getString(R.string.warm_tips);
            }
            // case: temp > 30
        } else {
            tempDesc = getResources().getString(R.string.hot);
            if (includeTips) {
                tempDesc = tempDesc + " " + getResources().getString(R.string.hot_tips);
            }
        }
        return tempDesc;
    }


    /**
     * @param current_rain_chance The likelihood of rain (e.g. 70.0 --> 70.0% chance of rain)
     * @param includeTips boolean specifying whether the user wants additional tips or not
     * @return A string with a rain chance description based on the current rain chance
     */
    public String interpretRainChance(double current_rain_chance, boolean includeTips) {
        String rainDesc = "";
        if (current_rain_chance <= 0.0) {
            // no tips available
            rainDesc = getResources().getString(R.string.no_precipitation);
        } else if (current_rain_chance > 0.0 && current_rain_chance < 10.0) {
            // no tips available
            rainDesc = getResources().getString(R.string.very_low_precipitation);
        } else if (current_rain_chance >= 10.0 && current_rain_chance < 30.0) {
            // no tips available
            rainDesc = getResources().getString(R.string.low_precipitation);
        } else if (current_rain_chance >= 30.0 && current_rain_chance < 50.0) {
            rainDesc =
                    getResources().getString(R.string.moderate_precipitation);
            if (includeTips) {
                rainDesc = rainDesc + " " + getResources().getString(R.string.moderate_precipitation_tips);
            }
        } else if (current_rain_chance >= 50.0 && current_rain_chance < 70.0) {
            rainDesc = getResources().getString(R.string.high_precipitation);
            if (includeTips) {
                rainDesc = rainDesc + " " + getResources().getString(R.string.high_precipitation_tips);
            }
            // case: rain chance >= 70
        } else {
            rainDesc = getResources().getString(R.string.very_high_precipitation);
            if (includeTips) {
                rainDesc = rainDesc + " " + getResources().getString(R.string.very_high_precipitation_tips);
            }
        }
        return rainDesc;
    }

    /**
     * @param current_humidity The current humidity level (e.g. 69.1 --> 69.1% relative humidity)
     * @param includeTipsNDesc boolean specifying whether the user wants additional tips or not
     * @return A MultipleData object that contains both humidity description (e.g. "High humidity")
     * and humidity tips. If includeTipsNDesc is set to false, the humidity tips will be an empty
     * string.
     */
    public MultipleData interpretHumidity(float current_humidity, boolean includeTipsNDesc) {
        String humidityDesc = "";
        String humidtipsNDesc = "";
        if (current_humidity < 20) {
//            System.out.println("return extremely low");
            humidityDesc = getResources().getString(R.string.extremely_low_humid);
            if (includeTipsNDesc) {
                humidtipsNDesc = getResources().getString(R.string.extreme_to_low_humid_desc) +
                        "\n" +
                        "\nTips: " + getResources().getString(R.string.extreme_to_low_humid_tips);
            }
        } else if (20 <= current_humidity && current_humidity < 30) {
//            System.out.println("return very low");
            humidityDesc = getResources().getString(R.string.very_low_humid);
            if (includeTipsNDesc) {
                humidtipsNDesc = getResources().getString(R.string.extreme_to_low_humid_desc) + "\n\nTips: "
                        + getResources().getString(R.string.extreme_to_low_humid_tips);
            }
        } else if (30 <= current_humidity && current_humidity < 40) {
//            System.out.println("return low");
            humidityDesc = getResources().getString(R.string.low_humid);
            if (includeTipsNDesc) {
                humidtipsNDesc = getResources().getString(R.string.low_humid_desc) + "\n\nTips: "
                        + getResources().getString(R.string.low_humid_tips);
            }
        } else if (40 <= current_humidity && current_humidity < 60) {
//            System.out.println("return moderate");
            humidityDesc = getResources().getString(R.string.moderate_humid);
            if (includeTipsNDesc) {
                humidtipsNDesc = getResources().getString(R.string.moderate_humid_desc);
            }
        } else if (60 <= current_humidity && current_humidity < 70) {
//            System.out.println("return high");
            humidityDesc = getResources().getString(R.string.high_humid);
            if (includeTipsNDesc) {
                humidtipsNDesc = getResources().getString(R.string.high_humid_desc) + "\n\nTips: "
                        + getResources().getString(R.string.high_humid_tips);
            }
        } else if (current_humidity >= 70) {
//            System.out.println("return very high");
            humidityDesc = getResources().getString(R.string.very_high_humid);
            if (includeTipsNDesc) {
                humidtipsNDesc = getResources().getString(R.string.very_high_humid_desc) + "\n\nTips: "
                        + getResources().getString(R.string.very_high_humid_tips);
            }
        } else {
            humidityDesc = getResources().getString(R.string.humid_error);
            if (includeTipsNDesc) {
                humidtipsNDesc = getResources().getString(R.string.humid_error);
            }
            System.out.println("error");
        }
        return new MultipleData(humidityDesc, humidtipsNDesc);
    }

    /**
     * @param current_aqi The integer value of the current aqi
     * @param includeTips boolean specifying whether the user wants additional tips or not
     * @return A MultipleData object that contains both the description of the current aqi and
     * any AQI tips. If includeTips is set to false, the aqi tips will be an empty string
     */
    public MultipleData interpretAQI(int current_aqi, boolean includeTips) {
        String aqiTips = "";
        String aqiDesc = "";
        if (current_aqi > 0 && current_aqi <= 50) {
            aqiDesc = getResources().getString(R.string.good_aqi);
            if (includeTips) {
                aqiTips = getResources().getString(R.string.good_aqi_desc);
            }
        } else if (current_aqi > 50 && current_aqi <= 100) {
            aqiDesc = getResources().getString(R.string.moderate_aqi);
            if (includeTips) {
                aqiTips = getResources().getString(R.string.moderate_aqi_desc);
            }
        } else if (current_aqi > 100 && current_aqi <= 150) {
            aqiDesc = getResources().getString(R.string.unhealthy_for_sensitive_aqi);
            if (includeTips) {
                aqiTips = getResources().getString(R.string.unhealthy_for_sensitive_aqi_desc);
            }
        } else if (current_aqi > 150 && current_aqi <= 200) {
            aqiDesc = getResources().getString(R.string.unhealthy_aqi);
            if (includeTips) {
                aqiTips = getResources().getString(R.string.unhealthy_aqi_desc);
            }
        } else if (current_aqi > 200 && current_aqi <= 300) {
            aqiDesc = getResources().getString(R.string.very_unhealthy_aqi);
            if (includeTips) {
                aqiTips = getResources().getString(R.string.very_unhealthy_aqi_desc);
            }
        } else if (current_aqi > 300) {
            aqiDesc = getResources().getString(R.string.hazardous_aqi);
            if (includeTips) {
                aqiTips = getResources().getString(R.string.hazardous_aqi_desc);
            }
        }
        return new MultipleData(aqiDesc, aqiTips);
    }

    /**
     * Bolds a specified word of a given text.
     * @param result The modified text
     * @param originalText The original text
     * @param wordToBold The word to bold in the modified text
     */
    public void boldText(SpannableString result, String originalText, String wordToBold) {
        int startIndex = originalText.indexOf(wordToBold);
        if (startIndex != -1) {
            int endIndex = startIndex + wordToBold.length();
            result.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}
