package com.gkftndltek.travelcourceapp.CustMap.Fragment_MyDataList.MyMapDataListFragment;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;

public class MyMapDataClass{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public void execute(View rootView, Context con, final Handler handlerPushMessage){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_MyList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

        mAdapter = new MyMapDataAdaptor(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null){
                    int position = (int)v.getTag();
                    ((MyMapDataAdaptor) mAdapter).getData(position);
                    // 터치를 했을 때
                }
            }
        });
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
        ((MyMapDataAdaptor) mAdapter).addDestData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear(){
        ((MyMapDataAdaptor) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}

