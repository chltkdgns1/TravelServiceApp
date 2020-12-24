package com.gkftndltek.travelcourceapp.Sns.MeFragment.Follow.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FriendAdaptor extends RecyclerView.Adapter<FriendAdaptor.MyViewHolder> {
    private List<FriendsDataClass> data;

    private FirebaseDatabase database;
    private DatabaseReference fref;

    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private View rootView; //목록 클릭시 이벤트 상황 처리

        private ImageView ImageView_User_Image;
        private TextView Textview_User_Name;


        public MyViewHolder(View v) {
            super(v);

            ImageView_User_Image = v.findViewById(R.id.ImageView_User_Image);
            Textview_User_Name = v.findViewById(R.id.Textview_User_Name);

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(cilicListener);
            rootView = v;
        }

    }

    public FriendAdaptor(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }

    public FriendAdaptor(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        cilicListener = click;
        database = FirebaseDatabase.getInstance();
        fref = database.getReference();
    }

    public FriendAdaptor() {
        data = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sns_follow_items, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        FriendsDataClass fd = data.get(position);

        if(fd.getImageUrl() != null) {
            holder.ImageView_User_Image.setImageBitmap(fd.getImageUrl());
        }
        else holder.ImageView_User_Image.setImageResource(fd.getImageName());
        holder.Textview_User_Name.setText(fd.getUsername());
        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리
    }

    public void addData(FriendsDataClass dest) {
        if (dest != null) {
            data.add(dest);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public FriendsDataClass getData(int pos) {
        return data != null ? data.get(pos) : null;
    }

}
