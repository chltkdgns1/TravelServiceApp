package com.gkftndltek.snstravelapp.PostRecycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gkftndltek.snstravelapp.BitmapPostData;
import com.gkftndltek.snstravelapp.PostViewPagerAdapter;
import com.gkftndltek.snstravelapp.R;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<BitmapPostData > data;

    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ImageView_Profile,ImageView_Post_like,ImageView_Post_Comment,ImageView_Post_Save;
        TextView TextView_Post_Name,TextView_Post_time,TextView_Content,TextView_Post_Likes;
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
    }

    public PostAdapter() {
        data = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BitmapPostData  pdata =  data.get(position);

        int sz = pdata.getImageSize();

        holder.TextView_Post_time.setText("7시간 전");
        holder.TextView_Content.setText(pdata.getDescription());
        holder.TextView_Post_Name.setText(pdata.getNickname());

        holder.adapter.clear();
        for(int i=0;i<sz;i++){
            System.out.println("들어오는거 맞죠.." + pdata.getImage(i).toString());
            holder.adapter.add(pdata.getImage(i));
        }

        holder.ViewPager_Post.setAdapter(holder.adapter);

//        alsdnjsrl234.setVideoURI(uri);
//        alsdnjsrl234.start();
//
//        holder.ImageView_Post_Video.setVideoURI(pdata.getVideo());
//        holder.ImageView_Post_Video.start();
//        holder.ImageView_profile.setImageBitmap(pdata.getImage());
//        holder.TextView_Showtitle.setText(pdata.getTitle());
//        holder.TextView_userName.setText(pdata.getUsername());
//        holder.TextView_Showcontents.setText(pdata.getContents());

        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리
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
