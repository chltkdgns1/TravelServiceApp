package com.gkftndltek.travelcourceapp.CustMap.Calender;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler.CalenderDataClass;
import com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler.TravelClass;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.NoneBitmapDestData;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CalenderMapConnectActivity extends AppCompatActivity {

    private String token, name;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    private TravelClass travelClass;


    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // 네이버 검색했다
                travelClass.add((DestinationDataClass) msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_map_activity);

        init();
    }

    void init() {

        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token"); // 유저네임
        name = (String) bun.get("name");   // 맵 네임

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        travelClass = new TravelClass();
        travelClass.execute(this, getApplicationContext());

        ref.child("users").child(token).child("maps").child(name).child("mylist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot data : dataSnapshot.getChildren()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NoneBitmapDestData cdata = data.getValue(NoneBitmapDestData.class);

                            DestinationDataClass destdata = new DestinationDataClass();

                            if (cdata.getUrl() != null && !cdata.getUrl().isEmpty()) {
                                destdata.setLink(getImageBitmap(cdata.getUrl()));
                            }

                            destdata.setAddress(cdata.getAddress());
                            destdata.setCategory(cdata.getCategory());
                            destdata.setDescription(cdata.getDescription());
                            destdata.setDrable(cdata.getDrable());
                            destdata.setAddress(cdata.getAddress());
                            destdata.setIndex(cdata.getIndex());
                            destdata.setName(cdata.getName());
                            destdata.setPhone(cdata.getPhone());
                            destdata.setRoadAddress(cdata.getRoadAddress());
                            destdata.setX(cdata.getX());
                            destdata.setY(cdata.getY());
                            destdata.setUrl(cdata.getUrl());

                            Message msg = Message.obtain();
                            msg.obj = destdata;
                            msg.what = 1;
                            handlerPushMessage.sendMessage(msg);
                        }
                    }).start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {

        }
        return bm;
    }
}