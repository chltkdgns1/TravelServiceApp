package com.example.sns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomViewHolder> {
    private List<CommentData> commentDataArrayList;

    public CommentAdapter(List<CommentData> commentDataArrayList) {
        this.commentDataArrayList = commentDataArrayList;
    }

    @NonNull
    @Override
    public CommentAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.CustomViewHolder holder, int position) {
        holder.comment_username.setText(commentDataArrayList.get(position).getComment_username());
        holder.comment_contents.setText(commentDataArrayList.get(position).getComment_contents());
    }

    public void add(CommentData data){
        commentDataArrayList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != commentDataArrayList ? commentDataArrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView comment_profile;
        protected TextView comment_username;
        protected TextView comment_contents;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.comment_profile=(ImageView) itemView.findViewById(R.id.comment_profile);
            this.comment_username=(TextView) itemView.findViewById(R.id.comment_username);
            this.comment_contents=(TextView) itemView.findViewById(R.id.comment_contents);
        }
    }
}
