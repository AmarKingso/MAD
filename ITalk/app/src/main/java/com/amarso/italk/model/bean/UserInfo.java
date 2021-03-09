package com.amarso.italk.model.bean;

import androidx.annotation.NonNull;

public class UserInfo {

    private String userid;
    private String nick;
    private String photo;

    public UserInfo() {}

    public UserInfo(String userid) {
        this.userid = userid;
        this.nick = "用户#" + userid;
        this.photo = userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserid() {
        return userid;
    }

    public String getNick() {
        return nick;
    }

    public String getPhoto() {
        return photo;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserInfo{" +
                "userid='" + userid + '\'' +
                ", nick='" + nick + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
