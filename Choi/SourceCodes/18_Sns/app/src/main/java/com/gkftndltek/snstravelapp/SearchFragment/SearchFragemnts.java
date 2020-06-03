package com.gkftndltek.snstravelapp.SearchFragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.snstravelapp.BitmapPostData;
import com.gkftndltek.snstravelapp.PostRecycle.PostData;
import com.gkftndltek.snstravelapp.PostRecycle.PostRecyclerClass;
import com.gkftndltek.snstravelapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SearchFragemnts extends Fragment{

    private View rootView;
    private Context con;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference fref;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    //  뷰

    private EditText EditText_Post_Search;
    private ImageView ImageView_Post_Confirm;


    // 리사이클러

    private PostRecyclerClass clinetRecyclerClass;

    // 핸들러

    public Handler handlerPushMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragmen_search, container, false);
        // 리니어레이아웃

        init();
        return rootView;
    }

    void init() {
        con = getActivity();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://snsproject-defe7.appspot.com");
        database = FirebaseDatabase.getInstance();
        fref = database.getReference();
        databaseReference = database.getReference("post");

        EditText_Post_Search = rootView.findViewById(R.id.EditText_Post_Search);
        ImageView_Post_Confirm = rootView.findViewById(R.id.ImageView_Post_Confirm);

        clinetRecyclerClass = new PostRecyclerClass();
        clinetRecyclerClass.execute(rootView, con,handlerPushMessage);

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

                                        bdata.setNickname(data.getNickname());
                                        bdata.setDescription(data.getDescription());

                                        final int sz = (int) snapshot.child("pictures").getChildrenCount();

                                        System.out.println("사진 몇 장이니? " + sz);

                                        if (sz != 0) {
                                            for (DataSnapshot pics : snapshot.child("pictures").getChildren()) {
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
                                                            if(bdata.getImageSize() == sz)
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       // ((MainActivity)context).setOnBackPressedListener(this); // 프레그먼트
    }
}
