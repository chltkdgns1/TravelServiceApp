package com.gkftndltek.snstravelapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class UriStrContainer implements Serializable {
    private List<String> list;
    UriStrContainer(){
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