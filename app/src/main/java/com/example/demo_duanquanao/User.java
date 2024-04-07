package com.example.demo_duanquanao;

public class User {
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

    public User(String username, String fullname, String password, String imageuser) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.imageuser = imageuser;
    }

    public User() {

    }

    String username;
    String fullname;
    String password;

    public String getImageuser() {
        return imageuser;
    }

    public void setImageuser(String imageuser) {
        this.imageuser = imageuser;
    }

    String imageuser;

}
