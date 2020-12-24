package com.gkftndltek.travelcourceapp.CustMap.Calender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler.CalenderDataClass;
import com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler.CalenderRecyclerView;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.NoneBitmapDestData;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalenderListActivity  extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref;

    private CalenderRecyclerView calenderRecyclerView;
    private Context con;

    private ImageView ImageView_CalenderView_Add;

    private String token,name;
    private DateClass data;

    private String stringDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_list);

        init();
    }

    void init(){
        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token"); // 유저네임
        name = (String)bun.get("name");   // 맵 네임
        data = (DateClass) bun.get("date");

        String day =  cvt(Integer.toString(data.getDay()));
        String year = Integer.toString(data.getYear());
        String month = cvt(Integer.toString((data.getMonth() + 1)));

        stringDate = year + month + day;

        con = getApplicationContext();
        calenderRecyclerView = new CalenderRecyclerView();
        calenderRecyclerView.execute(this, con,token,name,stringDate);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        ref.child("users").child(token).child("maps").child(name).child("manage").child(stringDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    CalenderDataClass cdata = data.getValue(CalenderDataClass.class);
                    System.out.println("여기 들어는가지?");

                    System.out.println("네임 : " + cdata.getName());

                    calenderRecyclerView.add(cdata);
                    System.out.println(calenderRecyclerView.getSize());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageView_CalenderView_Add = findViewById(R.id.ImageView_CalenderView_Add);

        ImageView_CalenderView_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalenderListActivity.this,CalenderInsertActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("token",token);
                startActivityForResult(intent,1);
            }
        });
    }


    private String cvt(String data){
        if(data.length() == 1) return "0" + data;
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== 1){ // 추가ㅣ
            if(data != null) {
                Bundle bun = data.getExtras();
                String names = (String) bun.get("name");
                String content = (String)bun.get("content");
                String address = (String) bun.get("address");
                String category = (String) bun.get("category");
                String image = (String) bun.get("image");

                CalenderDataClass cdata = new CalenderDataClass();
                cdata.setContent(content);
                cdata.setName(names);
                cdata.setAddress(address);
                cdata.setCategory(category);
                cdata.setUrl(image);

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String formatDate = sdfNow.format(date);

                cdata.setKey(formatDate);

                ref.child("users").child(token).child("maps").child(name).child("manage").child(stringDate).child(cdata.getKey()).setValue(cdata);
                calenderRecyclerView.add(cdata);
            }
        }
        else if(requestCode == 2){ // 수정
            if(data != null){
                Bundle bun = data.getExtras();
                CalenderDataClass cdata = (CalenderDataClass) bun.get("data");

                ref.child("users").child(token).child("maps").child(name).child("manage").child(stringDate).child(cdata.getKey()).setValue(cdata);
                calenderRecyclerView.changeData(cdata);
              //  calenderRecyclerView.add(cdata);
            }
        }
    }

}
