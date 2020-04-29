package com.gkftndltek.travelcourceapp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.TMapView;

public class DestinationsClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
   // private TMapView TmapView;
    public DestinationsClass(){}

    void execute(Activity act, Context con,final Handler handlerPushMessage){ // 리사이클러뷰 사용 시작

    //    TmapView = tampView;
        recyclerView = (RecyclerView) act.findViewById(R.id.RecyclerView_Destination_List);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DestinationAdapter();


        mAdapter = new DestinationAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null){
                    int position = (int)v.getTag();
                    DestinationDataClass dat = ((DestinationAdapter) mAdapter).getData(position);
                 //   TmapView.setCenterPoint(dat.getX(),dat.getY(),true);
                 //   TmapView.setZoomLevel(15);

                    Message msg = Message.obtain();
                    msg.obj = dat;
                    msg.what = 2;
                    handlerPushMessage.sendMessage(msg);
                }
            }
        });


        recyclerView.setAdapter(mAdapter);
    }

    void toPosition(final int index){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(index); // 얘가다함
            }
        }, 200);
    }

    void add(DestinationDataClass data){
        ((DestinationAdapter) mAdapter).addDestData(data);
    } // 리사이클러뷰에 데이터 추가

    void clear(){
        ((DestinationAdapter) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}