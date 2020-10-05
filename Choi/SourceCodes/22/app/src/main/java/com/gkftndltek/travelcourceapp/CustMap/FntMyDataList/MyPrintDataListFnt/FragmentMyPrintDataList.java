package com.gkftndltek.travelcourceapp.CustMap.FntMyDataList.MyPrintDataListFnt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.CustMap.FntMyDataList.MyPrintDataListFnt.MyMapDataListRecycler.MyMapDataClass;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.NoneBitmapDestData;
import com.gkftndltek.travelcourceapp.CustMap.MainActivity;
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

public class FragmentMyPrintDataList extends Fragment {

  //  private String key = "l7xx713d4db3b29b418dba74f8af6162f4fb";
    private View rootView;
    private Context con;

    // 데이터 베이스
    private String token, mapName;
    private FirebaseDatabase database;
    private DatabaseReference users, logined;

    // 리사이클

    MyMapDataClass myMapDataClass;

    // 액티비티

    private Activity act;

    // 데이터베이스에서 가져온 목적지 데이터들의 집합


    private Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                DestinationDataClass data = (DestinationDataClass) msg.obj;
                ((MainActivity)getActivity()).DestSum.add(data);
                myMapDataClass.add(data);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_mapdata_printlist, container, false);
        // 리니어레이아웃
        init();

        return rootView;
    }

    void init() {

        act = (MainActivity)getActivity();
        con = getActivity();

        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        logined = database.getReference("logined");

        token = ((MainActivity)act).token;
        mapName = ((MainActivity)act).mapName;

        if(!((MainActivity)getActivity()).DestSum.isEmpty())
           ((MainActivity)getActivity()).DestSum.clear();

        myMapDataClass = new MyMapDataClass();
        myMapDataClass .execute(rootView, con, handlerPushMessage);

        users.child(token).child("maps").child(mapName).child("mylist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapData : dataSnapshot.getChildren()) {
                    synchronized (this) {
                        new Thread() {
                            @Override
                            public void run() {
                                NoneBitmapDestData nondata = snapData.getValue(NoneBitmapDestData.class);
                                DestinationDataClass data = new DestinationDataClass();
                                data.setAddress(nondata.getAddress());
                                data.setCategory(nondata.getCategory());
                                data.setDescription(nondata.getDescription());
                                data.setDrable(nondata.getDrable());
                                data.setIndex(nondata.getIndex());
                                data.setLink(getImageBitmap(nondata.getUrl()));
                                data.setName(nondata.getName());
                                data.setPhone(nondata.getPhone());
                                data.setRoadAddress(nondata.getRoadAddress());
                                data.setUrl(nondata.getUrl());
                                data.setX(nondata.getX());
                                data.setY(nondata.getY());
                                Message msg = Message.obtain();
                                msg.obj = data;
                                msg.what = 1;
                                handlerPushMessage.sendMessage(msg);
                            }
                        }.start();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        con = getActivity();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}