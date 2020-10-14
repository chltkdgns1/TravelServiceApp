package com.gkftndltek.travelcourceapp.MakeMapActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.Login.LoginActivity;
import com.gkftndltek.travelcourceapp.MakeMapActivity.FntMakeMaps.MakeMapsFragment;
import com.gkftndltek.travelcourceapp.MakeMapActivity.FragmentMaps.MapFragment;
import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;

public class MakeMapAct extends AppCompatActivity {

    public OnBackPressedListeners mBackListener; // 백프레스

    // 프레그먼트
    private MapFragment mapFragment;
    private MakeMapsFragment makeMapsFragment;

    // 유아이

    private ImageView ImageView_Withdrawal,ImageView_MakeMap,ImageView_Home,ImageView_Logout;
    private TextView TextView_Sns;
    // 파이어베이스
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        init();
    }

    void init(){

        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token");

        makeMapsFragment = new MakeMapsFragment();
        mapFragment = new MapFragment();

        ImageView_Home = findViewById(R.id.ImageView_Home);
        ImageView_Withdrawal = findViewById(R.id.ImageView_Withdrawal);
       // ImageView_Set = findViewById(R.id.ImageView_Set);
        ImageView_MakeMap = findViewById(R.id.ImageView_MakeMap);
        ImageView_Logout = findViewById(R.id.ImageView_Logout);
        TextView_Sns = findViewById(R.id.TextView_Sns);

        TextView_Sns.setClickable(true);
        ImageView_Home.setClickable(true);
        ImageView_Withdrawal.setClickable(true);
        ImageView_MakeMap.setClickable(true);
        ImageView_Logout.setClickable(true);

        TextView_Sns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeMapAct.this, MainActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        ImageView_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_make_map, mapFragment).commit();
            }
        });

        ImageView_Withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 개인정보 아직 미정
            }
        });

        ImageView_MakeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 맵 만들기
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_make_map, makeMapsFragment).commit();
            }
        });

        ImageView_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeMapAct.this, LoginActivity.class);
                intent.putExtra("logout", true);
                startActivity(intent);
                finish();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_make_map, mapFragment).commit();
    }

    public interface OnBackPressedListeners {
        public void onBack();
    }

    // 리스너 설정 메소드
    public void setOnBackPressedListener(OnBackPressedListeners listener) { // this -> 프레먼트  -> 액티비티
        System.out.println("들어오는거맞음?");
        // 어느 액티비티에서 눌렸느냐?
        mBackListener = listener; // 프레그먼트
        // 액티비티
    }

    // 뒤로가기 버튼을 눌렀을 때의 오버라이드 메소드

    @Override
    public void onBackPressed() { // 기존의 액티비티
        // 다른 Fragment 에서 리스너를 설정했을 때 처리됩니다.
        if(mBackListener != null) {
            mBackListener.onBack();
        } else {
            AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
            alert_ex.setMessage("정말로 종료하시겠습니까?");

            alert_ex.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert_ex.setNegativeButton("종료", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            alert_ex.setTitle("Notice");
            AlertDialog alert = alert_ex.create();
            alert.show();
        }
    }
}
