package com.example.sns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private List<BitmapPostData > data;

    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;
    private String user="a";
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button Button_show,Button_Send;
        ImageView ImageView_Profile;
        TextView TextView_Post_Title,TextView_Post_Id,TextView_Post_time,TextView_Content;

        EditText EditText_comment;
        ViewPager ViewPager_Post;
        PostViewPagerAdapter adapter;

        RecyclerView recyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager;


        List<CommentData> list;

        private static final int DP = 24;

        private View rootView; //목록 클릭시 이벤트 상황 처리

        public MyViewHolder(View v) {
            super(v);
            Button_show = v.findViewById(R.id.Button_show);
            ImageView_Profile = v.findViewById(R.id.ImageView_Profile);
            TextView_Post_Title = v.findViewById(R.id.TextView_Post_Title);
            TextView_Post_Id = v.findViewById(R.id.TextView_Post_Id);
            TextView_Post_time = v.findViewById(R.id.TextView_Post_time);
            TextView_Content = v.findViewById(R.id.TextView_Content);

            ViewPager_Post = v.findViewById(R.id.ViewPager_Post);
            ViewPager_Post.setClipToPadding(false);

            adapter = new PostViewPagerAdapter(v.getContext());


            EditText_comment = v.findViewById(R.id.EditText_comment);
            Button_Send = v.findViewById(R.id.Button_Send);

            recyclerView = (RecyclerView) v.findViewById(R.id.comment_recyclerview);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(v.getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setVisibility(View.GONE);

            list = new ArrayList<>();
            mAdapter = new CommentAdapter(list);

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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        BitmapPostData  pdata =  data.get(position);

        int sz = pdata.getImageSize();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        holder.TextView_Post_Title.setText(pdata.getTitle());
        holder.TextView_Post_Id.setText(pdata.getUsername());
        holder.TextView_Post_time.setText("7시간 전");
        holder.TextView_Content.setText(pdata.getContents());

        System.out.println(pdata.getTitle());
        System.out.println(pdata.getUsername());
        System.out.println(pdata.getContents());
        System.out.println("사이즈가 어떻게 되죠? " + sz);

        holder.adapter.clear();

        for(int i=0;i<sz;i++){
            System.out.println("들어오는거 맞죠.." + pdata.getImage(i).toString());
            holder.adapter.add(pdata.getImage(i));
        }

        holder.ViewPager_Post.setAdapter(holder.adapter);
        holder.recyclerView.setAdapter(holder.mAdapter);

        databaseReference.child("post").child("a").child("20201010_0831 2").child("comment").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {

                            holder.list.add(Snapshot.getValue(CommentData.class));
                        }
                        //holder.mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("Error");
                    }
                }
        );

        holder.Button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(holder.recyclerView.getVisibility() == View.VISIBLE) holder.recyclerView.setVisibility(View.GONE);
                else holder.recyclerView.setVisibility(View.VISIBLE);

            }
        });

        holder.Button_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("comment button clicked");
                String str = holder.EditText_comment.getText().toString();
                if(str.isEmpty()) return;

                System.out.println(str);

                CommentData commentData = new CommentData(1,"User?",str);
                ((CommentAdapter)holder.mAdapter).add(commentData);

                databaseReference.child("post").child("a").child("20201010_0831 2").child("comment").push().setValue(commentData); // a 다음 primarykey
/*
                databaseReference.child("post").child("a").child("20201010_0831 2").child("comment").child("comment_Profile").push().setValue(commentData.getComment_profile());
                databaseReference.child("post").child("a").child("20201010_0831 2").child("comment").child("comment_Username").push().setValue(commentData.getComment_username());
                databaseReference.child("post").child("a").child("20201010_0831 2").child("comment").child("comment_Contents").push().setValue(commentData.getComment_contents());

 */


            }
        });


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
