package com.gkftndltek.travelcourceapp.Sns.Options.FragmentFollow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.R;


public class FragmentFollow extends Fragment {

    private View rootView;

    // 뷰

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sns_follow, container, false);
        // 리니어레이아웃

        init();
        return rootView;
    }

    void init(){

    }
}
