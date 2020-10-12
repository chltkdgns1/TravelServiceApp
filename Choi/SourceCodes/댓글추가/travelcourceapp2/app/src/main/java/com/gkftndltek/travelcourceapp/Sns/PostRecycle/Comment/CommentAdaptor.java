package com.gkftndltek.travelcourceapp.Sns.PostRecycle.Comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;


import java.util.ArrayList;
import java.util.List;

public class CommentAdaptor extends RecyclerView.Adapter<CommentAdaptor.MyViewHolder> {

    private List<CommentData> data;
    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private View rootView; //목록 클릭시 이벤트 상황 처리

        TextView TextView_Post_Comment_Name_Item,TextView_Post_Comment_Time_Item,TextView_Post_Comment_Input_Item;

        public MyViewHolder(View v) {
            super(v);

            TextView_Post_Comment_Name_Item = v.findViewById(R.id.TextView_Post_Comment_Name_Item);
            TextView_Post_Comment_Time_Item = v.findViewById(R.id.TextView_Post_Comment_Time_Item);
            TextView_Post_Comment_Input_Item = v.findViewById(R.id.TextView_Post_Comment_Input_Item);

            v.setClickable(true);
            v.setEnabled(true);
            //v.setOnTouchListener(touchListener);
            v.setOnClickListener(cilicListener);
            rootView = v;
        }
    }

    public CommentAdaptor(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        cilicListener = click;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sns_comment_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        System.out.println("여기 들어와?? : " + position);
        CommentData pdata = data.get(position);

        holder.TextView_Post_Comment_Input_Item.setText(pdata.getContent());
        holder.TextView_Post_Comment_Name_Item.setText(pdata.getName());
        holder.TextView_Post_Comment_Time_Item.setText(pdata.getTime());

        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리
    }


    public void addData( CommentData dest) {
        if (dest != null) {
            data.add(dest);
            notifyDataSetChanged();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public CommentData getData(int pos) {
        return data != null ? data.get(pos) : null;
    }
}

