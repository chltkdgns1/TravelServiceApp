package com.gkftndltek.travelcourceapp.RealHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.R;

public class MyData extends AppCompatActivity implements View.OnClickListener {


    // 인텐트 데이터
    private String token, nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_home_my_data_service);

        init();
    }

    public void init() {
        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token");
        nick = (String) bun.get("nick");

    }

    @Override
    public void onClick(View v) {

    }
}
