package com.chu.hihi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    Button btn_Frag1,btn_Frag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Frag1 = (Button) findViewById(R.id.btn_Frag1);
        btn_Frag2 = (Button) findViewById(R.id.btn_Frag2);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView=(View)findViewById(R.id.drawer);

        btn_Frag1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                Fragment1 fragment1 = new Fragment1();
                transaction.replace(R.id.frame,fragment1);
                transaction.commit();
            }
        });

        btn_Frag2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                Fragment2 fragment2 = new Fragment2();
                transaction.replace(R.id.frame,fragment2);
                transaction.commit();
            }
        });

        Button btn_open=(Button)findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 drawerLayout.openDrawer(drawerView);
            }
        });

        Button btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        Button btn_chuMenuFrag1 = (Button)findViewById(R.id.btn_chuMenuFrag1);
        btn_chuMenuFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                Fragment1 fragment1 = new Fragment1();
                transaction.replace(R.id.frame,fragment1);
                transaction.commit();
            }
        });

        Button btn_TestMenuFrag2 = (Button)findViewById(R.id.btn_TestMenuFrag2);
        btn_TestMenuFrag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                Fragment2 fragment2 = new Fragment2();
                transaction.replace(R.id.frame,fragment2);
                transaction.commit();
            }
        });


        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    DrawerLayout.DrawerListener listener= new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
