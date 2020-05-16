package com.alsdnjsrl.snsproject;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostInfo implements Serializable {
    private String title;
    private ArrayList<String> contents;
    private ArrayList<String> formats;
    private String publisher;
    private Date createdAt;
    private String id;

    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, String publisher, Date createdAt, String id){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.id = id;
    }

    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, String publisher, Date createdAt){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }

    public Map<String, Object> getPostInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("title",title);
        docData.put("contents",contents);
        docData.put("formats",formats);
        docData.put("publisher",publisher);
        docData.put("createdAt",createdAt);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = database.getReference("post").child("title");
        DatabaseReference databaseReference2 = database.getReference("post").child("contents");
        DatabaseReference databaseReference3 = database.getReference("post").child("formats");
        DatabaseReference databaseReference4 = database.getReference("post").child("publisher");
        DatabaseReference databaseReference5 = database.getReference("post").child("createdAt");
        databaseReference1.push().setValue(title);
        databaseReference2.push().setValue(contents);
        databaseReference3.push().setValue(formats);
        databaseReference4.push().setValue(publisher);
        databaseReference5.push().setValue(createdAt);
        return docData;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public ArrayList<String> getContents(){
        return this.contents;
    }
    public void setContents(ArrayList<String> contents){
        this.contents = contents;
    }
    public ArrayList<String> getFormats(){
        return this.formats;
    }
    public void setFormats(ArrayList<String> formats){
        this.formats = formats;
    }
    public String getPublisher(){
        return this.publisher;
    }
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public Date getCreatedAt(){
        return this.createdAt;
    }
    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }
    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
}