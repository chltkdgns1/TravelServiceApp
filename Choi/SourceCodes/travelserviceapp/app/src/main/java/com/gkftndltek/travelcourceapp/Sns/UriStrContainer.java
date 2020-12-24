package com.gkftndltek.travelcourceapp.Sns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UriStrContainer implements Serializable {
    private List<String> list;
    public UriStrContainer(){
        list = new ArrayList<>();
    }

    public String getAt(int idx){
        return list.get(idx);
    }

    public void push(String data){
        list.add(data);
    }

    public void clear(){
        list.clear();
    }

    public int size(){
        return list.size();
    }
}