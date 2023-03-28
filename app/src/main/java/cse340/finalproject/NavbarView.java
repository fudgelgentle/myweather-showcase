package cse340.finalproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class NavbarView extends FrameLayout {

    // Creates a new navbarview when called
    public NavbarView(Context context, AttributeSet attributeSet) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.navbar, this, false);
        this.addView(view);

        // ************* This part handles clickable nav items **********************************
        NavbarHandler navbarHandler = new NavbarHandler(this.getContext());

        ImageView home_icon = findViewById(R.id.home_icon);
        ImageView temperature_icon = findViewById(R.id.temperature_icon);
        ImageView humidity_icon = findViewById(R.id.humidity_icon);
        ImageView aqi_icon = findViewById(R.id.aqi_icon);

        home_icon.setOnClickListener(navbarHandler);
        temperature_icon.setOnClickListener(navbarHandler);
        humidity_icon.setOnClickListener(navbarHandler);
        aqi_icon.setOnClickListener(navbarHandler);
        // ************* This part handles clickable nav items **********************************
    }


}
