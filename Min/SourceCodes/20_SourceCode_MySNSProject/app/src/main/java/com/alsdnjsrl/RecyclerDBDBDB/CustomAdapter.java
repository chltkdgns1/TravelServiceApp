package com.alsdnjsrl.RecyclerDBDBDB;

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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;


    public CustomAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView).load(arrayList.get(position).getProfile()).into(holder.ImageView_profile);
        holder.TextView_Showtitle.setText(arrayList.get(position).getTitle());
        holder.TextView_Showcontents.setText(String.valueOf(arrayList.get(position).getContents()));
        holder.TextView_userName.setText(arrayList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView ImageView_profile;
        TextView TextView_Showtitle;
        TextView TextView_Showcontents;
        TextView TextView_userName;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ImageView_profile = itemView.findViewById(R.id.ImageView_profile);
            this.TextView_Showtitle = itemView.findViewById(R.id.TextView_Showtitle);
            this.TextView_Showcontents = itemView.findViewById(R.id.TextView_Showcontents);
            this.TextView_userName = itemView.findViewById(R.id.TextView_userName);
        }
    }
}
