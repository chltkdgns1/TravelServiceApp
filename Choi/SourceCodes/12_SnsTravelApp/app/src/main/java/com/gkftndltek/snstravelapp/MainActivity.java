package com.gkftndltek.snstravelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private ClinetRecyclerClass clinetRecyclerClass;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private Button Button_Post_Add;

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    void init(){
        Button_Post_Add = findViewById(R.id.Button_Post_Add);
        Button_Post_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PostUploadActivity.class);
                startActivityForResult(intent , 0);
            }
        });

        clinetRecyclerClass = new ClinetRecyclerClass();
        clinetRecyclerClass.execute(this,getApplicationContext(),handlerPushMessage);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("post");

        clinetRecyclerClass.clear();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    PostData data = snapshot.getValue(PostData.class);

                    BitmapPostData bdata = new BitmapPostData();
                    bdata.setContents(data.getContents());
                    bdata.setProfile(data.getProfile());
                    bdata.setTitle(data.getTitle());
                    bdata.setUsername(data.getUsername());

                   /*
                     if(bdata.getProfile() != null) {
                        System.out.println(bdata.getProfile());
                        bdata.setUrl(getImageBitmap(bdata.getProfile()));
                    }
                    */
                    clinetRecyclerClass.add(bdata);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1){
            //(PostData) data.getSerializableExtra("data");

            String title,contents,username,filepath;

            title = data.getExtras().getString("Title");
            contents = data.getExtras().getString("Contents");
            username = data.getExtras().getString("Username");
            filepath = data.getExtras().getString("filePath");

            PostData pdata = new PostData();
            pdata.setProfile(filepath);
            pdata.setUsername(username);
            pdata.setContents(contents);
            pdata.setTitle(title);

            databaseReference.push().setValue(pdata);



            BitmapPostData bdata = new BitmapPostData();
            bdata.setContents(contents);
            bdata.setProfile(filepath);
            bdata.setTitle(title);
            bdata.setUsername(username);


          /*
            if(bdata.getProfile() != null) {
                System.out.println(bdata.getProfile());
                bdata.setUrl(getImageBitmap(bdata.getProfile()));
            }

           */

            clinetRecyclerClass.add(bdata);
        }
        else return;
    }
}
