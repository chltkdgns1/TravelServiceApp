package com.gkftndltek.travelcourceapp.Sns.HomeFragment;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.KakaoLog.UserData;
import com.gkftndltek.travelcourceapp.R;

import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.HomeRecyclerClass;

import com.gkftndltek.travelcourceapp.Sns.PostRecycle.PostData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.usermgmt.response.model.User;

public class HomeFragments extends Fragment {

    private View rootView;
    private Context con;

    private HomeRecyclerClass homeRecyclerClass;

    private String token,nick;

    private FirebaseDatabase database;
    private DatabaseReference fref;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sns_home, container, false);
        // 리니어레이아웃
        init();
        return rootView;
    }

    void init() {
        con = getActivity();
        token = ((MainActivity) getActivity()).token;
        nick = ((MainActivity) getActivity()).nick;
        homeRecyclerClass = new HomeRecyclerClass();
        homeRecyclerClass.execute(rootView,getContext(),nick,token);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://travelcourceapp.appspot.com");

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        fref.child("users").child(token).child("followed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(final DataSnapshot data : dataSnapshot.getChildren()){

                    System.out.println("갯키 : " + data.getKey());

                    fref.child("users").child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserData udata = dataSnapshot.getValue(UserData.class);

                            final String image = udata.getImageName();

                            fref.child("post").child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snap : dataSnapshot.getChildren()) {

                                        PostData pdata = snap.getValue(PostData.class);
                                        final BitmapPostData bdata = new BitmapPostData();

//                                        System.out.println("이미지 : " + image);
                                        int resId = getResources().getIdentifier(image,"drawable",((MainActivity)getActivity()).getPackageName());

                                        bdata.setImageData(resId);
                                        bdata.setNickname(pdata.getNickname());
                                        bdata.setDescription(pdata.getDescription());
                                        bdata.setLike(pdata.getLike());
                                        bdata.setTimes(pdata.getTimes());
                                        bdata.setNowNick(token);
                                        bdata.setPath(snap.getKey());

                                        System.out.println(snap.getKey() + " " + pdata.getNickname());

                                        final int sz = (int)  snap.child("pictures").getChildrenCount();

                                        if (sz != 0) {
                                            for (DataSnapshot pics : snap.child("pictures").getChildren()) {
                                                String images = pics.getKey();
                                                images = images.replace('W', '.');

                                                final long ONE_MEGABYTE = 2048 * 2048;

                                                storageRef.child(images).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                    @Override
                                                    public void onSuccess(byte[] bytes) {
                                                        synchronized (this) {
                                                            System.out.println("여기 잘 들어옵니까???");
                                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                            bdata.setImage(bitmap);
                                                            if(bdata.getImageSize() == sz)
                                                                homeRecyclerClass.add(bdata);
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
                                        else homeRecyclerClass.add(bdata);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
    public void onAttach(Context context) {
        super.onAttach(context);
        // ((MainActivity)context).setOnBackPressedListener(this); // 프레그먼트
    }
}

