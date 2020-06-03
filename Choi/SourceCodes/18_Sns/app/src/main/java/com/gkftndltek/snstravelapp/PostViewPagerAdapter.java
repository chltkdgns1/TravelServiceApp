package com.gkftndltek.snstravelapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class PostViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Bitmap> imageList;

    public PostViewPagerAdapter(Context context)
    {
        this.mContext = context;
        imageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.postviewpager_item, null);

        ImageView ImageView_ViewPager = view.findViewById(R.id.ImageView_Post_ViewPager);
        ImageView_ViewPager.setImageBitmap(imageList.get(position));


        container.addView(view);

        return view;
    }

    public void add(Bitmap data){
        imageList.add(data);
    }
    public void clear() {imageList.clear();}
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }
}

