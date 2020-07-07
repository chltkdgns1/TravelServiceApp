package com.gkftndltek.travelcourceapp.RealHome.recycler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;

public class RecyclerClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public RecyclerClass (){}

    public void execute(Activity act, Context con, final Handler handlerPushMessage){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) act.findViewById(R.id.RecyclerView_MapView); // 이거 수정해야함
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

        // mAdapter = new MakeMapAdapter();

        mAdapter = new RealHomeAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null){
                    synchronized (this) {
                        int position = (int) v.getTag();
                        /*
                        RecyclerData dat = ((RealHomeAdapter) mAdapter).getData(position);
                        Message msg = Message.obtain();
                        msg.obj = dat;
                        msg.what = 2;
                        handlerPushMessage.sendMessage(msg);

                         */
                    }
                }
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    public void add(BitmapPostData data){
        ((RealHomeAdapter) mAdapter).addData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear(){
        ((RealHomeAdapter) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}
