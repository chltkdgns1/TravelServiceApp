package com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.CustMap.Calender.CalenderInsertActivity;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.NoneBitmapDestData;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.PersonRecycler.PersonAdaptor;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationAdapter;
import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.PostAdapter;

public class TravelClass {
    private RecyclerView recyclerView;
    private PersonAdaptor mAdapter;
    private LinearLayoutManager layoutManager;

    public void execute(final Activity act, final Context con){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) act.findViewById(R.id.RecyclerView_Basket);
        layoutManager = new LinearLayoutManager(con);
        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

        mAdapter = new PersonAdaptor(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    synchronized (this) {
                        int position = (int) v.getTag();
                        final DestinationDataClass cdata = ((PersonAdaptor) mAdapter).getData(position);

                        NoneBitmapDestData destdata = new NoneBitmapDestData();

                        destdata.setAddress(cdata.getAddress());
                        destdata.setCategory(cdata.getCategory());
                        destdata.setDescription(cdata.getDescription());
                        destdata.setDrable(cdata.getDrable());
                        destdata.setAddress(cdata.getAddress());
                        destdata.setIndex(cdata.getIndex());
                        destdata.setName(cdata.getName());
                        destdata.setPhone(cdata.getPhone());
                        destdata.setRoadAddress(cdata.getRoadAddress());
                        destdata.setX(cdata.getX());
                        destdata.setY(cdata.getY());
                        destdata.setUrl(cdata.getUrl());

                        Intent intent = new Intent(con, CalenderInsertActivity.class);
                        intent.putExtra("data",destdata);
                        System.out.println("여기 들어오지??");
                        act.setResult(1,intent);
                        act.finish();
                    }
                }
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    public void add(DestinationDataClass data){
        ((PersonAdaptor) mAdapter).addDestData(data);
    } // 리사이클러뷰에 데이터 추가

    public int getSize(){
        return ((PersonAdaptor) mAdapter).getItemCount();
    }

    public void clear(){
        ((PersonAdaptor) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}
