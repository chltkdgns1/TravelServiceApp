package com.gkftndltek.travelcourceapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gkftndltek.travelcourceapp.Fragment_Home.HomeFragment;
import com.gkftndltek.travelcourceapp.Fragment_PersonalMap.PersonalMapFragment;
import com.gkftndltek.travelcourceapp.Login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    private NavigationView nav_view;
    private HomeFragment homeFragment;
    private PersonalMapFragment personalMapFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public String token;

    // 마커
    public Bitmap[] bitmap; // 마커들

    public final int[] picture = {R.drawable.o0, R.drawable.o1, R.drawable.o2,
            R.drawable.o3, R.drawable.o4, R.drawable.o5, R.drawable.o6,
            R.drawable.o7, R.drawable.o8, R.drawable.o9};

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

        bitmap = new Bitmap[10];
        for (int i = 0; i < picture.length; i++) {
            bitmap[i] = BitmapFactory.decodeResource(getResources(), picture[i]);
        }

        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token");
        System.out.println(token);
        drawerLayout = findViewById(R.id.drawer);

        homeFragment = new HomeFragment();
        personalMapFragment = new PersonalMapFragment();

        nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        //fragmentManager = getSupportFragmentManager();
        //fragmentTransaction = fragmentManager.beginTransaction();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, homeFragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_person_map) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,personalMapFragment).commit();
        }
        else if(id == R.id.nav_home){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,homeFragment).commit();
        }

        else if(id == R.id.nav_logout){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("logout", true);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

