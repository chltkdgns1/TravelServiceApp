package com.gkftndltek.travelcourceapp.Sns.Options;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.Options.FragmentFollow.FragmentFollow;


public class OptionActivity extends AppCompatActivity {


    // 뷰

    private TextView TextView_Nomal_Follow;


    // 프레그먼트

    private FragmentFollow fragmentFollow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_sns_option);


        init();
    }

    void init(){

        fragmentFollow = new FragmentFollow();

        TextView_Nomal_Follow = findViewById(R.id.TextView_Nomal_Follow);

        TextView_Nomal_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout_Options,fragmentFollow).commit();
            }
        });
    }
}
