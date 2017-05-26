package com.example.administrator.bicycle.entity;

import java.util.List;

/**
 * Created by Administrator on 2017-04-25.
 */
public class MessageOutEntity {
    List<MessageInEntity> list;
    int code;

    public List<MessageInEntity> getList() {
        return list;
    }

    public void setList(List<MessageInEntity> list) {
        this.list = list;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

