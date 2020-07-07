package com.gkftndltek.travelcourceapp.MakeMapActivity.firstRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;

import java.util.ArrayList;
import java.util.List;

public class MakeMapAdapter extends RecyclerView.Adapter<MakeMapAdapter.MyViewHolder> {
    private List<MakeMapData> data;

    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
//        private TextView TextView_List_Name,TextView_List_Address,TextView_Numbers;//TextView_What;
//        private ImageView ImageView_Marker,ImageView_Picture;

        private TextView TextView_Map_Name,TextView_Map_coment;

        private View rootView; //목록 클릭시 이벤트 상황 처리

        public MyViewHolder(View v) {
            super(v);
            TextView_Map_Name = v.findViewById(R.id.TextView_Map_Name);
            TextView_Map_coment = v.findViewById(R.id.TextView_Map_coment);
            v.setClickable(true);
            v.setEnabled(true);
            //v.setOnTouchListener(touchListener);
            v.setOnClickListener(cilicListener);
            rootView = v;
        }
    }


    public MakeMapAdapter(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }

    public MakeMapAdapter(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        cilicListener = click;
    }

    public MakeMapAdapter() {
        data = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.map_list_index, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MakeMapData inputData = data.get(position);
        holder.TextView_Map_Name.setText(inputData.getMapName());
        holder.TextView_Map_coment.setText(inputData.getComent());
        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리
    }

    public void addData(MakeMapData dest) {
        if (dest != null) {
            data.add(dest);
            System.out.println("리사이클러뷰 갯수 : " + Integer.toString(data.size() - 1));
            notifyItemInserted(data.size() - 1); // 갱신용
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

    public MakeMapData getData(int pos) {
        return data != null ? data.get(pos) : null;
    }
}
