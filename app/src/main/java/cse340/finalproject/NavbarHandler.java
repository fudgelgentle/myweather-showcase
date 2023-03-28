package cse340.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

// A custom listener that handles activity (page) change for each nav item
public class NavbarHandler implements View.OnClickListener{
    private Context context;

    public final String HOME_KEY = "IS_HOME_CLICKED";
    public final String TEMPERATURE_KEY = "IS_TEMPERATURE_CLICKED";
    public final String HUMIDITY_KEY = "IS_HUMIDITY_CLICKED";
    public final String AQI_KEY = "IS_AQI_CLICKED";

    public NavbarHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.home_icon:
                Intent home_intent = new Intent(context, MainActivity.class);
                System.out.println("putting is home true");
                home_intent.putExtra(HOME_KEY, true);
                context.startActivity(home_intent);
                break;
            case R.id.temperature_icon:
                Intent temperature_intent = new Intent(context, TemperatureActivity.class);
                temperature_intent.putExtra(TEMPERATURE_KEY, true);
                context.startActivity(temperature_intent);
                break;
            case R.id.humidity_icon:
                Intent humidity_intent = new Intent(context, HumidityActivity.class);
                humidity_intent.putExtra(HUMIDITY_KEY, true);
                context.startActivity(humidity_intent);
                break;
            case R.id.aqi_icon:
                Intent aqi_intent = new Intent(context, AQIActivity.class);
                aqi_intent.putExtra(AQI_KEY, true);
                context.startActivity(aqi_intent);
                break;
        }
    }
}
