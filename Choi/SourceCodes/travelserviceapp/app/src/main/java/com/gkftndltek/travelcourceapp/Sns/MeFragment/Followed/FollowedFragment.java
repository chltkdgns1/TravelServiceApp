package com.gkftndltek.travelcourceapp.Sns.MeFragment.Followed;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.KakaoLog.UserData;
import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow.Recycler.FriendsDataClass;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Followed.Recycler.FollowedPrintRecyclerClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FollowedFragment extends Fragment {
    private View rootView;
    private Context con;
    // 닉네임 토큰

    private FollowedPrintRecyclerClass followedPrintRecyclerClass;
    private String nick,token;

    private FirebaseDatabase database;
    private DatabaseReference fref;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.sns_followed_print, container, false);
        // 리니어레이아웃

        init();
        return rootView;
    }

    void init() {
        con = getActivity();
        followedPrintRecyclerClass = new FollowedPrintRecyclerClass();
        followedPrintRecyclerClass.execute(rootView,con);

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        token = ((MainActivity) con).token;

        fref.child("users").child(token).child("followed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    final String key = data.getKey();

                    fref.child("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserData data = snapshot.getValue(UserData.class);
                            FriendsDataClass fdata = new FriendsDataClass();

                            int resId = getResources().getIdentifier(data.getImageName(),"drawable",((MainActivity)getActivity()).getPackageName());

                            fdata.setImageName(resId);
                            fdata.setUsername(data.getNickName());
                            followedPrintRecyclerClass.add(fdata);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
