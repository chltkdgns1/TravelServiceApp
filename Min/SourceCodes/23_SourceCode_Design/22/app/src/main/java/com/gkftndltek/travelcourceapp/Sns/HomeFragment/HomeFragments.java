package com.gkftndltek.travelcourceapp.Sns.HomeFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.R;

public class HomeFragments extends Fragment {

    private View rootView;
    private Context con;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sns_home, container, false);
        // 리니어레이아웃

        init();
        return rootView;
    }

    void init() {
        con = getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // ((MainActivity)context).setOnBackPressedListener(this); // 프레그먼트
    }
}

