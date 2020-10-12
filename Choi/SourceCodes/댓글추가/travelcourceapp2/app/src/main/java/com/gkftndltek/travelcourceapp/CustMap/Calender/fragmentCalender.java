package com.gkftndltek.travelcourceapp.CustMap.Calender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gkftndltek.travelcourceapp.CustMap.MainActivity;
import com.gkftndltek.travelcourceapp.R;



public class fragmentCalender extends Fragment {

    private View rootView;
    private CalendarView CalendarView;
    private Context con;

    private String token,name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_calender, container, false);
        // 리니어레이아웃
        init();
        return rootView;
    }

    void init(){


        con = getActivity();

        token = ((MainActivity) getActivity()).token;
        name = ((MainActivity) getActivity()).mapName;

        CalendarView = rootView.findViewById(R.id.CalendarView);
        CalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){

                DateClass data = new DateClass();

                data.setDay(dayOfMonth); data.setMonth(month); data.setYear(year);

                Intent intent = new Intent(getActivity(),CalenderListActivity.class);
                intent.putExtra("date",data);
                intent.putExtra("token",token);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

//    private String cvt(String data){
//        if(data.length() == 1) return "0" + data;
//        return data;
//    }

}
