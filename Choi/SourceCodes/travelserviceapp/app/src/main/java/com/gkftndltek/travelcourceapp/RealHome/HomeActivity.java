package com.gkftndltek.travelcourceapp.RealHome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gkftndltek.travelcourceapp.KakaoLog.LoadingActivity;
import com.gkftndltek.travelcourceapp.KakaoLog.LoginActivity;
import com.gkftndltek.travelcourceapp.MakeMapActivity.MakeMapAct;
import com.gkftndltek.travelcourceapp.R;

import com.gkftndltek.travelcourceapp.RealHome.Fragment.Event.EventFragment;
import com.gkftndltek.travelcourceapp.RealHome.Fragment.Notice.NoticeFragment;
import com.gkftndltek.travelcourceapp.RealHome.Fragment.Update.UpdateFragment;
import com.gkftndltek.travelcourceapp.RealHome.recycler.RecyclerClass;
import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.PostData;
import com.gkftndltek.travelcourceapp.Sns.UploadPost.PostUploadActivity;
import com.gkftndltek.travelcourceapp.Sns.UriStrContainer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    // implements View.OnClickListener

    //데이터 베이스

    private FirebaseDatabase database;
    private DatabaseReference fref;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    // 뷰

    private TextView TextView_Real_Home_Cource_Mangement, TextView_Real_Home_SNS;
    private TextView TextView_Real_Home_MyData, TextView_Real_Home_Update, TextView_Real_Home_Notice;
    private TextView TextView_Real_Home_Event, TextView_Real_Home_Admin_Writing;

    // 인텐트 데이터

    public String token, nick;

    // 시간

    private SimpleDateFormat formatter;


    // 핸들러

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            }
        }
    };

    private EventFragment eventFragment;
    private NoticeFragment noticeFragment;
    private UpdateFragment updateFragment;
    // 이벤트 공지사항 업데이트 체크

    private int clicked = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_home_layout);

        init();
    }

    public void init() {
        Intent it = getIntent();
        Bundle bun = it.getExtras();
        token = (String) bun.get("token");
        nick = (String) bun.get("nick");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://travelcourceapp.appspot.com");

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        TextView_Real_Home_Cource_Mangement = findViewById(R.id.TextView_Real_Home_Cource_Mangement);
        TextView_Real_Home_SNS = findViewById(R.id.TextView_Real_Home_SNS);
        TextView_Real_Home_MyData = findViewById(R.id.TextView_Real_Home_MyData);
        TextView_Real_Home_Admin_Writing = findViewById(R.id.TextView_Real_Home_Admin_Writing);

        TextView_Real_Home_Update = findViewById(R.id.TextView_Real_Home_Update);
        TextView_Real_Home_Notice = findViewById(R.id.TextView_Real_Home_Notice);
        TextView_Real_Home_Event = findViewById(R.id.TextView_Real_Home_Event);

        TextView_Real_Home_Cource_Mangement.setClickable(true);
        TextView_Real_Home_SNS.setClickable(true);
        TextView_Real_Home_MyData.setClickable(true);
        TextView_Real_Home_Admin_Writing.setClickable(true);

        TextView_Real_Home_Update.setClickable(true);
        TextView_Real_Home_Notice.setClickable(true);
        TextView_Real_Home_Event.setClickable(true);

        //TextView_Real_Home_MyData
        TextView_Real_Home_Cource_Mangement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MakeMapAct.class);
                intent.putExtra("token", token);
                intent.putExtra("nick", nick);
                startActivity(intent);
            }
        });

        TextView_Real_Home_SNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("nick", nick);
                startActivity(intent);
            }
        });

        TextView_Real_Home_MyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);//액티비티
                                finish();
                            }
                        });
            }
        });

        clicked = 1;


        eventFragment = new EventFragment();
        noticeFragment = new NoticeFragment();
        updateFragment = new UpdateFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home_Recycler, updateFragment).commit();

        TextView_Real_Home_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicked == 1) return;
                clicked = 1;
                updateFragment = new UpdateFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home_Recycler, updateFragment).commit();
            }
        });

        TextView_Real_Home_Notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                if (clicked == 2) return;
                noticeFragment = new NoticeFragment();
                clicked = 2;
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home_Recycler, noticeFragment).commit();
            }
        });

        TextView_Real_Home_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                if (clicked == 3) return;
                clicked = 3;
                eventFragment = new EventFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Home_Recycler, eventFragment).commit();
            }
        });

        TextView_Real_Home_Admin_Writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fref.child("adminlist").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String key = data.getKey();
                            if (token.equals(key)) {
                                Intent intent = new Intent(HomeActivity.this, PostUploadActivity.class);
                                intent.putExtra("nickname", nick);
                                startActivityForResult(intent, 0);
                                return;
                            }
                        }
                        return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

//    private void printENU(String tag){
//
//        recyclerClass = new RecyclerClass();
//        recyclerClass.execute(this,getApplication(),handlerPushMessage);
//
//        fref.child("adminpost").child("관리자").child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot data : dataSnapshot.getChildren()){
//
//                    PostData pdata = data.getValue(PostData.class);
//                    final BitmapPostData bdata = new BitmapPostData();
//
//                    bdata.setNickname("관리자");
//                    bdata.setDescription(pdata.getDescription());
//                    bdata.setLike(pdata.getLike());
//                    bdata.setTimes(pdata.getTimes());
//                    bdata.setNowNick(token);
//                    bdata.setPath(pdata.getPrimaryKey());
//
//                    final int sz = (int) data.child("pictures").getChildrenCount();
//
//                    if(sz == 0){
//                        recyclerClass.add(bdata);
//                        return;
//                    }
//
//                    for(DataSnapshot pics : data.child("pictures").getChildren()){
//                        String images = pics.getKey();
//                        images = images.replace('W', '.');
//
//                        final long ONE_MEGABYTE = 2048 * 2048;
//
//                        storageRef.child(images).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                            @Override
//                            public void onSuccess(byte[] bytes) {
//                                synchronized (this) {
//                                    System.out.println("여기 잘 들어옵니까???");
//                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                    bdata.setImage(bitmap);
//                                    if(bdata.getImageSize() == sz)
//                                        recyclerClass.add(bdata);
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle any errors
//                            }
//                        });
//                    }
//                }
//                return;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


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
                    if (tags[i].equals("event") || tags[i].equals("notice") || tags[i].equals("update")) {
                        tag = tags[i];
                        break;
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

    @Override
    public void onBackPressed() { // 기존의 액티비티
        // 다른 Fragment 에서 리스너를 설정했을 때 처리됩니다.

        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("정말로 종료하시겠습니까?");

        alert_ex.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert_ex.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alert_ex.setTitle("Notice");
        AlertDialog alert = alert_ex.create();
        alert.show();

    }

}

