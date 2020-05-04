package com.gkftndltek.travelcourceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PersonalMapFragment extends Fragment implements MainActivity.OnBackPressedListeners {

    private String key = "l7xx713d4db3b29b418dba74f8af6162f4fb";
    private View rootView;
    private TMapView tMapView;
    private LinearLayout linearLayoutTmap2;
    private Context con;
    private RoundedLayout Rounded_Layout2; // 레이아웃의 형태를 바꿔줘요
    private ImageView imageView_Confirm2,ImageView_Menu2;
    // 데이터 베이스

    private List <NoneBitmapDestData> XYdata;
    private String token ;
    private FirebaseDatabase database;
    private DatabaseReference users,logined;

    // 마커

    final int[] picture = {R.drawable.o0, R.drawable.o1, R.drawable.o2,
            R.drawable.o3, R.drawable.o4, R.drawable.o5, R.drawable.o6,
            R.drawable.o7, R.drawable.o8, R.drawable.o9}; // 마커 drawble

    /*
    tMapView.addMarkerItem(Integer.toString(locationNaverData.getIndex()), marker);
     */
    private Handler handlerPushMessage = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1) {
                for(int i=0;i<XYdata.size();i++){
                    NoneBitmapDestData data = XYdata.get(i);
                    double x = data.getX(),y = data.getY();
                    TMapMarkerItem marker = new TMapMarkerItem();
                    TMapPoint tMapPoint1 = new TMapPoint(x, y);

                    marker.setIcon(((MainActivity)getActivity()).bitmap[data.getIndex()]);
                    marker.setTMapPoint(tMapPoint1);
                    tMapView.addMarkerItem(Integer.toString(data.getIndex()), marker);

                    if(data.getIndex() == 0) {
                        tMapView.setZoomLevel(14);
                        tMapView.setCenterPoint(data.getY(), data.getX());
                    }
                }
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.personal_map_activity, container, false);
        // 리니어레이아웃
        init();

        tMapView.setSKTMapApiKey(key);
        linearLayoutTmap2.addView(tMapView);

        return rootView;
    }

    void init() {

        XYdata = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        users = database.getReference("users");
        logined = database.getReference("logined");

        token = ((MainActivity)getActivity()).token;

        users.child(token).child("xy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapData : dataSnapshot.getChildren()) {
                    NoneBitmapDestData data = snapData.getValue(NoneBitmapDestData.class);
                    XYdata.add(data);
                }
                Message msg = Message.obtain();
                msg.what = 1;
                handlerPushMessage.sendMessage(msg);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        con = getActivity();
        tMapView = new TMapView(con);

        imageView_Confirm2 = rootView.findViewById(R.id.ImageView_Confirm2);
        ImageView_Menu2 = rootView.findViewById(R.id.ImageView_Menu2);

        ImageView_Menu2.setOnClickListener(new View.OnClickListener() { //  메뉴버튼 드러워아웃 나타남
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        linearLayoutTmap2 = rootView.findViewById(R.id.linearLayoutTmap2);
        Rounded_Layout2 = rootView.findViewById(R.id.Rounded_Layout2);
        Rounded_Layout2.setCornerRadius(100);
        Rounded_Layout2.showBorder(true);  // 경계선 생성
        Rounded_Layout2.setBorderWidth(3);  // 경계선 두께
    }

    @Override
    public void onBack() {
        final MainActivity activity = (MainActivity) getActivity();

        AlertDialog.Builder alert_ex = new AlertDialog.Builder(con);
        alert_ex.setMessage("정말로 종료하시겠습니까?");

        alert_ex.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert_ex.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
            }
        });
        alert_ex.setTitle("Notice");
        AlertDialog alert = alert_ex.create();
        alert.show();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnBackPressedListener(this); // 프레그먼트
    }
}
