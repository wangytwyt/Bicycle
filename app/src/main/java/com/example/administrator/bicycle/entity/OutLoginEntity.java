package com.example.administrator.bicycle.entity;

/**
 * Created by Administrator on 2017-05-05.
 */

public class OutLoginEntity {
    private int code;

    private User user;

    private String info;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return this.info;
    }
}
