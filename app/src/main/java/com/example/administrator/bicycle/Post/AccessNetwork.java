package com.example.administrator.bicycle.Post;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @dhl 2017-05-08
 */

public class AccessNetwork implements Runnable {

    private String op;
    private String url;
    private String params;
    private Handler h;
    private int what;


    public AccessNetwork(String op, String url, String params, Handler h, int what) {
        super();
        this.op = op;
        this.url = url;
        this.params = params;
        this.h = h;
        this.what = what;
    }

    @Override
    public void run() {
        Message m = new Message();
        m.what = what;
        if (op.equals("GET")) {
            Log.i("iiiiiii", "发送GET请求");
            m.obj = PostUtil.sendGet(url, params);
            Log.i("iiiiiii", ">>>>>>>>>>>>" + m.obj);
        }
        if (op.equals("POST")) {
            Log.i("iiiiiii", "发送POST请求");
            m.obj = PostUtil.sendPost(url, params);
            Log.i("gggggggg", ">>>>>>>>>>>>" + m.obj);
        }
        h.sendMessage(m);
    }
}
