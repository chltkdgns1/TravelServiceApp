package com.gkftndltek.travelcourceapp.Sns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.HomeFragment.HomeFragments;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.MeFragments;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.PostData;
import com.gkftndltek.travelcourceapp.Sns.SaveFragment.SaveFragments;
import com.gkftndltek.travelcourceapp.Sns.SearchFragment.SearchFragemnts;
import com.gkftndltek.travelcourceapp.Sns.UploadPost.PostUploadActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // 리사이클러 뷰

   // private PostRecyclerClass clinetRecyclerClass;

    // 데이터 베이스
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference fref;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    // View

    private ImageView ImageView_Post_Add, ImageView_Home;
    private ImageView ImageView_Post_Search,ImageView_Post_like,ImageView_Me;

    // 시간

    private SimpleDateFormat formatter;

    //

    public String token,nick;

    // 프레그먼트

    HomeFragments homeFragments;
    SearchFragemnts searchFragemnts;
    SaveFragments saveFragments;
    MeFragments meFragments;
    //
    private InputMethodManager imm;

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_main);
        init();
    }

    void init() {


        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token");
        nick = (String) bun.get("nick");


        homeFragments = new HomeFragments();
        searchFragemnts = new SearchFragemnts();
        saveFragments = new SaveFragments();
        meFragments = new MeFragments();

        ImageView_Home = findViewById(R.id.ImageView_Home);
        ImageView_Post_Search = findViewById(R.id.ImageView_Post_Search);
        ImageView_Post_Add = findViewById(R.id. ImageView_Post_Add);
        ImageView_Post_like = findViewById(R.id.ImageView_Post_like);
        ImageView_Me = findViewById(R.id.ImageView_Me);

        ImageView_Home.setClickable(true);
        ImageView_Post_Search.setClickable(true);


        ImageView_Post_Add.setClickable(true);
        ImageView_Post_like.setClickable(true);
        ImageView_Me.setClickable(true);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://travelcourceapp.appspot.com");

        ImageView_Post_Add = findViewById(R.id.ImageView_Post_Add);
        ImageView_Post_Add.setClickable(true);

        ImageView_Post_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostUploadActivity.class);
                intent.putExtra("token",token);
                startActivityForResult(intent, 0);
            }
        });

        ImageView_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.Sns_Main, homeFragments).commit();
            }
        });

        ImageView_Post_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.Sns_Main, searchFragemnts).commit();
            }
        });

        ImageView_Post_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.Sns_Main, saveFragments).commit();
            }
        });

        ImageView_Me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.Sns_Main, meFragments).commit();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.Sns_Main, homeFragments).commit();

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();
        databaseReference = database.getReference("post");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {

            synchronized (this) {
                String token, description, tag;
                int like;
                UriStrContainer uriStrContainer;

                token = data.getExtras().getString("token");
                description = data.getExtras().getString("description");
               // fpVideo = data.getExtras().getString("fpVideo");
                tag = data.getExtras().getString("tag");

                uriStrContainer = (UriStrContainer) data.getSerializableExtra("UriStr");

                String tags[] = tag.split("#");


                formatter = new SimpleDateFormat("yyyyMMHH_mmss"); // 실험이 필요함
                Date now = new Date();
                String primaryKey = formatter.format(now) + " " + token;
                String path = "post/" + token + "/" + primaryKey; // 추후 변경이 필요함 나중에 User 등등 추가되면

                for (int i = 1; i < tags.length; i++) {
                    fref.child("tag").child(tags[i]).child(primaryKey).setValue(path);
                }

                PostData pdata = new PostData();

                pdata.setLike(0);
                pdata.setTimes(System.currentTimeMillis());
                pdata.setNickname(nick);
                pdata.setDescription(description);
                pdata.setPrimaryKey(primaryKey);

                databaseReference.child(token).child(primaryKey).setValue(pdata);

                if (uriStrContainer != null) {
                    final int sz = uriStrContainer.size();
                    for (int i = 0; i < uriStrContainer.size(); i++) {
                        String temp = uriStrContainer.getAt(i).replace('.', 'W');

                        databaseReference.child(token).child(primaryKey).child("pictures").child(temp).setValue(1);
                    }
                }
            }
        }
    }
}