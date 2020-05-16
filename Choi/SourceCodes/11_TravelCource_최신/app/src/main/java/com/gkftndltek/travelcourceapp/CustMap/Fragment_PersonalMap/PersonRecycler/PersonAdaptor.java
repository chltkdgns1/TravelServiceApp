package com.gkftndltek.travelcourceapp.CustMap.Fragment_PersonalMap.PersonRecycler;

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

public class PersonAdaptor extends RecyclerView.Adapter<PersonAdaptor.MyViewHolder>{

    private List<DestinationDataClass> data;

    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static  View.OnTouchListener touchListener;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private View rootView; //목록 클릭시 이벤트 상황

        private TextView TextView_Person_List_Name,TextView_PersonList_Address,TextView_Person_Numbers;
        private ImageView ImageView_Person_Picture;
        public MyViewHolder(View v) {
            super(v);

            TextView_Person_List_Name = v.findViewById(R.id.TextView_Person_List_Name);
            TextView_Person_Numbers = v.findViewById(R.id.TextView_Person_Numbers);
            TextView_PersonList_Address = v.findViewById(R.id.TextView_PersonList_Address);
            ImageView_Person_Picture = v.findViewById(R.id.ImageView_Person_Picture);
            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(clickListener);
            //v.setOnClickListener(clickListener);  //목록 클릭시 이벤트 상황 처리
            rootView = v;
        }
    }


    public PersonAdaptor(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        clickListener = click;
    }

    public PersonAdaptor(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.personal_recycle_index, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DestinationDataClass inputData = data.get(position);

        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리
        holder.TextView_PersonList_Address.setText(inputData.getAddress());
        // holder.TextView_What.setText(inputData.getRoadAddress());
        holder.TextView_Person_List_Name.setText(inputData.getName());
        holder.ImageView_Person_Picture.setImageBitmap(inputData.getLink());


        // 추가한 부분

        //    System.out.println(inputData.getName()); //
        String a,c,e;
        a = inputData.getCategory();
        c = inputData.getPhone();
        e = " | ";

        if(c =="" || c ==null) e = "";
        holder.TextView_Person_Numbers.setText(a + e + c); // 문서 값 사진 정하거나
    }

    public void remove(int position){
        data.remove(position);
        notifyDataSetChanged();
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
