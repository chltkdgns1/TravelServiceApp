package com.gkftndltek.travelcourceapp.CustMap.Calender.Recycler;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CalenderAdaptor extends RecyclerView.Adapter<CalenderAdaptor.MyViewHolder> implements ItemTouchHelperListener {

    private List<CalenderDataClass> data;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String token , name,stringDate;

    private Activity act;

    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private View rootView; //목록 클릭시 이벤트 상황 처리

        private TextView TextView_Calender_List_Name;


        public MyViewHolder(View v) {
            super(v);

            TextView_Calender_List_Name = v.findViewById(R.id.TextView_Calender_List_Name);
            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(clickListener);  //목록 클릭시 이벤트 상황 처리
            rootView = v;
        }
    }


    public CalenderAdaptor(View.OnClickListener click, String token, String name, String stringDate, Activity act) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        this.act = act;
        data = new ArrayList<>();
        clickListener = click;
        this.stringDate = stringDate;
        this.token = token;
        this.name = name;
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
    }

    public CalenderAdaptor(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_calender_recyclerview, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CalenderDataClass inputData = data.get(position);
        System.out.println(inputData.getName());
        holder.TextView_Calender_List_Name.setText(inputData.getName());
        //      holder.ImageView_MyList_Picture_nearby.setImageBitmap(inputData.getBit());

        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리

    }

    @Override
    public boolean onItemMove(int from_position, int to_position){
        System.out.println("왜 안움직이죠?");

        CalenderDataClass scheduledata = data.get(from_position);
        data.remove(from_position);
        data.add(to_position,scheduledata);
        notifyItemMoved(from_position,to_position);
        return true;
    }

    @Override
    public void onItemSwipte(final int position) {

        AlertDialog.Builder alert_ex = new AlertDialog.Builder(act);
        alert_ex.setMessage("목록을 삭제하시겠습니까?");

        alert_ex.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyDataSetChanged();
            }
        });

        alert_ex.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ref.child("users").child(token).child("maps").child(name).child("manage").child(stringDate).child(data.get(position).getKey()).removeValue();
                data.remove(position);
                notifyItemRemoved(position);
            }
        });

        alert_ex.setTitle("Notice");
        AlertDialog alert = alert_ex.create();
        alert.show();
    }

    public void changeData(CalenderDataClass cdata){
        if(cdata != null){

            String key = cdata.getKey();

            int l=0,r = data.size() - 1;

            while(l <= r){
                int m = (l + r )>> 1;

                int t = data.get(m).getKey().compareTo(key);
                if(t == 0){
                    data.get(m).setContent(cdata.getContent());
                    data.get(m).setName(cdata.getName());
                    break;
                }
                else if(t > 0) r = m - 1;
                else l = m + 1;
            }
            notifyDataSetChanged();
        }
    }

    public void addData(CalenderDataClass dest) {
        if (dest != null) {
            data.add(dest);

          //  notifyItemInserted(data.size() - 1); // 갱신용
            notifyDataSetChanged();
        }
    }


    // Return the size of your dataset (invoked by the layout manager)

    public void clear() {
        data.clear();
        notifyItemRangeRemoved(0, data.size() - 1);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public CalenderDataClass getData(int pos) {
        return data != null ? data.get(pos) : null;
    }
}
