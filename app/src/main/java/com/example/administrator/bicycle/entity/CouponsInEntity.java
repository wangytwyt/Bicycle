package com.example.administrator.bicycle.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017-04-25.
 */

public class CouponsInEntity {
    String name;
    Date date;
    String much;
    String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMuch() {
        return much;
    }

    public void setMuch(String much) {
        this.much = much;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
