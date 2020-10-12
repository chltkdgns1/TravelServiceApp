package com.example.coooooment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView TextView_name, TextView_comment, TextView_time;
    View view;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        TextView_name = itemView.findViewById(R.id.TextView_name);
        TextView_comment = itemView.findViewById(R.id.TextView_comment);
        TextView_time = itemView.findViewById(R.id.TextView_time);
        view = itemView;
    }
}
