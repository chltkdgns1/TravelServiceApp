package com.gkftndltek.travelcourceapp.Sns.MeFragment.FollowWritePrint.Recycler.Recycler;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;


public class PhotosClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public void execute(View rootView, Context con){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_Follow_Write_Print); // 이거 수정해야함
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(con,3);

        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

        // mAdapter = new MakeMapAdapter();

        mAdapter = new PhotosAdaptor(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null){
                    synchronized (this) {
                        int position = (int) v.getTag();

                    }
                }
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    public void recyclerViewGone(){
        recyclerView.setVisibility(View.GONE);
    }

    public void recyclerViewVisible(){
        recyclerView.setVisibility(View.VISIBLE);
    }

    public synchronized void add(PhotosData data){
        ((PhotosAdaptor) mAdapter).addData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear(){
        ((PhotosAdaptor) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}

