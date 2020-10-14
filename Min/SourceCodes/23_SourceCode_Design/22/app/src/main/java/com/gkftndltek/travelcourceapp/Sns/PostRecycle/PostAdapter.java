package com.gkftndltek.travelcourceapp.Sns.PostRecycle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<BitmapPostData> data;
    private FirebaseDatabase database;
    private DatabaseReference fref;

    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ImageView_Profile, ImageView_Post_like, ImageView_Post_Comment, ImageView_Post_Save;
        TextView TextView_Post_Name, TextView_Post_time, TextView_Content, TextView_Post_Likes;
        ViewPager ViewPager_Post;

        PostViewPagerAdapter adapter;

        private static final int DP = 24;

        private View rootView; //목록 클릭시 이벤트 상황 처리

        public MyViewHolder(View v) {
            super(v);

            ImageView_Profile = v.findViewById(R.id.ImageView_Profile);
            ImageView_Post_like = v.findViewById(R.id.ImageView_Post_like);
            ImageView_Post_Comment = v.findViewById(R.id.ImageView_Post_Comment);
            ImageView_Post_Save = v.findViewById(R.id.ImageView_Post_Save);

           // ImageView_Post_Save.setOnClickListener(cilicListener);

            TextView_Post_Likes = v.findViewById(R.id.TextView_Post_Likes);
            TextView_Post_time = v.findViewById(R.id.TextView_Post_time);
            TextView_Content = v.findViewById(R.id.TextView_Content);
            TextView_Post_Name = v.findViewById(R.id.TextView_Post_Name);

            ViewPager_Post = v.findViewById(R.id.ViewPager_Post);
            ViewPager_Post.setClipToPadding(false);


//            float density = v.getResources().getDisplayMetrics().density;
////            int margin = (int) (DP * density);
////            ViewPager_Post.setPadding(margin, 0, margin, 0);
////            ViewPager_Post.setPageMargin(margin/2);

            adapter = new PostViewPagerAdapter(v.getContext());
            v.setClickable(true);
            v.setEnabled(true);
            //v.setOnTouchListener(touchListener);
            v.setOnClickListener(cilicListener);

      //      TextView_Post_Likes.setOnClickListener(cilicListener);

            rootView = v;
        }
    }


    public PostAdapter(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }

    public PostAdapter(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        cilicListener = click;
        database = FirebaseDatabase.getInstance();
        fref = database.getReference();
    }

    public PostAdapter() {
        data = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sns_post_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)



    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        final BitmapPostData pdata = data.get(position);

        int sz = pdata.getImageSize();

        // 리사이클러뷰 구현

        holder.TextView_Post_time.setText("7시간 전"); // 시간 차이 매겨주고
        holder.TextView_Content.setText(pdata.getDescription());
        holder.TextView_Post_Name.setText(pdata.getNickname());

        fref.child("like").child(pdata.getNowNick()).child(pdata.getPath()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.ImageView_Post_like.setImageResource(R.drawable.ic_liked);
                    holder.ImageView_Post_like.setTag("liked");
                }
                else {
                    holder.ImageView_Post_like.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        fref.child("save").child(pdata.getNowNick()).child(pdata.getPath()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.ImageView_Post_Save.setImageResource(R.drawable.ic_save_black);
                    holder.ImageView_Post_Save.setTag("saved");
                }
                else holder.ImageView_Post_Save.setTag("save");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        likeCheck(pdata.getPath(), holder);

        holder.ImageView_Post_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = holder.ImageView_Post_like.getTag().toString();
                if(tag == "liked"){
                    fref.child("liked").child(pdata.getPath()).child(pdata.getNowNick()).removeValue();
                    fref.child("like").child(pdata.getNowNick()).child(pdata.getPath()).removeValue();
                    likeCheck(pdata.getPath(),holder);
                    holder.ImageView_Post_like.setImageResource(R.drawable.ic_like);
                    holder.ImageView_Post_like.setTag("like");
                }
                else{
                    fref.child("liked").child(pdata.getPath()).child(pdata.getNowNick()).setValue(1);
                    fref.child("like").child(pdata.getNowNick()).child(pdata.getPath()).setValue(1);
                    likeCheck(pdata.getPath(),holder);
                    holder.ImageView_Post_like.setImageResource(R.drawable.ic_liked);
                    holder.ImageView_Post_like.setTag("liked");
                }
            }
        });

        holder.ImageView_Post_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = holder.ImageView_Post_Save.getTag().toString();
                if(tag == "save"){
                    fref.child("save").child(pdata.getNowNick()).child(pdata.getPath()).setValue(1);
                    holder.ImageView_Post_Save.setImageResource(R.drawable.ic_save_black);
                    holder.ImageView_Post_Save.setTag("saved");
                }
                else{
                    fref.child("save").child(pdata.getNowNick()).child(pdata.getPath()).removeValue();
                    holder.ImageView_Post_Save.setImageResource(R.drawable.ic_savee_black);
                    holder.ImageView_Post_Save.setTag("save");
                }
            }
        });

        holder.adapter.clear();
        for (int i = 0; i < sz; i++) {
            System.out.println("들어오는거 맞죠.." + pdata.getImage(i).toString());
            holder.adapter.add(pdata.getImage(i));
        }

        holder.ViewPager_Post.setAdapter(holder.adapter);

        if(sz == 0) holder.ViewPager_Post.setVisibility(View.GONE);

        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리

    }


    public void likeCheck(final String path, final MyViewHolder holder) {

        fref.child("liked").child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long like = (long) snapshot.getChildrenCount();
                holder.TextView_Post_Likes.setText(like + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }


    public void addData(BitmapPostData dest) {
        if (dest != null) {
            data.add(dest);
            System.out.println("너 몇번째야? " + data.size());
            notifyItemInserted(data.size() - 1); // 갱신용
        }
    }

    // Return the size of your dataset (invoked by the layout manager)

    public void clear() {
        data.clear();
        notifyDataSetChanged();
        // notifyDataSetChanged();
        //  notifyItemRangeRemoved(0, data.size() - 1);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public BitmapPostData getData(int pos) {
        return data != null ? data.get(pos) : null;
    }
}
