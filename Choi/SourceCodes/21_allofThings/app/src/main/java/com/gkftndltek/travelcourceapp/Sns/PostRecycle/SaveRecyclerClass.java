package com.gkftndltek.travelcourceapp.Sns.PostRecycle;


import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;


public class SaveRecyclerClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public void execute(View rootView,Context con, final Handler handlerPushMessage){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) rootView.findViewById(R.id.Save_RecyclerView); // 이거 수정해야함
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

        // mAdapter = new MakeMapAdapter();

        mAdapter = new PostAdapter(new View.OnClickListener() {
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

    public synchronized void add(BitmapPostData data){
        ((PostAdapter) mAdapter).addData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear(){
        ((PostAdapter) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}
