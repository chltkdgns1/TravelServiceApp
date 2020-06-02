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

public class MainActivity extends AppCompatActivity {

    private PostRecyclerClass clinetRecyclerClass;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    private ImageView alsdnjsrl223;
    private VideoView alsdnjsrl234;
    private ImageView ImageView_Post_Add;

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

//                PostData data = (PostData) msg.obj;
//
//                final String filePathVideo = data.getFilePathVideo();
//                final BitmapPostData bdata = new BitmapPostData();
//
//                bdata.setContents(data.getContents());
//                bdata.setTitle(data.getTitle());
//                bdata.setUsername(data.getUsername());
//
//                databaseReference.child("tkdgns685").child(data.getTitle()).child("pictures").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
//                            synchronized (this) {
//                                String key = snapshot.getKey();
//
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        // 디비를 가져오던중 에러 발생 시
//                        Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
//                    }
//                });

            }
        }
    };

    /*
     if(filePathVideo != null && filePathImage != null) {
                    final long ONE_MEGABYTE = 1024 * 1024;
                    storageRef.child(filePathImage).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            bdata.setImage(bitmap);

                            storageRef.child(filePathVideo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    bdata.setVideo(uri);
                                    clinetRecyclerClass.add(bdata);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                    return;
                }

                if(filePathVideo != null){
                    storageRef.child(filePathVideo).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            bdata.setVideo(uri);
                            clinetRecyclerClass.add(bdata);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                    return;
                }

                if (filePathImage != null) {

                    final long ONE_MEGABYTE = 1024 * 1024;
                    storageRef.child(filePathImage).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            bdata.setImage(bitmap);
                            clinetRecyclerClass.add(bdata);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                    return;
                }

                clinetRecyclerClass.add(bdata);

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("post");


//        PostData data = new PostData();
//        data.setFilePathVideo("ewqeqeq");
//        data.setFilePathImage("dasdsadadada");
//        data.setTitle("dasdada");
//        data.setContents("dasdadada");
//        data.setUsername("dasdadada");
//        databaseReference.child("Post").child("tkdgns685").setValue(data);
//        databaseReference.child("Post").child("tkdgns685").child("pictures").setValue("2014.png");
//
//        databaseReference.child("Post").child("tkdgns685").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                PostData data = dataSnapshot.getValue(PostData.class);
//                System.out.println("잘 받아짐?");
//
//                System.out.println(data.getContents());
//                System.out.println(data.getFilePathImage());
//                System.out.println(data.getFilePathVideo());
//                System.out.println(data.getTitle());
//                System.out.println(data.getUsername());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // 디비를 가져오던중 에러 발생 시
//                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
//            }
//        });
//
//        databaseReference.child("tkdgns685").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                //   String a =  dataSnapshot.child("contents").getValue(String.class);
//                //   System.out.println(a);
//
//                System.out.println(dataSnapshot.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // 디비를 가져오던중 에러 발생 시
//                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
//            }
//        });


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

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("post");

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
                    for (DataSnapshot pics : snapshot.child("pictures").getChildren()) {
                        cnt++;
                        final int into = cnt;
                        String images = pics.getKey();
                        images = images.replace('W', '.');

                        final long ONE_MEGABYTE = 1024 * 1024;

                        storageRef.child(images).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void  onSuccess(byte[] bytes) {
                                synchronized (this) {
                                    System.out.println("여기 잘 들어옵니까???");
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    bdata.setImage(bitmap);
                                    if(into == sz) clinetRecyclerClass.add(bdata);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            //(PostData) data.getSerializableExtra("data");

            synchronized (this) {
                String title, contents, username, fpImage, fpVideo;
                UriStrContainer uriStrContainer;

                title = data.getExtras().getString("Title");
                contents = data.getExtras().getString("Contents");
                username = data.getExtras().getString("Username");
                fpVideo = data.getExtras().getString("fpVideo");
                uriStrContainer = (UriStrContainer) data.getSerializableExtra("UriStr");

                PostData pdata = new PostData();
                pdata.setFilePathVideo(fpVideo);
                pdata.setUsername(username);
                pdata.setContents(contents);
                pdata.setTitle(title);

                databaseReference.child("tkdgns685").child(title).setValue(pdata);

                for (int i = 0; i < uriStrContainer.size(); i++) {
                    String temp = uriStrContainer.getAt(i).replace('.', 'W');
                    databaseReference.child("tkdgns685").child(title).child("pictures").child(temp).setValue(1);
                }

                Message msg = Message.obtain();
                msg.obj = pdata;
                msg.what = 1;
                handlerPushMessage.sendMessage(msg);
            }
        }
    }
}

//
//       FirebaseStorage storage = FirebaseStorage.getInstance();
//
//        //Unique한 파일명을 만들자.
//        //storage 주소와 폴더 파일명을 지정해 준다.
//
//    alsdnjsrl223 = findViewById(R.id.alsdnjsrl223);
//       alsdnjsrl234 = findViewById(R.id.alsdnjsrl234);
//
//      StorageReference storageRef = storage.getReferenceFromUrl("gs://snsproject-defe7.appspot.com");

/*
        storageRef.child("20200514_2846.png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(MainActivity.this)
                            .load(task.getResult())
                            .into(alsdnjsrl234);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
        /*

        storageRef.child("20200514_2846.png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(MainActivity.this)
                            .load(task.getResult())
                            .into(alsdnjsrl223);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

         */

/*
        storageRef.child("20200516_1146.mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                alsdnjsrl234.setVideoURI(uri);
                alsdnjsrl234.start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

*/
