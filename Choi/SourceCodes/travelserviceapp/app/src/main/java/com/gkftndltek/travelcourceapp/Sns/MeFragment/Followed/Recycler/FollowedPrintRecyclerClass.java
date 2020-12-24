package com.gkftndltek.travelcourceapp.Sns.MeFragment.Followed.Recycler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow.Recycler.FriendAdaptor;
import com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow.Recycler.FriendsDataClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FollowedPrintRecyclerClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference fref;

    public void execute(final View rootView, Context con) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_Followed);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        mAdapter = new FriendAdaptor(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {

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
