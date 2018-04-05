package com.ilikezhibo.ggzb.avsdk.exchange;

/**
 * Created by stephensun on 2017/2/21.
 */
public class ExchangeBankEntity {

    /**
     * last_diamond : 967
     * money : 1
     * msg : 兑换成功
     * stat : 200
     */

    private int last_diamond;
    private int money;
    private String msg;
    private int stat;

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
