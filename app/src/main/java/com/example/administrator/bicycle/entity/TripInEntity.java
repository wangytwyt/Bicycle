package com.example.administrator.bicycle.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017-04-25.
 */

public class TripInEntity {
    String id;
    String much;
    String time;
    Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMuch() {
        return much;
    }

    public void setMuch(String much) {
        this.much = much;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
