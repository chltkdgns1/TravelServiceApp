package com.gkftndltek.travelcourceapp.Sns.PostRecycle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.gkftndltek.travelcourceapp.KakaoLog.UserData;
import com.gkftndltek.travelcourceapp.R;
import com.gkftndltek.travelcourceapp.Sns.BitmapPostData;
import com.gkftndltek.travelcourceapp.Sns.MainActivity;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.Comment.CommentAdaptor;
import com.gkftndltek.travelcourceapp.Sns.PostRecycle.Comment.CommentData;
import com.gkftndltek.travelcourceapp.Sns.PostViewPagerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<BitmapPostData> data;
    private FirebaseDatabase database;
    public static DatabaseReference fref;
    public static String name,token;
    public static Context con;
    public static int resid;

    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ImageView_Profile, ImageView_Post_like, ImageView_Post_Comment, ImageView_Post_Save;
        TextView TextView_Post_Name, TextView_Post_time, TextView_Content, TextView_Post_Likes;

        RecyclerView recyclerView;
        ViewPager ViewPager_Post;

        PostViewPagerAdapter adapter;

        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager;

        LinearLayout LinearLayout_Comment_Input;
        TextView TextView_Post_Comment_Name, TextView_Post_Comment_Complete;
        EditText EditText_Post_Comment_Input;
        ImageView CircleImageView_Comment_Profile_Input;

        ChildEventListener childEventListener = null;

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

            TextView_Post_Comment_Name = rootView.findViewById(R.id.TextView_Post_Comment_Name);
            TextView_Post_Comment_Name.setText(name);
            TextView_Post_Comment_Complete = rootView.findViewById(R.id.TextView_Post_Comment_Complete);
            TextView_Post_Comment_Complete.setClickable(true);
            EditText_Post_Comment_Input = rootView.findViewById(R.id.EditText_Post_Comment_Input);
            LinearLayout_Comment_Input = rootView.findViewById(R.id.LinearLayout_Comment_Input);
            CircleImageView_Comment_Profile_Input = rootView.findViewById(R.id.CircleImageView_Comment_Profile_Input);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_Post_Comment);

            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(v.getContext());

            recyclerView.setLayoutManager(layoutManager);

            //mAdapter = new DestinationAdapter(); // 원래 기존에 사용하던 것

            // mAdapter = new MakeMapAdapter();


            fref.child("users").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserData udata = dataSnapshot.getValue(UserData.class);
                    String image = udata.getImageName();

                    resid = con.getResources().getIdentifier(image,"drawable",con.getPackageName());
                    CircleImageView_Comment_Profile_Input.setImageResource( resid);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mAdapter = new CommentAdaptor(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        synchronized (this) {
                            int position = (int) v.getTag();
                            // 삭제하시겠습니까?
                        }
                    }
                }
            });

            recyclerView.setAdapter(mAdapter);

            ImageView_Post_Comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (recyclerView.getVisibility() == View.GONE)
                        recyclerView.setVisibility(View.VISIBLE);
                    else recyclerView.setVisibility(View.GONE);

                    if (LinearLayout_Comment_Input.getVisibility() == View.GONE)
                        LinearLayout_Comment_Input.setVisibility(View.VISIBLE);
                    else LinearLayout_Comment_Input.setVisibility(View.GONE);

                }
            });
        }
    }


    public PostAdapter(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }

    public PostAdapter(Context con, View.OnClickListener click, String name, String token) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        cilicListener = click;
        database = FirebaseDatabase.getInstance();
        fref = database.getReference();
        this.name = name;
        this.token = token;
        this.con = con;
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

        holder.TextView_Post_Comment_Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = holder.EditText_Post_Comment_Input.getText().toString();
                if (t.isEmpty()) return;

                holder.EditText_Post_Comment_Input.setText("");

                CommentData data = new CommentData();
                data.setName(name);
                data.setContent(t);
                data.setImageName(resid);

                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )

                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String formatDate = sdfNow.format(date);
                data.setTime(formatDate);

                fref.child("post").child(pdata.getNickname()).child(pdata.getPath()).child("comment").push().setValue(data);
