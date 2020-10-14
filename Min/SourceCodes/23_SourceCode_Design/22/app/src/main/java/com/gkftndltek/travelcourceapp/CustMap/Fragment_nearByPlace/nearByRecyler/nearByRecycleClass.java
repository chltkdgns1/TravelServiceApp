package com.gkftndltek.travelcourceapp.CustMap.Fragment_nearByPlace.nearByRecyler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.NoneBitmapDestData;
import com.gkftndltek.travelcourceapp.CustMap.Fragment_nearByPlace.placeFindData;
import com.gkftndltek.travelcourceapp.CustMap.MainActivity;
import com.gkftndltek.travelcourceapp.R;

public class nearByRecycleClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public nearByRecycleClass(){}

    public void execute(View rootView, final Context con, final Handler handlerPushMessage){ // 리사이클러뷰 사용 시작
        recyclerView = (RecyclerView) rootView.findViewById(R.id.LinearLayout_nearBy_recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

        mAdapter = new nearByAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null){
                    synchronized (this) {
                        int position = (int) v.getTag();
                        final placeFindData dat = ((nearByAdapter) mAdapter).getData(position);


                        AlertDialog.Builder alert_ex = new AlertDialog.Builder(con);
                        alert_ex.setMessage("경로를 장바구니에 넣으시겠습니까?");

                        alert_ex.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("취소 눌렸냐? 개새이야");
                            }
                        });

                        alert_ex.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 버그 발생할 수 있음.

                                NoneBitmapDestData data = new NoneBitmapDestData();

                                if(dat == null){
                                    Toast.makeText(con,"리사이클러뷰 아이템에 데이터가 없습니다.",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                data.setAddress(dat.getAddres());
                                data.setName(dat.getTitle());
                                data.setX(dat.getX());
                                data.setY(dat.getY());
                                data.setUrl(dat.getImage());

                                Message msg = Message.obtain();
                                msg.obj = data;
                                msg.what = 2;
                                handlerPushMessage .sendMessage(msg);
                            }
                        });

                        alert_ex.setTitle("Notice");
                        AlertDialog alert = alert_ex.create();
                        alert.show();
                    }
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

    public void add(placeFindData data){
        ((nearByAdapter) mAdapter).addData(data);
    } // 리사이클러뷰에 데이터 추가

    public void clear(){
        ((nearByAdapter) mAdapter).clear();
    }  // 리사이클러뷰 전체 초기화
}
