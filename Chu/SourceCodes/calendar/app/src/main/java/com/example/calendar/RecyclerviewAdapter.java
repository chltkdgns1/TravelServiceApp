package com.example.calendar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.CustomViewHolder> implements ItemTouchHelperListener {

    private ArrayList<ScheduleData> arrayList;
    private int REQUEST_CODE=444;
    private DatabaseReference mDatabase;
    private String name,date,mapname;

    public interface OnItemClickListener{
        void onItemClick(View v,int pos);
    }
    private OnItemClickListener mListener =null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }

    public RecyclerviewAdapter(ArrayList<ScheduleData> arrayList,String username,String mapname, String date) {

        this.name = username;
        this.mapname=mapname;
        this.date = date;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerviewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerviewAdapter.CustomViewHolder holder, final int position) {
        holder.textview_schedulename.setText(arrayList.get(position).getTextview_schedulename());
        holder.itemView.setTag(position);
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(v.getContext(),ItemshowActivity.class);
                intent.putExtra("name",arrayList.get(position).getTextview_schedulename());
                intent.putExtra("content",arrayList.get(position).getTextview_schedulecontent());
                v.getContext().startActivity(intent);


            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return true;
            }

        });
        */
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);

        } catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onItemMove(int from_position, int to_position){
        ScheduleData scheduledata = arrayList.get(from_position);
        arrayList.remove(from_position);
        arrayList.add(to_position,scheduledata);
        notifyItemMoved(from_position,to_position);
        return true;
    }

    @Override
    public void onItemSwipte(int position) {
        mDatabase.child("users").child(name).child("maps").child(mapname).child("manage").child(date).child(arrayList.get(position).getKey()).removeValue();
        arrayList.remove(position);
        notifyItemRemoved(position);
    }

    public class CustomViewHolder extends  RecyclerView.ViewHolder{
        protected TextView textview_schedulename;

        public CustomViewHolder(View itemView){
            super(itemView);
            this.textview_schedulename= itemView.findViewById(R.id.schedule_name);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        mListener.onItemClick(v,pos);

                    }

                }

            });
        }

    }

}
