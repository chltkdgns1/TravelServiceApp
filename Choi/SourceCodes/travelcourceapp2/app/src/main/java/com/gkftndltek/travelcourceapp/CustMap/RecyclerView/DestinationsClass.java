package com.gkftndltek.travelcourceapp.CustMap.RecyclerView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;

public class DestinationsClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GestureDetector gestureDetector;
    public DestinationsClass(){}

    public void execute(View rootView,  Context con,final Handler handlerPushMessage, GestureDetector gestureDetectors){ // 리사이클러뷰 사용 시작
        this.gestureDetector = gestureDetectors;
        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_Destination_List);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

        mAdapter = new DestinationAdapter(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if(v.getTag() != null){
                    synchronized (this) {
                        int position = (int) v.getTag();
                        DestinationDataClass dat = ((DestinationAdapter) mAdapter).getData(position);
                        Message msg = Message.obtain();
                        msg.obj = dat;
                        msg.what = 2;
                        handlerPushMessage.sendMessage(msg);
                        gestureDetector.onTouchEvent(motionEvent);
                    }
                }
                return true;
            }
        });

        /*
        mAdapter = new DestinationAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null){
                    int position = (int)v.getTag();
                    ((DestinationAdapter) mAdapter).getData(position);
                   // gestureDetector.onTouchEvent(v.getE)
                }
            }
        });

         */
        recyclerView.setAdapter(mAdapter);
    }

    public void toPosition(final int index){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(index); // 얘가다함
            }
        }, 200);
    }

    public void add(DestinationDataClass data){
        ((DestinationAdapter) mAdapter).addDestData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear(){
        ((DestinationAdapter) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}
