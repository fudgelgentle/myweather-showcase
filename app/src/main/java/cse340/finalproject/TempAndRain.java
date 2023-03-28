package cse340.finalproject;

// public class used for storing temperature + rain chance value and passing those two values
// between async tasks and activity.
public final class TempAndRain {
    private double current_temperature;
    private double current_rain_chance;

    public TempAndRain(double temp, double rain) {
        this.current_temperature = temp;
        this.current_rain_chance = rain;
    }

    public double getCurrent_temperature() {
        return current_temperature;
    }

    public double getCurrent_rain_chance() {
        return current_rain_chance;
    }
}