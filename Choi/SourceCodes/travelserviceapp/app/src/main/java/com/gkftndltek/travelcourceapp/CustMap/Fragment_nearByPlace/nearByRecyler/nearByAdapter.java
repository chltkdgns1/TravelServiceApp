package com.gkftndltek.travelcourceapp.CustMap.Fragment_nearByPlace.nearByRecyler;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.gkftndltek.travelcourceapp.CustMap.Fragment_nearByPlace.placeFindData;
import com.gkftndltek.travelcourceapp.R;
import java.util.ArrayList;
import java.util.List;

public class nearByAdapter extends RecyclerView.Adapter<nearByAdapter.MyViewHolder> {
    private List<placeFindData> data;

    private static View.OnClickListener clickListener; //목록 클릭시 이벤트 상황 처리
    private static View.OnTouchListener touchListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private View rootView; //목록 클릭시 이벤트 상황 처리

        private TextView TextView_MyList_Name_nearby,TextView_MyList_Address_nearby;
        private ImageView ImageView_MyList_Picture_nearby;
        private LinearLayout LinearLayout_Background_nearBy;

        public MyViewHolder(View v) {
            super(v);

            TextView_MyList_Name_nearby = v.findViewById(R.id. TextView_MyList_Name_nearby);
            TextView_MyList_Address_nearby = v.findViewById(R.id.TextView_MyList_Address_nearby);
            LinearLayout_Background_nearBy = v.findViewById(R.id.LinearLayout_Background_nearBy);
            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(clickListener);  //목록 클릭시 이벤트 상황 처리
            rootView = v;
        }
    }


    public nearByAdapter(View.OnClickListener click) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        clickListener = click;
    }

    public nearByAdapter(View.OnTouchListener touch) { // 찾아온 목록 클릭했을 때의 이벤트 처리 생성자 상태
        data = new ArrayList<>();
        touchListener = touch;
    }


    /*
        public DestinationAdapter() {
            data = new ArrayList<>();
        }
    */
    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearbyplace_items, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        placeFindData inputData = data.get(position);

  //      holder.ImageView_MyList_Picture_nearby.setImageBitmap(inputData.getBit());

        Drawable drawable = new BitmapDrawable(inputData.getBit());
        holder.LinearLayout_Background_nearBy.setBackground(drawable);
        holder. TextView_MyList_Name_nearby.setText(inputData.getTitle());
        holder.TextView_MyList_Address_nearby.setText(inputData.getAddres());
        holder.rootView.setTag(position); // 목록 클릭시 이벤트 상황 처리

    }

    public void addData(placeFindData dest) {
        if (dest != null) {
            data.add(dest);
            System.out.println("버그찾았다이새키 : " + Integer.toString(data.size() - 1));
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

    public placeFindData getData(int pos) {
        return data != null ? data.get(pos) : null;
    }
}