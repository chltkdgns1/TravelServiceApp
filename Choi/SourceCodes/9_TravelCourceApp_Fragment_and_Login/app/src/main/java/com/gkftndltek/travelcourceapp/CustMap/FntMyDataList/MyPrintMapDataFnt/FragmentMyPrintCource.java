package com.gkftndltek.travelcourceapp.CustMap.FntMyDataList.MyPrintMapDataFnt;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.CustMap.MainActivity;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

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

    private double[][] dp; //
    private double[][] cost;
    private int p[][];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_print_cource, container, false);
        // 리니어레이아웃

        init();
        System.out.println("사이즈좀 볼까 ㅎㅎ Dest : " + destData.size());
        tMapView.setSKTMapApiKey(key);
        My_Travel_Cource.addView(tMapView);
        // tMapView.setZoomLevel(7);
        // getAtoBLine();
        return rootView;
    }

    void init() {
        dp = new double[(1 << 12)][13];
        p = new int[(1 << 12)][13];

        cost = new double[13][13];
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
        for (int i = 1; i < (1 << n); i++) {
            for (int k = 1; k <= n; k++) dp[i][k] = 1e9;
        }

        dp[0][1] = 0;
        for (int i = 0; i < (1 << n); i++) {
            for (int k = 1; k <= n; k++) {
                if(dp[i][k] != 1e9){
                    for(int z=1;z<=n;z++){
                        if(k == z) continue;

                        if((i & (1<<z)) == 1) continue;

                        int t = (i | (1<<z));

                        dp[t][z] = max(dp[t][z],dp[i][k] + cost[k][z]);
                        p[t][z] = k;
                    }
                }
            }
        }

        double ans = dp[(1<<n) - 1][1];  // 결과

    }

    private double max(double a,double b) {
        return a < b ? b : a;
     }

    void getAtoBLine() {

        for (int i = 0; i < destData.size(); i++) {
            TMapMarkerItem marker = new TMapMarkerItem();
            TMapPoint tMapPoint1 = new TMapPoint(destData.get(i).getX(), destData.get(i).getY());
            marker.setIcon((((MainActivity) act)).bitmap[i % 10]);
            marker.setTMapPoint(tMapPoint1);
            tMapView.addMarkerItem(Integer.toString(i), marker);
        }

        for (int i = 0; i < destData.size() - 1; i++) {
            final TMapPoint a = new TMapPoint(destData.get(i).getX(), destData.get(i).getY());
            for (int k = i + 1; k < destData.size(); k++) {
                final TMapPoint b = new TMapPoint(destData.get(k).getX(), destData.get(k).getY());

                final String id = Integer.toString((i + 1) * 1000 + k);
                if (a.mKatecLon == b.mKatecLon && a.mKatecLat == b.mKatecLat) continue;

                final int x = k,y = i;

                synchronized (this) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                int rr = (rand.nextInt() % 255 + 1),gg =(rand.nextInt() % 255 + 1),bb =(rand.nextInt() % 255 + 1);
//                                System.out.println("RR : " + rr + " GG : " + gg + " BB : " + bb);
                                TMapPolyLine tMapPolyLine = new TMapData().findPathData(a, b);
                                cost[y][x] = tMapPolyLine.getDistance();
                                cost[x][y] = cost[y][x];
//                                tMapPolyLine.setLineColor(Color.rgb(rr,gg,bb));
//                                tMapPolyLine.setLineWidth(20);
//                                tMapView.addTMapPolyLine(id, tMapPolyLine);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}