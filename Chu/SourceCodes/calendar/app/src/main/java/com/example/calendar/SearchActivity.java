package com.example.calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private SearchRecyclerviewAdapter Srecyclerviewadapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<NoneBitmapDestData> arrayList;
    private DatabaseReference mDatabase;
    private ImageView imageView;
    private Bitmap bitmapsample;
    Handler handler = new Handler();
    private String user;
    private String mapname;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.search_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        Srecyclerviewadapter = new SearchRecyclerviewAdapter(arrayList, this);
        recyclerView.setAdapter(Srecyclerviewadapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
/*
        NoneBitmapDestData obj = new NoneBitmapDestData();
        obj.setName("chu");
        obj.setAddress("two");
        obj.setRoadAddress("three");
        obj.setPhone("four");
        obj.setCategory("five");
        obj.setDescription("six");
        obj.setUrl("https://cdn.pixabay.com/photo/2017/03/25/17/55/color-2174045_960_720.png");
        obj.setX(8.888);
        obj.setY(9.999);
        obj.setDrable(10);
        obj.setIndex(11);

 */


        //Bitmap bitmap = getImageBitmap(obj.getUrl());
        /* 왜 안될까?
        String imgurl = ("http://imgnews.naver.net/image/5353/2017/10/19/0000254684_001_20171019134723287.jpg");
        Bitmap bitmapsample = getImageBitmap(imgurl);
        imageView = (ImageView) findViewById(R.id.search_imageview);
        try {
            imageView.setImageBitmap(bitmapsample);
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
        /* 왜 안될까?
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //
                try{

                    final ImageView iv = (ImageView)findViewById(R.id.search_imageview);
                    URL url = new URL("http://developer.android.com/assets/images/android_logo.png");
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            iv.setImageBitmap(bm);
                        }
                    });
                    iv.setImageBitmap(bm);
                } catch(Exception e){

                }

            }
        });
        t.start();

         */
        /* 이건되네?
        imageView = findViewById(R.id.search_imageview);
        String imageUrl = "https://cdn.pixabay.com/photo/2017/03/25/17/55/color-2174045_960_720.png";
        Glide.with(this).load(imageUrl).into(imageView);
         */







/*
        mDatabase.child("users").child("chu naver com").child("maps").child("77").child("mylist").child("dadsfasdfasfd").setValue(obj);
        mDatabase.child("users").child("chu naver com").child("maps").child("77").child("mylist").child("vwrva").setValue(obj);

 */


        Intent intent = getIntent();
        user=intent.getStringExtra("user");
        mapname=intent.getStringExtra("mapname");


        mDatabase.child("users").child(user).child("maps").child(mapname).child("mylist").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {

                            arrayList.add(Snapshot.getValue(NoneBitmapDestData.class));
                        }
                        Srecyclerviewadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("Error");
                    }
                }
        );

        Srecyclerviewadapter.setOnItemClickListener(new SearchRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                Intent intent = new Intent(SearchActivity.this, InsertActivity.class);
                intent.putExtra("address", arrayList.get(pos).getAddress());
                intent.putExtra("category", arrayList.get(pos).getCategory());
                intent.putExtra("url",arrayList.get(pos).getUrl());
                System.out.println(arrayList.get(pos).getAddress());
                System.out.println(arrayList.get(pos).getCategory());
                setResult(RESULT_OK,intent);
                finish();

            }
        });

    }
    /* 보류
    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {

        }
        return bm;
    }
     */
}
