package com.example.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchRecyclerviewAdapter extends RecyclerView.Adapter<SearchRecyclerviewAdapter.CustomViewHolder> {

    private ArrayList<NoneBitmapDestData> arrayList;
    private Context context;



    public interface OnItemClickListener{
        void onItemClick(View v,int pos);
    }
    private SearchRecyclerviewAdapter.OnItemClickListener mListener =null;
    public void setOnItemClickListener(SearchRecyclerviewAdapter.OnItemClickListener listener){ this.mListener=listener; }

    public SearchRecyclerviewAdapter(ArrayList<NoneBitmapDestData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public SearchRecyclerviewAdapter(ArrayList<NoneBitmapDestData> arrayList) {
    }

    @NonNull
    @Override
    public SearchRecyclerviewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_recycle_index,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerviewAdapter.CustomViewHolder holder, int position) {
        holder.text_address.setText(arrayList.get(position).getAddress());
        holder.text_category.setText(arrayList.get(position).getCategory());
        //holder.imageView.setImageURI(arrayList.get(position).getUrl());
        Glide.with(holder.itemView).load(arrayList.get(position).getUrl()).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView text_address;
        TextView text_category;

        public CustomViewHolder(@NonNull View itemView){
            super(itemView);
            this.imageView=itemView.findViewById(R.id.ImageView_Person_Picture);
            this.text_address=itemView.findViewById(R.id.TextView_Person_List_Address);
            this.text_category=itemView.findViewById(R.id.TextView_PersonList_Category);

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
