package com.gkftndltek.travelcourceapp.CustMap.FntPersonalMap.PersonRecycler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;

public class PersonClass  {
        private RecyclerView recyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager layoutManager;

        public void execute(View rootView, final Context con, final Handler handlerPushMessage){ // 리사이클러뷰 사용 시작
            recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_Basket);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(con);

            recyclerView.setLayoutManager(layoutManager);

            //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

            mAdapter = new PersonAdaptor(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getTag() != null){
                        final int position = (int)v.getTag();
                        final DestinationDataClass data = ((PersonAdaptor) mAdapter).getData(position);

                        if(con == null){
                            System.out.println("왜 눌임?");
                            return;
                        }
                        AlertDialog.Builder alert_ex = new AlertDialog.Builder(con);
                        alert_ex.setMessage("내 목록에 추가하시겠습니까?");

                        alert_ex.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              return;
                            }
                        });

                        alert_ex.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 버그 발생할 수 있음.

                                ((PersonAdaptor) mAdapter).remove(position);

                                Message msg = Message.obtain();
                                msg.obj = data;
                                msg.what = 2;
                                handlerPushMessage.sendMessage(msg);
                            }
                        });

                        alert_ex.setTitle("Notice");
                        AlertDialog alert = alert_ex.create();
                        alert.show();
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
            ((PersonAdaptor) mAdapter).addDestData(data);
        } // 리사이클러뷰에 데이터 추가

        public void clear(){
            ((PersonAdaptor) mAdapter).clear();
        }  // 리사이클러뷰 전체 초기화
}
