package com.example.coooooment;

public class User {
    private String Name;
    private String Comment;
    private String Time;

    public User() {
    }

    public User(String name, String comment, String time) {
        Name = name;
        Comment = comment;
        Time = time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
