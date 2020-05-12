package com.example.sns;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러 성능 향상? 좀더 알아봐야함
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>(); //어댑터로


        final EditText edittext=(EditText)findViewById(R.id.edittext);

        Button button = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.btn_database);
        Button button3 = (Button)findViewById(R.id.find_database);
        final TextView textView=(TextView)findViewById(R.id.textview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(edittext.getText());
                String value=edittext.getText().toString();
                System.out.println(value);
            }
        });


        database = FirebaseDatabase.getInstance();
        databaseReference=database.getReference("User"); // DB테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는곳
                arrayList.clear(); //기존 리스트 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //반복문으로 데이터 list 추출
                    User user = snapshot.getValue(User.class);
                    //만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(user); //담은 데이터를 배열리스트에 넣고 리사이클러뷰에 보낼 준비
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //에러 발생시?
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
                //에러 출력
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            database = FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
                Query query = databaseReference.child("User").orderByChild("location").equalTo("japan");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            //만들어뒀던 User 객체에 데이터를 담는다.
                            arrayList.add(user); //담은 데이터를 배열리스트에 넣고 리사이클러뷰에 보낼 준비
                        }
                        adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //에러 발생시?
                        Log.e("MainActivity", String.valueOf(databaseError.toException()));
                        //에러 출력
                    }
                });
            }
        });



        adapter = new CustomAdapter(arrayList,this);
        recyclerView.setAdapter(adapter); //리사이클러뷰 어댑터 연결결


    }



}


