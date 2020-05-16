package com.gkftndltek.travelcourceapp.MakeMapActivity.firstRecyclerView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;

public class MakeMapClass  {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public MakeMapClass (){}

    public void execute(View rootView, Context con, final Handler handlerPushMessage){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_MapView); // 이거 수정해야함
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

       // mAdapter = new MakeMapAdapter();

        mAdapter = new MakeMapAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null){
                    synchronized (this) {
                        int position = (int) v.getTag();
                        MakeMapData dat = ((MakeMapAdapter) mAdapter).getData(position);
                        Message msg = Message.obtain();
                        msg.obj = dat;
                        msg.what = 2;
                        handlerPushMessage.sendMessage(msg);
                    }
                }
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    public void add(MakeMapData data){
        ((MakeMapAdapter) mAdapter).addData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear(){
        ((MakeMapAdapter) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}