package com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.CustMap.Calender.CalenderInsertActivity;
import com.gkftndltek.travelcourceapp.CustMap.Calender.CalenderItemChangeActivity;
import com.gkftndltek.travelcourceapp.CustMap.Calender.CalenderListActivity;
import com.gkftndltek.travelcourceapp.CustMap.Calender.DateClass;
import com.gkftndltek.travelcourceapp.R;

public class CalenderRecyclerView {

    private RecyclerView recyclerView;
    private CalenderAdaptor mAdapter;
    private LinearLayoutManager layoutManager;
    private ItemTouchHelper helper;

    public void execute(final Activity act, final Context con,final String token, final String name,final String stringDate){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) act.findViewById(R.id.CalendarView_RecyclerView);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것


            mAdapter = new CalenderAdaptor(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    synchronized (this) {
                        int position = (int) v.getTag();
                        final CalenderDataClass data = ((CalenderAdaptor) mAdapter).getData(position);


                        Intent intent = new Intent(con, CalenderItemChangeActivity.class);

                        System.out.println("키 잘 들어감 ? " + data.getKey());
                        intent.putExtra("data",data);
                        act.startActivityForResult(intent,2);

                    }
                }
            }
        },token,name,stringDate,act);

        recyclerView.setAdapter(mAdapter);
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        helper.attachToRecyclerView(recyclerView);
    }

    public void  changeData(CalenderDataClass data){
        ((CalenderAdaptor) mAdapter).changeData(data);
    } // 리사이클러뷰에 데이터 추가


    public void add(CalenderDataClass data){
        ((CalenderAdaptor) mAdapter).addData(data);
    } // 리사이클러뷰에 데이터 추가

    public int getSize(){
        return ((CalenderAdaptor) mAdapter).getItemCount();
    }

    public void clear(){
        ((CalenderAdaptor) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}