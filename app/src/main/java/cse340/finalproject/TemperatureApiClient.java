package cse340.finalproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// * A client class that handles calling the temperature API.
public class TemperatureApiClient {

    public static TempAndRain getTemperature(double latitude, double longitude) throws IOException,
            JSONException {

        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude" +
                "=" + longitude + "&hourly=temperature_2m," +
                "precipitation_probability&current_weather=true&timezone=America%2FLos_Angeles";
        System.out.println("weather api url = " + url);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonObject = new JSONObject(response.toString());
//        System.out.println("jsonObject = " + jsonObject);
        JSONObject cw = jsonObject.getJSONObject("current_weather");
        Double curr_temperature = cw.getDouble("temperature");
        System.out.println("curr_temp = " + curr_temperature);

        String time_ISO8601 = cw.getString("time");
        System.out.println("time_ISO8601 = " + time_ISO8601);

        JSONObject hourly = jsonObject.getJSONObject("hourly");
        JSONArray time_array = hourly.getJSONArray("time");
        int target_index = 0;

        System.out.println("time = " + time_array);
        for (int i = 0; i < time_array.length(); i++) {
            if (time_array.get(i).equals(time_ISO8601)) {
                target_index = i;
            }
        }

        JSONArray rain_array = hourly.getJSONArray("precipitation_probability");
        Double rain_chance = new Double(rain_array.get(target_index).toString());
        System.out.println("rain_chance = " + rain_chance);

        TempAndRain tempAndRain = new TempAndRain(curr_temperature, rain_chance);

        return tempAndRain;

    }

}
