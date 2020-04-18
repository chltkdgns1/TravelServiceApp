package com.gkftndltek.t_map_test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapPolygon;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String project_key = "l7xx713d4db3b29b418dba74f8af6162f4fb";
    private String secret_key = "8417379e421e4a54800524b24b1db366";
    private TMapPoint tMapPointStart, tMapPointEnd;
    private TMapView tMapView;
    private LinearLayout linearLayoutTmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey( project_key);
        linearLayoutTmap.addView( tMapView );

        tMapPointStart = new TMapPoint(37.570841, 126.985302); // SKT타워(출발지)
        tMapPointEnd = new TMapPoint(37.566537, 126.982604); // 을지로입구역

        tMapView.setCenterPoint( 126.985302, 37.570841 );

        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                    tMapPolyLine.setLineColor(Color.RED);
                    tMapPolyLine.setLineWidth(50);
                    tMapView.addTMapPolyLine("Line1", tMapPolyLine);
                    linearLayoutTmap.addView( tMapView );

                }catch(Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
