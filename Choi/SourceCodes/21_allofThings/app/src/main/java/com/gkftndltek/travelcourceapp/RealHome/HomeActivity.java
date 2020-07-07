package com.gkftndltek.travelcourceapp.RealHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.Login.LoginDataClass;
import com.gkftndltek.travelcourceapp.R;

import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.PostData;
import com.gkftndltek.travelcourceapp.Sns.UploadPost.PostUploadActivity;
import com.gkftndltek.travelcourceapp.Sns.UriStrContainer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //데이터 베이스

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference fref;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    // 뷰

    private TextView TextView_Real_Home_Cource_Mangement,TextView_Real_Home_SNS;
    private TextView TextView_Real_Home_MyData,TextView_Real_Home_Update,TextView_Real_Home_Notice;
    private TextView TextView_Real_Home_Event;

    // 인텐트 데이터

    private String token, nick;

    // 시간

    private SimpleDateFormat formatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_home_layout);

        init();
    }

    public void init(){

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://travelcourceapp.appspot.com");

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        TextView_Real_Home_Cource_Mangement = findViewById(R.id.TextView_Real_Home_Cource_Mangement);
        TextView_Real_Home_SNS = findViewById(R.id.TextView_Real_Home_SNS);
        TextView_Real_Home_MyData = findViewById(R.id.TextView_Real_Home_MyData);

        TextView_Real_Home_Update = findViewById(R.id.TextView_Real_Home_Update);
        TextView_Real_Home_Notice = findViewById(R.id.TextView_Real_Home_Notice);
        TextView_Real_Home_Event = findViewById(R.id.TextView_Real_Home_Event);

        TextView_Real_Home_Cource_Mangement.setClickable(true);
        TextView_Real_Home_SNS.setClickable(true);
        TextView_Real_Home_MyData.setClickable(true);

        TextView_Real_Home_Update.setClickable(true);
        TextView_Real_Home_Notice.setClickable(true);
        TextView_Real_Home_Event.setClickable(true);

        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token");
        nick = (String)bun.get("nick");
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.TextView_Real_Home_Cource_Mangement){
            Intent intent = new Intent(HomeActivity.this, LoginDataClass.class);
            intent.putExtra("token", token);
            intent.putExtra("nick",nick);
            startActivity(intent);
        }
        else if(v.getId() == R.id.TextView_Real_Home_SNS){
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("nick",nick);
            startActivity(intent);
        }
        else if(v.getId() == R.id.TextView_Real_Home_MyData){
            Intent intent = new Intent(HomeActivity.this, MyData.class);
            intent.putExtra("token", token);
            intent.putExtra("nick",nick);
            startActivity(intent);
        }
        else if(v.getId() == R.id.TextView_Real_Home_Update){

        }
        else if(v.getId() == R.id.TextView_Real_Home_Notice){

        }
        else if(v.getId() == R.id.TextView_Real_Home_Event){

        }
        else if(v.getId() == R.id.TextView_Real_Home_Admin_Writing){ // 어드민만 공지사항 업데이트 이벤트를 작성할 수 있음
            Intent intent = new Intent(HomeActivity.this, PostUploadActivity.class);
            intent.putExtra("nickname",nick);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {

            synchronized (this) {
                String nickname, description, tag;
                UriStrContainer uriStrContainer;

                nickname = "관리자";
                description = data.getExtras().getString("description");
                // fpVideo = data.getExtras().getString("fpVideo");
                tag = data.getExtras().getString("tag");

                uriStrContainer = (UriStrContainer) data.getSerializableExtra("UriStr");

                String tags[] = tag.split("#");
                String path = "";

                formatter = new SimpleDateFormat("yyyyMMHH_mmss"); // 실험이 필요함

                Date now = new Date();
                String primaryKey = formatter.format(now) + " " + nickname;

                for (int i = 1; i < tags.length; i++) {
                    if(tags[i] == "event" || tags[i] == "notice" ||  tags[i] == "update"){
                        tag = tags[i]; break;
                    }
                }

                PostData pdata = new PostData();

                pdata.setLike(0);
                pdata.setTimes(System.currentTimeMillis());
                pdata.setNickname(nickname);
                pdata.setDescription(description);
                pdata.setPrimaryKey(primaryKey);

                fref.child("adminpost").child(nickname).child(tag).child(primaryKey).setValue(pdata);

                if (uriStrContainer != null) {
                    final int sz = uriStrContainer.size();
                    for (int i = 0; i < uriStrContainer.size(); i++) {
                        String temp = uriStrContainer.getAt(i).replace('.', 'W');

                        fref.child("adminpost").child(nickname).child(tag).child(primaryKey).child("pictures").child(temp).setValue(1);
                    }
                }
            }
        }
    }

}

