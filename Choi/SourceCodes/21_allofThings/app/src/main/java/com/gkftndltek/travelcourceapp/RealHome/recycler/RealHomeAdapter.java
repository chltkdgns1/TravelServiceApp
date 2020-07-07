package com.gkftndltek.travelcourceapp.RealHome.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;
import com.gkftndltek.travelcourceapp.Sns.PostViewPagerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RealHomeAdapter extends RecyclerView.Adapter<RealHomeAdapter.MyViewHolder> {
    private List<BitmapPostData> data;

    private FirebaseDatabase database;
    private DatabaseReference fref;

    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private View rootView; //목록 클릭시 이벤트 상황 처리

        private ImageView ImageView_Real_Home_Profile, ImageView_Real_Home_like, ImageView_Real_Home_Comment;
        private TextView TextView_Real_Home_Name, TextView_Real_Home_time, TextView_Real_Home_Content, TextView_Real_Home_Likes;
        private ViewPager ViewPager_Real_Home;

        private PostViewPagerAdapter adapter;

        public MyViewHolder(View v) {
            super(v);

            ImageView_Real_Home_Profile = v.findViewById(R.id.ImageView_Real_Home_Profile);
            ImageView_Real_Home_like = v.findViewById(R.id.ImageView_Real_Home_like);
            ImageView_Real_Home_Comment = v.findViewById(R.id.ImageView_Real_Home_Comment);

            TextView_Real_Home_Likes = v.findViewById(R.id.TextView_Real_Home_Likes);
            TextView_Real_Home_time = v.findViewById(R.id.TextView_Real_Home_time);
            TextView_Real_Home_Content = v.findViewById(R.id.TextView_Real_Home_Content);
            TextView_Real_Home_Name = v.findViewById(R.id.TextView_Real_Home_Name);

            ViewPager_Real_Home = v.findViewById(R.id.ViewPager_Real_Home);
            ViewPager_Real_Home.setClipToPadding(false);

            adapter = new PostViewPagerAdapter(v.getContext());


            v.setClickable(true);
            v.setEnabled(true);
            //v.setOnTouchListener(touchListener);
            v.setOnClickListener(cilicListener);
            rootView = v;
        }
    }

    public RealHomeAdapter(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }

    public  RealHomeAdapter(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        cilicListener = click;
        database = FirebaseDatabase.getInstance();
        fref = database.getReference();
    }

    public  RealHomeAdapter() {
        data = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.real_home_recycler_item, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BitmapPostData  pdata = data.get(position);
        int sz = pdata.getImageSize();

        holder.TextView_Real_Home_time.setText("7시간 전"); // 시간 차이 매겨주고
        holder.TextView_Real_Home_Content.setText(pdata.getDescription());
        holder.TextView_Real_Home_Name.setText(pdata.getNickname());

        fref.child("like").child(pdata.getNowNick()).child(pdata.getPath()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.ImageView_Real_Home_like.setImageResource(R.drawable.ic_liked);
                    holder.ImageView_Real_Home_like.setTag("liked");
                }
                else {
                    holder.ImageView_Real_Home_like.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        likeCheck(pdata.getPath(), holder);

        holder.ImageView_Real_Home_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = holder.ImageView_Real_Home_like.getTag().toString();
                if(tag == "liked"){
                    fref.child("liked").child(pdata.getPath()).child(pdata.getNowNick()).removeValue();
                    fref.child("like").child(pdata.getNowNick()).child(pdata.getPath()).removeValue();
                    likeCheck(pdata.getPath(),holder);
                    holder.ImageView_Real_Home_like.setImageResource(R.drawable.ic_like);
                    holder.ImageView_Real_Home_like.setTag("like");
                }
                else{
                    fref.child("liked").child(pdata.getPath()).child(pdata.getNowNick()).setValue(1);
                    fref.child("like").child(pdata.getNowNick()).child(pdata.getPath()).setValue(1);
                    likeCheck(pdata.getPath(),holder);
                    holder.ImageView_Real_Home_like.setImageResource(R.drawable.ic_liked);
                    holder.ImageView_Real_Home_like.setTag("liked");
                }
            }
        });

        holder.adapter.clear();
        for (int i = 0; i < sz; i++) {
            holder.adapter.add(pdata.getImage(i));
        }
        holder.ViewPager_Real_Home.setAdapter(holder.adapter);
        if(sz == 0) holder.ViewPager_Real_Home.setVisibility(View.GONE);
        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리
    }

    public void likeCheck(final String path, final MyViewHolder holder) {

        fref.child("liked").child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long like = (long) snapshot.getChildrenCount();
                holder.TextView_Real_Home_Likes.setText(like + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }

    public void addData(BitmapPostData  dest) {
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

    public BitmapPostData  getData(int pos) {
        return data != null ? data.get(pos) : null;
    }
}

