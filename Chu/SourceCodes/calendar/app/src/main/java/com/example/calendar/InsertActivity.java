package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

public class InsertActivity extends AppCompatActivity {

    private int REQUEST_CODE = 999;
    private TextView textView1;
    private TextView textView2;
    private ImageView imageView;
    private String address;
    private String category;
    private String url;
    private String user;
    private String mapname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Intent getintent = getIntent();

        user = getintent.getStringExtra("user");
        mapname = getintent.getStringExtra("mapname");

        textView1 = (TextView)findViewById(R.id.Insert_Person_List_Address);
        textView2 = (TextView)findViewById(R.id.Insert_PersonList_Category);
        imageView = (ImageView)findViewById(R.id.ImageView_Person_Picture);

        textView1.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

        Button btn_click = (Button)findViewById(R.id.Insert_button);
        Button btn_search = (Button)findViewById(R.id.Search_item_button);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertActivity.this, SearchActivity.class);
                intent.putExtra("user",user);
                intent.putExtra("mapname",mapname);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edittext1 = (EditText)findViewById(R.id.insert_schedulename);
                EditText edittext2 = (EditText)findViewById(R.id.insert_schedulecontent);
                String schedulename = edittext1.getText().toString();
                String schedulecontent = edittext2.getText().toString();
                ScheduleData data2 = new ScheduleData(schedulename,schedulecontent);

                System.out.println(data2);
                System.out.println(address);
                System.out.println(category);
                Intent intent2 = new Intent();
                intent2.putExtra("result", data2);
                intent2.putExtra("address",address);
                intent2.putExtra("category",category);
                intent2.putExtra("url",url);
                setResult(RESULT_OK,intent2);
                finish();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
        }

        if(requestCode==REQUEST_CODE){
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);

            address = data.getStringExtra("address");
            category = data.getStringExtra("category");
            url=data.getStringExtra("url");
            System.out.println(address);
            System.out.println(category);
            textView1.setText(address);
            textView2.setText(category);
            Glide.with(this).load(url).into(imageView);


        }
    }
}

