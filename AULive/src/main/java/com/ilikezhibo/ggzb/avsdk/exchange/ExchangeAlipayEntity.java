package com.ilikezhibo.ggzb.avsdk.exchange;

/**
 * Created by stephensun on 2017/2/11.
 */
public class ExchangeAlipayEntity {

    /**
     * stat : 200
     * msg : 兑换成功
     * last_diamond : 70749
     * money : 0
     */

    private int stat;
    private String msg;
    private int last_diamond;
    private int money;

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getLast_diamond() {
        return last_diamond;
    }

    public void setLast_diamond(int last_diamond) {
        this.last_diamond = last_diamond;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
