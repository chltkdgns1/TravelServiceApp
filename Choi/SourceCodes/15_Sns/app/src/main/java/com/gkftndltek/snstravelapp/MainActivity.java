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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 리사이클러 뷰

    private PostRecyclerClass clinetRecyclerClass;

    // 데이터 베이스
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference fref;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    // View

    private ImageView ImageView_Post_Add, ImageView_Post_Confirm;
    private EditText EditText_Post_Search;


    // 시간

    private SimpleDateFormat formatter;


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
        setContentView(R.layout.activity_main);
        init();
    }

    void init() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://snsproject-defe7.appspot.com");

        ImageView_Post_Add = findViewById(R.id.ImageView_Post_Add);
        ImageView_Post_Add.setClickable(true);

        ImageView_Post_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostUploadActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        clinetRecyclerClass = new PostRecyclerClass();
        clinetRecyclerClass.execute(this, getApplicationContext(), handlerPushMessage);

        ImageView_Post_Confirm = findViewById(R.id.ImageView_Post_Confirm);
        EditText_Post_Search = findViewById(R.id.EditText_Post_Search);
        ImageView_Post_Confirm.setClickable(true);

        ImageView_Post_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = EditText_Post_Search.getText().toString();
                if (tag.isEmpty()) return;

                clinetRecyclerClass.clear();

                fref.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot shot : dataSnapshot.getChildren()) { // 태그 내부에 있는 게시글들의 primary key
                            String data = shot.getValue(String.class);
                            String[] path = data.split("/");

                            fref.child(path[0]).child(path[1]).child(path[2]).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    synchronized (this) {
                                        PostData data = snapshot.getValue(PostData.class);
                                        final BitmapPostData bdata = new BitmapPostData();

                                        bdata.setUsername(data.getUsername());
                                        bdata.setTitle(data.getTitle());
                                        bdata.setContents(data.getContents());

                                        final int sz = (int) snapshot.child("pictures").getChildrenCount();

                                        System.out.println("사진 몇 장이니? " + sz);
                                        int cnt = 0;

                                        if (sz != 0) {
                                            System.out.println("으아아아아아아아아아아ㅏ 여기 몇번들어와 ");
                                            for (DataSnapshot pics : snapshot.child("pictures").getChildren()) {
                                                cnt++;
                                                final int into = cnt;
                                                String images = pics.getKey();
                                                images = images.replace('W', '.');

                                                final long ONE_MEGABYTE = 1024 * 1024;

                                                storageRef.child(images).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                    @Override
                                                    public void onSuccess(byte[] bytes) {
                                                        synchronized (this) {
                                                            System.out.println("여기 잘 들어옵니까???");
                                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                            bdata.setImage(bitmap);
                                                            if (into == sz)
                                                                clinetRecyclerClass.add(bdata);
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        // Handle any errors
                                                    }
                                                });
                                            }
                                        }
                                        else clinetRecyclerClass.add(bdata);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // 디비를 가져오던중 에러 발생 시
                                    Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                                }
                            });
                        }
                        //
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });

            }
        });

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();
        databaseReference = database.getReference("post");

        /*
        clinetRecyclerClass.clear();

        databaseReference.child("tkdgns685").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    PostData data = snapshot.getValue(PostData.class);
                    final BitmapPostData bdata = new BitmapPostData();

                    bdata.setUsername(data.getUsername());
                    bdata.setTitle(data.getTitle());
                    bdata.setContents(data.getContents());

                    final int sz =(int) snapshot.child("pictures").getChildrenCount();
                    int cnt = 0;

                    if(sz != 0) {
                        for (DataSnapshot pics : snapshot.child("pictures").getChildren()) {
                            cnt++;
                            final int into = cnt;
                            String images = pics.getKey();
                            images = images.replace('W', '.');

                            final long ONE_MEGABYTE = 1024 * 1024;

                            storageRef.child(images).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    synchronized (this) {
                                        System.out.println("여기 잘 들어옵니까???");
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        bdata.setImage(bitmap);
                                        if (into == sz) clinetRecyclerClass.add(bdata);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }
                    }

                    else  clinetRecyclerClass.add(bdata);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            //(PostData) data.getSerializableExtra("data");

            synchronized (this) {
                String title, contents, username, fpImage, fpVideo, tag;
                UriStrContainer uriStrContainer;

                title = data.getExtras().getString("Title");
                contents = data.getExtras().getString("Contents");
                username = data.getExtras().getString("Username");
                fpVideo = data.getExtras().getString("fpVideo");
                tag = data.getExtras().getString("tag");

                uriStrContainer = (UriStrContainer) data.getSerializableExtra("UriStr");

                String tags[] = tag.split("#");


                formatter = new SimpleDateFormat("yyyyMMHH_mmss"); // 실험이 필요함
                Date now = new Date();
                String primaryKey = formatter.format(now) + " " + username;

                String path = "post/tkdgns685/" + primaryKey; // 추후 변경이 필요함 나중에 User 등등 추가되면

                for (int i = 1; i < tags.length; i++) {
                    fref.child(tags[i]).child(primaryKey).setValue(path);
                }

                PostData pdata = new PostData();

                pdata.setFilePathVideo(fpVideo);
                pdata.setUsername(username);
                pdata.setContents(contents);
                pdata.setTitle(title);
                pdata.setPrimaryKey(primaryKey);

                final BitmapPostData bdata = new BitmapPostData();

                bdata.setContents(contents);
                bdata.setTitle(title);
                bdata.setUsername(username);

                databaseReference.child("tkdgns685").child(primaryKey).setValue(pdata);

                if (uriStrContainer != null) {
                    final int sz = uriStrContainer.size();
                    for (int i = 0; i < uriStrContainer.size(); i++) {
                        String temp = uriStrContainer.getAt(i).replace('.', 'W');

                        databaseReference.child("tkdgns685").child(primaryKey).child("pictures").child(temp).setValue(1);
                    }
                }

                /*
                if(uriStrContainer != null) {
                    final int sz = uriStrContainer.size();
                    for (int i = 0; i < uriStrContainer.size(); i++) {
                        String temp = uriStrContainer.getAt(i).replace('.', 'W');

                        databaseReference.child("tkdgns685").child(primaryKey).child("pictures").child(temp).setValue(1);

                        final long ONE_MEGABYTE = 1024 * 1024;
                        final int into = i;

                        storageRef.child(uriStrContainer.getAt(i)).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                synchronized (this) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    bdata.setImage(bitmap);
                                    if (into == sz - 1) {
                                        System.out.println("여기 안 들어옴?");
                                        clinetRecyclerClass.add(bdata);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                }
                else clinetRecyclerClass.add(bdata);

                 */
            }
        }
    }
}