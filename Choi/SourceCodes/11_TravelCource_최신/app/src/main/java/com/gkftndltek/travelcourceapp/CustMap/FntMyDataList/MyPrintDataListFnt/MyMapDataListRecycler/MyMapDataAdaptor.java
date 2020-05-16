package com.gkftndltek.travelcourceapp.CustMap.FntMyDataList.MyPrintDataListFnt.MyMapDataListRecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.CustMap.RecyclerView.DestinationDataClass;
import com.gkftndltek.travelcourceapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyMapDataAdaptor extends RecyclerView.Adapter<MyMapDataAdaptor.MyViewHolder>{

    private List<DestinationDataClass> data;

    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static  View.OnTouchListener touchListener;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private View rootView; //목록 클릭시 이벤트 상황

        private TextView TextView_MyList_Name,TextView_MyList_Address,TextView_MyList_Numbers;
        private ImageView ImageView_MyList_Picture;
        public MyViewHolder(View v) {
            super(v);

            TextView_MyList_Name = v.findViewById(R.id.TextView_MyList_Name);
            TextView_MyList_Numbers = v.findViewById(R.id.TextView_MyList_Numbers);
            TextView_MyList_Address = v.findViewById(R.id.TextView_MyList_Address);
            ImageView_MyList_Picture = v.findViewById(R.id.ImageView_MyList_Picture);
            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(clickListener);
            //v.setOnClickListener(clickListener);  //목록 클릭시 이벤트 상황 처리
            rootView = v;
        }
    }


    public MyMapDataAdaptor(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        clickListener = click;
    }

    public MyMapDataAdaptor(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_mapdata_list, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DestinationDataClass inputData = data.get(position);

        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리
        holder.TextView_MyList_Address.setText(inputData.getAddress());
        // holder.TextView_What.setText(inputData.getRoadAddress());
        holder.TextView_MyList_Name.setText(inputData.getName());
        holder.ImageView_MyList_Picture.setImageBitmap(inputData.getLink());


        // 추가한 부분

        //    System.out.println(inputData.getName()); //
        String a,c,e;
        a = inputData.getCategory();
        c = inputData.getPhone();
        e = " | ";

        if(c =="" || c ==null) e = "";
        holder.TextView_MyList_Numbers.setText(a + e + c); // 문서 값 사진 정하거나
    }

    public void addDestData(DestinationDataClass dest){
        if(dest != null) {
            data.add(dest);
            System.out.println("버그찾았다이새키 : " + Integer.toString(data.size() - 1));
            notifyItemInserted(data.size() - 1); // 갱신용
        }
    }

    // Return the size of your dataset (invoked by the layout manager)

    public void clear(){
        data.clear();
        notifyItemRangeRemoved(0, data.size() - 1);
    }

    @Override
    public int getItemCount() {
        return data == null ?  0  : data.size();
    }

    public DestinationDataClass getData(int pos){
        return data != null ? data.get(pos) : null;
    }
}
