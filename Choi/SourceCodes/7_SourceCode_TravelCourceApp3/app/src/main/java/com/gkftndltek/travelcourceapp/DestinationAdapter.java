package com.gkftndltek.travelcourceapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.MyViewHolder>{
    private List<DestinationDataClass> data;
  //  private static View.OnClickListener clickListener; 목록 클릭시 이벤트 상황 처리

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView  TextView_List_Name,TextView_List_Address,TextView_Numbers;//TextView_What;
        private ImageView ImageView_Marker,ImageView_Picture;
       //private View rootView; 목록 클릭시 이벤트 상황 처리
        public MyViewHolder(View v) {
            super(v);
            TextView_Numbers = v.findViewById(R.id.TextView_Numbers);
            TextView_List_Name = v.findViewById(R.id.TextView_List_Name);
            TextView_List_Address = v.findViewById(R.id.TextView_List_Address);
            ImageView_Marker = v.findViewById(R.id.ImageView_Marker);
            ImageView_Picture = v.findViewById(R.id.ImageView_Picture);
            //TextView_What = v.findViewById(R.id.TextView_What);
           // v.setClickable(true);
            //v.setEnabled(true);
           // v.setOnClickListener(clickListener);  목록 클릭시 이벤트 상황 처리
           // rootView = v;
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    /*
    public chatAdapter(List<chatdata> myDataset, Context context, String mynickname) {
        mDataset = myDataset;
        con = context;
        Fresco.initialize(context);
        mynicknames = mynickname;
    }
     */

    /*

    public DestinationAdapter(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        clickListener = click;
    }

     */

    public DestinationAdapter() {
        data = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DestinationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.destination_index, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DestinationDataClass inputData = data.get(position);

      //  holder.rootView.setTag(position); 목록 클릭시 이벤트 상황 처리
        holder.TextView_List_Address.setText(inputData.getAddress());
       // holder.TextView_What.setText(inputData.getRoadAddress());
        holder.TextView_List_Name.setText(inputData.getName());
        holder.ImageView_Marker.setImageResource(inputData.getDrable());
        holder.ImageView_Picture.setImageBitmap(inputData.getLink());

        System.out.println(inputData.getName()); //
        String a,c,e;
        a = inputData.getCategory();
        c = inputData.getPhone();
        e = " | ";

        if(c =="" || c ==null) e = "";
        holder.TextView_Numbers.setText(a + e + c); // 문서 값 사진 정하거나
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
    }

    @Override
    public int getItemCount() {
        return data == null ?  0  : data.size();
    }

    public DestinationDataClass getData(int pos){
        return data != null ? data.get(pos) : null;
    }
}
