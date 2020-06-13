package com.example.sns;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostRecyclerClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public void execute(Activity act,Context con, final Handler handlerPushMessage){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) act.findViewById(R.id.Post_RecyclerView); // 이거 수정해야함
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

    public synchronized void add(BitmapPostData data){
        ((PostAdapter) mAdapter).addData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear(){
        ((PostAdapter) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}
