    package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemshowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        final Intent intent = getIntent();

        String name = intent.getExtras().getString("name");
        final String content = intent.getExtras().getString("content");
        final int position = intent.getExtras().getInt("position");
        String address = intent.getStringExtra("address");
        String category = intent.getStringExtra("category");
        String url = intent.getStringExtra("url");

        System.out.println(name);
        System.out.println(content);
        System.out.println("---------------");
        System.out.println(address);
        System.out.println(category);
        TextView textView1 = findViewById(R.id.Show_Person_List_Address);
        TextView textView2 = findViewById(R.id.Show_PersonList_Category);
        ImageView imageView = findViewById(R.id.show_PersonList_url);
        textView1.setText(address);
        textView2.setText(category);
        Glide.with(this).load(url).into(imageView);

        final EditText editText1 = (EditText) findViewById(R.id.et_schedulename);
        final EditText editText2 = (EditText) findViewById(R.id.et_schedulecontent);
        editText1.setText(name);
        editText2.setText(content);

//        editText1.setHint(name);
//        editText2.setHint(content);
        Button btn_change = (Button)findViewById(R.id.change_button);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String changename = editText1.getText().toString();
                String changecontent = editText2.getText().toString();
                ScheduleData data3 = new ScheduleData(changename,changecontent);
                System.out.println(data3);
                Intent intent2 = new Intent();
                intent2.putExtra("result2", data3);
                intent2.putExtra("position",position);
                setResult(RESULT_OK,intent2);
                finish();
            }
        });



    }

}
