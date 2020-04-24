package com.gkftndltek.travelcourceapp;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.MyViewHolder>{
    private List<DestinationDataClass> data;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView  TextView_List_Name,TextView_List_Address,TextView_Numbers;

        public MyViewHolder(View v) {
            super(v);
            TextView_Numbers = v.findViewById(R.id.TextView_Numbers);
            TextView_List_Name = v.findViewById(R.id.TextView_List_Name);
            TextView_List_Address = v.findViewById(R.id.TextView_List_Address);
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

        holder.TextView_List_Address.setText(inputData.getAddress());
        holder.TextView_List_Name.setText(inputData.getName());
        String a,b,c,d,e;

        a = inputData.getDistinguish();
        b = inputData.getEx();
        c = inputData.getPhone();
        d = e = " | ";

        if(a == null) a = d = "";
        if(b == null)  b = e = "";
        if(c == null) c = e = "";

        holder.TextView_Numbers.setText(a + d + b + e + c);
    }

    public void addDestData(DestinationDataClass dest){
        if(dest != null) {
            data.add(dest);
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

    public DestinationDataClass getChat(int pos){
       return data != null ? data.get(pos) : null;
    }
}
