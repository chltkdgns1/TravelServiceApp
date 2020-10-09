package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NextActivity extends AppCompatActivity {


    private TextView textView_date;
    private ArrayList<ScheduleData> arrayList;
    private RecyclerviewAdapter recyclerviewadapter;
    private RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private int REQUEST_CODE = 482;
    private int REQUEST_CODE2 = 334;
    private String default_user = "gydbslove2 naver com";  //default_user와 default_mapname을 받아와서 입력시키면됨
    private String dafault_mapName="1312321";
    private DatabaseReference mDatabase;

    String db_year;
    String db_month;
    String db_day;
    String chosen_date;
    String date;

    ItemTouchHelper helper;

    SimpleDateFormat format;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        init();
    }

    void init(){

        final Intent intent = getIntent();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Data data1 = (Data) intent.getSerializableExtra("Data");
        textView_date = findViewById(R.id.date);
        textView_date.setText(data1.getData_year() + "년 " + data1.getData_month() + "월 " + data1.getData_day() + "일 일정");
        db_year = Integer.toString(data1.getData_year());
        db_month = Integer.toString(data1.getData_month());
        db_day = Integer.toString(data1.getData_day());

        date = (db_year + cvt(db_month) + cvt(db_day));

        format = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        recyclerviewadapter = new RecyclerviewAdapter(arrayList,default_user,dafault_mapName,date);
        recyclerview.setAdapter(recyclerviewadapter);

        mDatabase.child("users").child(default_user).child("maps").child(dafault_mapName).child("manage").child(date).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dbSnapshot : dataSnapshot.getChildren()) {

                            arrayList.add(dbSnapshot.getValue(ScheduleData.class));
                        }
                        recyclerviewadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("Error");
                    }
                }
        );

        helper = new ItemTouchHelper(new ItemTouchHelperCallback(recyclerviewadapter));
        helper.attachToRecyclerView(recyclerview);

        recyclerviewadapter.setOnItemClickListener(new RecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent3 = new Intent(NextActivity.this, ItemshowActivity.class);
                intent3.putExtra("name", arrayList.get(position).getTextview_schedulename());
                intent3.putExtra("content", arrayList.get(position).getTextview_schedulecontent());
                intent3.putExtra("position", position);
                System.out.println(arrayList.get(position).getAddress());
                intent3.putExtra("address",arrayList.get(position).getAddress());
                intent3.putExtra("category",arrayList.get(position).getCategory());
                intent3.putExtra("url",arrayList.get(position).getUrl());
                startActivityForResult(intent3, REQUEST_CODE2);
            }
        });

        Button btn_add = (Button) findViewById(R.id.add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(NextActivity.this, InsertActivity.class);
                intent2.putExtra("user",default_user);
                intent2.putExtra("mapname",dafault_mapName);
                startActivityForResult(intent2, REQUEST_CODE);
            }
        });
    }

    String cvt(String time) {
        if (time.length() == 1) return "0" + time;
        return time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
        }

        if(data == null) return;

        if (requestCode == REQUEST_CODE) { // 인풋

            ScheduleData data2 = (ScheduleData) data.getSerializableExtra("result");

       //  데이터를 넣고 firebase 들어가죠

            long now = System.currentTimeMillis();
            Date dates = new Date(now);
            String formatDate = format.format(dates);
            String address = data.getStringExtra("address");
            String category = data.getStringExtra("category");
            String url = data.getStringExtra("url");
            System.out.println("-------------------");
            System.out.println(address);
            System.out.println(category);

            data2.setAddress(address);
            data2.setCategory(category);
            data2.setUrl(url);
            data2.setKey(formatDate);
            arrayList.add(data2);

            // key formatDate
            int pos = arrayList.indexOf(data2);
            recyclerviewadapter.notifyDataSetChanged();
            mDatabase.child("users").child(default_user).child("maps").child(dafault_mapName).child("manage").child(date).child(formatDate).setValue(data2);
        }

        else if (requestCode == REQUEST_CODE2) { // 수정
            ScheduleData data3 = (ScheduleData) data.getSerializableExtra("result2");
            int pos = (int) data.getSerializableExtra("position");

            arrayList.get(pos).setTextview_schedulecontent(data3.getTextview_schedulecontent());
            arrayList.get(pos).setTextview_schedulename(data3.getTextview_schedulename());
            recyclerviewadapter.notifyDataSetChanged();

            mDatabase.child("users").child(default_user).child("maps").child(dafault_mapName).child("manage").child(date).child(arrayList.get(pos).getKey()).setValue(arrayList.get(pos));
        }
    }
}


