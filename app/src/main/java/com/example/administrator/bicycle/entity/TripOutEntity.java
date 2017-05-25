package com.example.administrator.bicycle.entity;

import java.util.List;

/**
 * Created by Administrator on 2017-04-25.
 */

public class TripOutEntity {
    List<TripInEntity>  inEntities;
    int code;

    public List<TripInEntity> getInEntities() {
        return inEntities;
    }

    public void setInEntities(List<TripInEntity> inEntities) {
        this.inEntities = inEntities;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
