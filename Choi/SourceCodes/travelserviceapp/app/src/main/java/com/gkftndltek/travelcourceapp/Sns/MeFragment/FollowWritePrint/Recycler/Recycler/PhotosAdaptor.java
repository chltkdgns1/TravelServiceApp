package com.gkftndltek.travelcourceapp.Sns.MeFragment.FollowWritePrint.Recycler.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PhotosAdaptor extends RecyclerView.Adapter<PhotosAdaptor.MyViewHolder> {

    private List<PhotosData> data;
    private FirebaseDatabase database;
    private DatabaseReference fref;


    //    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;
    private static View.OnClickListener cilicListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView ImageView_Photos;

        private View rootView; //목록 클릭시 이벤트 상황 처리

        public MyViewHolder(View v) {
            super(v);

            ImageView_Photos = v.findViewById(R.id.ImageView_Photos);

            v.setClickable(true);
            v.setEnabled(true);
            //v.setOnTouchListener(touchListener);
            v.setOnClickListener(cilicListener);
            rootView = v;
        }
    }


    public PhotosAdaptor(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }

    public PhotosAdaptor(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        cilicListener = click;
        database = FirebaseDatabase.getInstance();
        fref = database.getReference();
    }

    public PhotosAdaptor() {
        data = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sns_photos_items, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        System.out.println("왜 안들어옵니까.." + position);
        final PhotosData pdata = data.get(position);
        holder.ImageView_Photos.setImageBitmap(pdata.getData());
        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리
    }


    public void addData(PhotosData dest) {
        if (dest != null) {
            System.out.println("잘들어오는데 왜 안되 시발아?");
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

    public PhotosData getData(int pos) {
        return data != null ? data.get(pos) : null;
    }
}
