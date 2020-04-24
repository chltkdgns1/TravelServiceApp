package com.gkftndltek.travelcourceapp;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DestinationsClass {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public DestinationsClass(){}

    void execute(Activity act,  Context con){

        recyclerView = (RecyclerView) act.findViewById(R.id.RecyclerView_Destination_List);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(con);

        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DestinationAdapter();

        recyclerView.setAdapter(mAdapter);
    }

    void add(DestinationDataClass data){
        ((DestinationAdapter) mAdapter).addDestData(data);
    }

    void clear(){
        ((DestinationAdapter) mAdapter).clear();
    }
}
