package com.alsdnjsrl.travelcourceapp;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DestinationsClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public DestinationsClass(){}

    void execute(Activity act,  Context con){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) act.findViewById(R.id.RecyclerView_Destination_List);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DestinationAdapter();

        /* 찾아온 위치 목록을 클릭했을 때의 이벤트 처리
        mAdapter = new DestinationAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null){
                    int position = (int)v.getTag();
                    ((DestinationAdapter) mAdapter).getData(position);

                }
            }
        });
        */

        recyclerView.setAdapter(mAdapter);
    }

    void add(DestinationDataClass data){
        ((DestinationAdapter) mAdapter).addDestData(data);
    } // 리사이클러뷰에 데이터 추가

    void clear(){
        ((DestinationAdapter) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}
