package com.gkftndltek.travelcourceapp.Sns.PostRecycle;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeRecyclerClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference fref;


    public void execute(final View rootView, Context con,String name,String token) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.Home_RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        fref = database.getReference();

        mAdapter = new PostAdapter(con,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null){
                    synchronized (this) {
                        int position = (int) v.getTag();
                        // 삭제하시겠습니까?
                    }
                }
            }
        },name,token);

        recyclerView.setAdapter(mAdapter);
    }

    public void recyclerViewGone(){
        recyclerView.setVisibility(View.GONE);
    }

    public void recyclerViewVisible(){
        recyclerView.setVisibility(View.VISIBLE);
    }

    public synchronized void add(BitmapPostData data){
        ((PostAdapter) mAdapter).addData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear(){
        ((PostAdapter) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}