//                    System.out.println("name : " + name + " comment : " + t);
//
//                    ((CommentAdaptor)mAdapter).addData(data);
//                    System.out.println("몇갠데 도대체 안보이는거야 시발 : " + ((CommentAdaptor)mAdapter).getItemCount());
            }
        });

        if (holder.childEventListener == null) {
            holder.childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    ((CommentAdaptor) holder.mAdapter).addData(dataSnapshot.getValue(CommentData.class));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            fref.child("post").child(pdata.getNickname()).child(pdata.getPath()).child("comment").addChildEventListener(holder.childEventListener);
        }


//        holder.ImageView_Post_Comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(holder.recyclerView.getVisibility() == View.GONE)
//                    holder.recyclerView.setVisibility(View.VISIBLE);
//                else  holder.recyclerView.setVisibility(View.GONE);
//
//                if( holder.LinearLayout_Comment_Input.getVisibility() == View.GONE)
//                    holder.LinearLayout_Comment_Input.setVisibility(View.VISIBLE);
//                else holder.LinearLayout_Comment_Input.setVisibility(View.GONE);
//
//            }
//        });
//
//
//        holder.TextView_Post_Comment_Complete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        int sz = pdata.getImageSize();

        // 리사이클러뷰 구현

        holder.TextView_Post_time.setText(getTime(pdata.getTimes())); // 시간 차이 매겨주고


        holder.TextView_Content.setText(pdata.getDescription());
        holder.TextView_Post_Name.setText(pdata.getNickname());
        holder.ImageView_Profile.setImageResource(pdata.getImageData());

        fref.child("like").child(pdata.getNowNick()).child(pdata.getPath()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.ImageView_Post_like.setImageResource(R.drawable.ic_liked);
                    holder.ImageView_Post_like.setTag("liked");
                } else {
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
                } else holder.ImageView_Post_Save.setTag("save");
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
                if (tag == "liked") {
                    fref.child("liked").child(pdata.getPath()).child(pdata.getNowNick()).removeValue();
                    fref.child("like").child(pdata.getNowNick()).child(pdata.getPath()).removeValue();
                    likeCheck(pdata.getPath(), holder);
                    holder.ImageView_Post_like.setImageResource(R.drawable.ic_like);
                    holder.ImageView_Post_like.setTag("like");
                } else {
                    fref.child("liked").child(pdata.getPath()).child(pdata.getNowNick()).setValue(1);
                    fref.child("like").child(pdata.getNowNick()).child(pdata.getPath()).setValue(1);
                    likeCheck(pdata.getPath(), holder);
                    holder.ImageView_Post_like.setImageResource(R.drawable.ic_liked);
                    holder.ImageView_Post_like.setTag("liked");
                }
            }
        });

        holder.ImageView_Post_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = holder.ImageView_Post_Save.getTag().toString();
                if (tag == "save") {
                    fref.child("save").child(pdata.getNowNick()).child(pdata.getPath()).setValue(1);
                    holder.ImageView_Post_Save.setImageResource(R.drawable.ic_save_black);
                    holder.ImageView_Post_Save.setTag("saved");
                } else {
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

        if (sz == 0) holder.ViewPager_Post.setVisibility(View.GONE);

        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리

    }


    public String getTime(long times) {
        long nowTime = System.currentTimeMillis();
        System.out.println("nowTime : " + nowTime + " timees : " + times);
        if (nowTime - times >= 3600000 * 24) { // 게시글 올린지 1일이 지남
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 HH일 mm분");
            Date now = new Date();
            return formatter.format(now);
        }

        else{ // 1일 이내
            nowTime -= times;
            if(nowTime < 3600000){
                if(nowTime < 60000){
                    return Long.toString(nowTime / 1000) + "초 전";
                }
                return Long.toString(nowTime / 60000) + "분 전";
            }
            String hour = Long.toString(nowTime / 3600000);
            return hour + "시간 전";
        }
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
