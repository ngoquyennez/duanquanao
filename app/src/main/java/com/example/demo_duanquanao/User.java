package com.example.demo_duanquanao;

public class User {
    String username;
    String fullname;
    String password;
    String id;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String fullname, String password, String id) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.id = id;
    }
    public User() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
