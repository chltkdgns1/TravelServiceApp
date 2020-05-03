package com.gkftndltek.atobdistance;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        double a = 36.634649, b = 127.456350;
        double x = 36.634722, y =  127.457396;

        System.out.println(getDistance(a,b,x,y));
    }

    public double getDistance(double lat1 , double lng1 , double lat2 , double lng2 ) {
        double distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);

        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);

        distance = locationA.distanceTo(locationB);

        return distance;
    }
}
