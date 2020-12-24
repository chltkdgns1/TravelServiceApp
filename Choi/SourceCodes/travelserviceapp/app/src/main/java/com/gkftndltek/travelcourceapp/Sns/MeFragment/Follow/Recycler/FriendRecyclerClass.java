package com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow.Recycler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FriendRecyclerClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference fref;

    public FriendRecyclerClass() {
    }

    public void execute(final Activity act,final View rootView, Context con, final Handler handlerPushMessage, final String myuid) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_Kakao_Friends);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        mAdapter = new FriendAdaptor(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    synchronized (this) {
                        int position = (int) v.getTag();
                        FriendsDataClass data = ((FriendAdaptor) mAdapter).getData(position);
                        final String uid = data.getUid();

                        fref.child("signuped").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.exists()){
                                    Toast.makeText(act.getApplicationContext(),"해당 서비스를 이용하는 사용자가 아닙니다.",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                fref.child("users").child(myuid).child("followed").child(uid).addListenerForSingleValueEvent(new ValueEventListener() { // 팔로우 내가 한 사람 목록
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            Toast.makeText(act.getApplicationContext(),"이미 팔로우된 계정입니다.",Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        fref.child("users").child(myuid).child("followed").child(uid).setValue(1);
                                        fref.child("users").child(uid).child("follower").child(myuid).setValue(1);
                                        Toast.makeText(act.getApplicationContext(),"팔로우하였습니다.",Toast.LENGTH_LONG).show();
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
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    public void add(FriendsDataClass data) {
        ((FriendAdaptor) mAdapter).addData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear() {
        ((FriendAdaptor) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화

    public int getSize(){
        return ((FriendAdaptor) mAdapter).getItemCount();
    }
}
