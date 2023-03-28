package cse340.finalproject;

// Interface that uses callback to retrieve information from asynchronous tasks
public interface MyCallBack {
    void onAqiDataReceived(int data);

    void onTemperatureDataReceived(TempAndRain data);
}
