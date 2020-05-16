package com.gkftndltek.travelcourceapp.CustMap.FntMyDataList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.CustMap.FntMyDataList.MyPrintDataListFnt.FragmentMyPrintDataList;
import com.gkftndltek.travelcourceapp.CustMap.FntMyDataList.MyPrintMapDataFnt.FragmentMyPrintCource;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_Home.HomeFragment;
import com.gkftndltek.travelcourceapp.CustMap.MainActivity;
import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FragmentMyMapDataList extends Fragment implements MainActivity.OnBackPressedListeners {

    private String key = "l7xx713d4db3b29b418dba74f8af6162f4fb";
    private View rootView;
    private Context con;

    private ImageView ImageView_Menu3,ImageView_GetCource;
    private TextView TextView_MyList_Print,TextView_MyList_MapPrint;

    // 데이터 베이스
    private String token, mapName;
    private FirebaseDatabase database;
    private DatabaseReference users, logined;

    // 프레그먼트

    private HomeFragment homeFragment;
    private FragmentMyPrintDataList fragmentMyPrintDataList;
    private FragmentMyPrintCource fragmentMyPrintCource;

    // 액티비티

    private Activity act;

    // 데이터베이스에서 가져온 목적지 데이터들의 집합



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_mapdata, container, false);
        // 리니어레이아웃
        init();
        return rootView;
    }

    void init() {
        fragmentMyPrintDataList = new FragmentMyPrintDataList();
        fragmentMyPrintCource = new FragmentMyPrintCource();
        act = (MainActivity)getActivity();
        homeFragment = new HomeFragment();
        con = getActivity();

        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        logined = database.getReference("logined");
        token = ((MainActivity)act).token;
        mapName = ((MainActivity)act).mapName;


        TextView_MyList_Print = rootView.findViewById(R.id.TextView_MyList_Print);
        TextView_MyList_MapPrint = rootView.findViewById(R.id.TextView_MyList_MapPrint);

        TextView_MyList_Print.setClickable(true);
        TextView_MyList_MapPrint.setClickable(true);

        TextView_MyList_Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MyList_Fragment, fragmentMyPrintDataList).commit();
            }
        });

        TextView_MyList_MapPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MyList_Fragment, fragmentMyPrintCource).commit();
                // 새로 프레그먼트하나 생성해야함
            }
        });

        ImageView_Menu3 = rootView.findViewById(R.id.ImageView_Menu3);
        //ImageView_GetCource = rootView.findViewById(R.id.ImageView_GetCource);

//        ImageView_GetCource.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 이부분이 정말 중요함...
//            }
//        });
        ImageView_Menu3.setOnClickListener(new View.OnClickListener() { //  메뉴버튼 드러워아웃 나타남
            @Override
            public void onClick(View v) {
                ((MainActivity)act).drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MyList_Fragment, fragmentMyPrintDataList).commit();
    }

    @Override
    public void onBack() {
        DrawerLayout drawer = ((MainActivity)act).drawerLayout;

        if (drawer.isDrawerOpen(Gravity.LEFT)){
            drawer.closeDrawer(Gravity.LEFT);
        }
        else  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, homeFragment).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnBackPressedListener(this); // 프레그먼트
    }
}