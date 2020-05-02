package com.gkftndltek.travelcourceapp;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;

    // 리스너 객체 생성
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
    public void setOnBackPressedListener(OnBackPressedListeners listener) {
        System.out.println("들어오는거맞음?");
        mBackListener = listener;
    }

    // 뒤로가기 버튼을 눌렀을 때의 오버라이드 메소드
    @Override
    public void onBackPressed() {
        // 다른 Fragment 에서 리스너를 설정했을 때 처리됩니다.
        if(mBackListener != null) {
            mBackListener.onBack();
            System.out.println("백키 눌렀을 때");
        } else {
            System.out.println("노이루");
            //super.onBackPressed();
        }
    }
}

