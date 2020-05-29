package com.gkftndltek.snstravelapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter  extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> {

    private List<BitmapPostData > data;

    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private View rootView; //목록 클릭시 이벤트 상황 처리
        private TextView TextView_Showtitle,TextView_userName,TextView_Showcontents;
        private ImageView ImageView_Post_Image,ImageView_profile;

        public MyViewHolder(View v) {
            super(v);

            TextView_Showtitle = v.findViewById(R.id.TextView_Showtitle);
            TextView_userName = v.findViewById(R.id.TextView_userName);
            TextView_Showcontents = v.findViewById(R.id.TextView_Showcontents);
            ImageView_Post_Image = v.findViewById(R.id.ImageView_Post_Image);
            ImageView_profile = v.findViewById(R.id.ImageView_profile);

            v.setClickable(true);
            v.setEnabled(true);
            //v.setOnTouchListener(touchListener);
            v.setOnClickListener(cilicListener);
            rootView = v;
        }
    }


    public ClientAdapter(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }

    public ClientAdapter(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        cilicListener = click;
    }

    public ClientAdapter() {
        data = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BitmapPostData  pdata =  data.get(position);

        Bitmap bit = pdata.getUrl();
        holder.ImageView_profile.setImageBitmap(bit);
        holder.ImageView_Post_Image.setImageBitmap(bit);
        holder.TextView_Showtitle.setText(pdata.getTitle());
        holder.TextView_userName.setText(pdata.getUsername());
        holder.TextView_Showcontents.setText(pdata.getContents());
        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리
    }

    public void addData(BitmapPostData dest) {
        if (dest != null) {
            data.add(dest);
            notifyItemInserted(data.size() - 1); // 갱신용
        }
    }


    // Return the size of your dataset (invoked by the layout manager)

    public void clear() {
        data.clear();
        notifyItemRangeRemoved(0, data.size() - 1);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public BitmapPostData getData(int pos) {
        return data != null ? data.get(pos) : null;
    }
}
