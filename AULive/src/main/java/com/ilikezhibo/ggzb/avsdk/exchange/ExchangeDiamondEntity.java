package com.ilikezhibo.ggzb.avsdk.exchange;

/**
 * Created by stephensun on 2017/2/11.
 */
public class ExchangeDiamondEntity {

    /**
     * diamond : 122
     * last_diamond : 70549
     * msg : 兑换成功
     * stat : 200
     */

    private int diamond;
    private int last_diamond;
    private String msg;
    private int stat;

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getLast_diamond() {
        return last_diamond;
    }

    public void setLast_diamond(int last_diamond) {
        this.last_diamond = last_diamond;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }
}
