package com.gkftndltek.travelcourceapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;

    // 리스너 객체 생성


    // 우리가 사용하는 UI 들은 액티비티 위에 있죠
    // 액티
   public OnBackPressedListeners mBackListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    void init(){
        drawerLayout = findViewById(R.id.drawer);

        HomeFragment home = new HomeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.main_frame, home);
        fragmentTransaction.commit();
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
            // 프레그먼트에 접근할 수 있으니 실행이된다.
            System.out.println("백키 눌렀을 때");
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

