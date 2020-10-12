package com.example.coooooment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MainData> arrayList;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        mainAdapter = new MainAdapter(arrayList, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() != null) {
                    int t = (int) view.getTag();

                    Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                    startActivityForResult(intent, 1);

                }
            }
        });
        recyclerView.setAdapter(mainAdapter);

        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData mainData = new MainData(R.mipmap.ic_launcher, "안녕", "리사이클러뷰");
                arrayList.add(mainData);
                mainAdapter.notifyDataSetChanged();
            }
        });
    }
}
