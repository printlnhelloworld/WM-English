package com.example.yzt.wm_english.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YZT on 2017/1/26.
 */

public class Status {
    public String nickName;
    private int status;
    @SerializedName("userId")
    private int id;
    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getid() {
        return id;
    }
}
