package com.alsdnjsrl.tmapapitest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TMapView tMapView;
    private  TMapPoint tMapPointStart,tMapPointEnd;
    private TMapData tmapdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey( "l7xx78ce6244fbb34c96b88e96ad54e4074e");
                linearLayoutTmap.addView( tMapView );

        tMapPointStart = new TMapPoint(37.570841, 126.985302); // SKT타워(출발지)
        tMapPointEnd = new TMapPoint(37.551135, 126.988205); // N서울타워(목적지)
        tmapdata = new TMapData();
        String strData = "편의점";
        tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList poiItem) {
                for(int i = 0; i < poiItem.size(); i++) {
                    TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                    Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                            "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                            "Point: " + item.getPOIPoint().toString());
                }
            }
        });
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(50);
                    tMapView.addTMapPolyLine("Line1", tMapPolyLine);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
