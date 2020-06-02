package com.gkftndltek.travelcourceapp.CustMap.FntMyDataList.MyPrintMapDataFnt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.CustMap.Fragment_Home.naverSearchLocationData;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.NoneBitmapDestData;
import com.gkftndltek.travelcourceapp.CustMap.MainActivity;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentMyPrintCource extends Fragment {

    private String key = "l7xx713d4db3b29b418dba74f8af6162f4fb";
    private View rootView;
    private Context con;
    private TMapView tMapView;

    private Random rand;
    private LinearLayout My_Travel_Cource;

    // 데이터 베이스
    private String token, mapName;
    private FirebaseDatabase database;
    private DatabaseReference users, logined;

    // 액티비티

    private Activity act;

    // 데이터베이스에서 가져온 목적지 데이터들의 집합

    public List<DestinationDataClass> destData;

    // 컬러

    private int[][] dp; //
    private int[][] cost;
    private int p[][];
    private int []pass;

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                System.out.println("여기 들어옵니까???");
                getCource();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_print_cource, container, false);
        // 리니어레이아웃

        init();
        System.out.println("사이즈좀 볼까 ㅎㅎ Dest : " + destData.size());
        tMapView.setZoomLevel(7);
        tMapView.setSKTMapApiKey(key);
        My_Travel_Cource.addView(tMapView);
        getAtoBLine();
        return rootView;
    }

    void init() {
        dp = new int[(1 << 12)][13];
        p = new int[(1 << 12)][13];
        cost = new int[20][20];
        rand = new Random();
        con = getActivity();
        tMapView = new TMapView(con);
        My_Travel_Cource = rootView.findViewById(R.id.My_Travel_Cource);

        act = (MainActivity) getActivity();

        destData = ((MainActivity) act).DestSum;

//        database = FirebaseDatabase.getInstance();
//        users = database.getReference("users");
//        logined = database.getReference("logined");
//
//        token = ((MainActivity)act).token;
//        mapName = ((MainActivity)act).mapName;


    }


    void getCource() {

        int n = destData.size();

        for (int i = 1; i <= n; i++) {
            for (int k = 1; k <= n; k++) {
                System.out.print(cost[i][k] + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < (1 << n); i++) {
            for (int k = 1; k <= n; k++) dp[i][k] = 1000000000;
        }

        dp[0][1] = 0;
        for (int i = 0; i < (1 << n); i++) {
            for (int k = 1; k <= n; k++) {
                if(dp[i][k] != 1000000000){
                    for(int z=1;z<=n;z++){
                        if(k == z) continue;
                        if(cost[k][z] == 0) continue;
                        if((i & (1<<(z - 1))) == 1) continue;

                        int t = (i | (1<<(z - 1)));

                        if(dp[t][z] > dp[i][k] + cost[k][z]){
                            dp[t][z] = dp[i][k] + cost[k][z];
                            p[t][z] = k;
                        }
                    }
                }
            }
        }

        double ans = dp[(1<<n) - 1][1];  // 결과
        System.out.println(ans);

        pass = new int[n + 2];
        int idx = 0;

        int t = 1;
        int u = (1 << n) - 1;

        while (t != 0) {
            pass[idx++] = t;
            int r = t;
            t = p[u][t];
            u -= (1 << (r - 1));
        }

        for(int i=0;i<idx - 1;i++){
            int xx = pass[i] - 1;
            int yy = pass[i + 1] - 1;
            System.out.println(xx + " - > " + yy);
        }

        final int index = idx;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(index);
                    for(int i=0;i<index - 1;i++){
                        int u = pass[i] - 1;
                        int r = pass[i + 1] - 1;
                        TMapPoint a = new TMapPoint(destData.get(u).getX(), destData.get(u).getY());
                        TMapPoint b = new TMapPoint(destData.get(r).getX(), destData.get(r).getY());
                        Thread.sleep(300);
                        TMapPolyLine tMapPolyLine = new TMapData().findPathData(a, b);
                        tMapPolyLine.setLineColor(Color.BLUE);
                        tMapPolyLine.setLineWidth(3);
                        tMapView.addTMapPolyLine(Integer.toString(i + 1), tMapPolyLine);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int min(int a, int b) {
        return a < b ? a : b;
    }

    void getAtoBLine() {

        for (int i = 0; i < destData.size(); i++) {
            TMapMarkerItem marker = new TMapMarkerItem();
            TMapPoint tMapPoint1 = new TMapPoint(destData.get(i).getX(), destData.get(i).getY());
            marker.setIcon((((MainActivity) act)).bitmap[i % 10]);
            marker.setTMapPoint(tMapPoint1);
            tMapView.addMarkerItem(Integer.toString(i), marker);
        }
        synchronized (this) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < destData.size() - 1; i++) {
                            TMapPoint a = new TMapPoint(destData.get(i).getX(), destData.get(i).getY());
                            for (int k = i + 1; k < destData.size(); k++) {
                                TMapPoint b = new TMapPoint(destData.get(k).getX(), destData.get(k).getY());

//                                  final String id = Integer.toString((i + 1) * 1000 + k);
                                if (a.mKatecLon == b.mKatecLon && a.mKatecLat == b.mKatecLat)
                                    continue;

                                final int x = k, y = i;
//                              int rr = (rand.nextInt() % 255 + 1),gg =(rand.nextInt() % 255 + 1),bb =(rand.nextInt() % 255 + 1);
//                              System.out.println("RR : " + rr + " GG : " + gg + " BB : " + bb);

//                                System.out.println(destData.get(i).getX() + " , " + destData.get(i).getY());
//                                System.out.println(destData.get(k).getX() + " , " + destData.get(k).getY());
                                Thread.sleep(300);
                                TMapPolyLine tMapPolyLine = new TMapData().findPathData(a, b);
//                                System.out.println("비드비드");
                                cost[y + 1][x + 1] = (int)tMapPolyLine.getDistance();
                                cost[x + 1][y + 1] = cost[y + 1][x + 1];

                                if(cost[y + 1][x + 1] == 0){
                                    System.out.println(cost[y + 1][x + 1]);
                                    Toast.makeText(con,"에러가 발생했습니다. 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                                    return;
                                }

//                              tMapPolyLine.setLineColor(Color.rgb(rr,gg,bb));
//                              tMapPolyLine.setLineWidth(20);
//                              tMapView.addTMapPolyLine(id, tMapPolyLine);
                            }
                        }

                        Message msg = Message.obtain();
                        msg.what = 1;
                        handlerPushMessage.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}