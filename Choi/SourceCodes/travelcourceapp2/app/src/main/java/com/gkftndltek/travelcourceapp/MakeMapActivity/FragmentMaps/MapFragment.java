package com.gkftndltek.travelcourceapp.MakeMapActivity.FragmentMaps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.CustMap.MainActivity;
import com.gkftndltek.travelcourceapp.MakeMapActivity.MakeMapAct;
import com.gkftndltek.travelcourceapp.MakeMapActivity.firstRecyclerView.MakeMapClass;
import com.gkftndltek.travelcourceapp.MakeMapActivity.firstRecyclerView.MakeMapData;
import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements MakeMapAct.OnBackPressedListeners {

    private Context con;
    private View rootView;
    private MakeMapClass makeMapClass;
    private GestureDetector gestureDetector;

    // 데이터 베이스

    private String token;
    private FirebaseDatabase database;
    private DatabaseReference users, logined;

    // 액티비티

    private Activity act;

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) { // 네이버 검색했다
                List<MakeMapData> mapData = (List< MakeMapData>)msg.obj;
                for(int i=0;i<mapData.size();i++){
                    System.out.println(mapData.get(i).getMapName());
                    if(mapData.get(i) == null){
                        System.out.println("너 정말 null 이야 ? " + mapData.get(i));
                    }
                    makeMapClass.add(mapData.get(i));
                }
            }
            else if(msg.what == 2){
                MakeMapData data = (MakeMapData) msg.obj;
                Intent intent = new Intent((MakeMapAct)getActivity(), MainActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("name",data.getMapName());
                startActivity(intent);
                // 버그 생길 수 있음
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map_map_fragment, container, false);
        init();
        return rootView;
    }

    void init() {
        con = getActivity();

        act = ((MakeMapAct) getActivity());
        makeMapClass = new MakeMapClass();
        makeMapClass.execute(rootView,con,handlerPushMessage);

        token =  ((MakeMapAct)act).token;

        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        logined = database.getReference("logined");

        users.child(token).child("maps").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List< MakeMapData> mapData = new ArrayList<>();
                for (DataSnapshot snapData : dataSnapshot.getChildren()) {
                    MakeMapData data = snapData.getValue(MakeMapData.class);
                    mapData.add(data);
                }

                Message msg = Message.obtain();
                msg.obj= mapData;
                msg.what = 1;
                handlerPushMessage.sendMessage(msg);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBack() {
        final MakeMapAct activity = (MakeMapAct) getActivity();

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
        ((MakeMapAct) context).setOnBackPressedListener(this); // 프레그먼트
    }
}
