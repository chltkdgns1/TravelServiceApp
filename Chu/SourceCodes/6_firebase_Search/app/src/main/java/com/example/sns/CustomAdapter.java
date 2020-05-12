package com.example.sns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{

    private ArrayList<User> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
//                firebase에서 데이터받아서 User로 저장->그리고 커스텀어댑터에서 glide로 받아옴
                .into(holder.iv_profile);
        holder.tv_number.setText(String.valueOf(arrayList.get(position).getNumber()));
        holder.tv_id.setText(arrayList.get(position).getId());
        holder.tv_title.setText(arrayList.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return (arrayList!=null?arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_number;
        TextView tv_id;
        TextView tv_title;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile= itemView.findViewById(R.id.iv_profile);
            this.tv_number=itemView.findViewById(R.id.tv_number);
            this.tv_id=itemView.findViewById(R.id.tv_id);
            this.tv_title=itemView.findViewById(R.id.tv_title);

        }
    }
}