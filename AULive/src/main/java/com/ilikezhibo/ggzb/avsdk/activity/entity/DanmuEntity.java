package com.ilikezhibo.ggzb.avsdk.activity.entity;

/**
 * Created by stephensun on 2017/2/21.
 */
public class DanmuEntity {

    /**
     * diamond : 99
     * exp_value : 10
     * msg : ok
     * send_diamond : 1
     * stat : 200
     */

    private int diamond;
    private int exp_value;
    private String msg;
    private int send_diamond;
    private int stat;

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getExp_value() {
        return exp_value;
    }

    public void setExp_value(int exp_value) {
        this.exp_value = exp_value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSend_diamond() {
        return send_diamond;
    }

    public void setSend_diamond(int send_diamond) {
        this.send_diamond = send_diamond;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }
}
