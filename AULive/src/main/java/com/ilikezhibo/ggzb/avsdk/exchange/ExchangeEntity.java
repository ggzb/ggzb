package com.ilikezhibo.ggzb.avsdk.exchange;


/**
 * Created by stephensun on 2017/2/10.
 */
public class ExchangeEntity {

    /**
     * alipay_status : 1
     * anchor_type : 0
     * certify : 0
     * diamond : 0
     * diamond_status : 1
     * exchange_diamond_rate : 2
     * exchange_money_rate : 33
     * family :
     * is_live : 0
     * lianlianpay_status : 1
     * msg :
     * recv_diamond : 48
     * stat : 200
     * uid : 18378762
     * wxpay_status : 1
     */

    private int alipay_status;
    private String anchor_type;
    private int certify;
    private String diamond;
    private int diamond_status;
    private int exchange_diamond_rate;
    private int exchange_money_rate;
    private String family;
    private String is_live;
    private int lianlianpay_status;
    private String msg;
    private int recv_diamond;
    private int stat;
    private int uid;
    private int wxpay_status;

    public int getAlipay_status() {
        return alipay_status;
    }

    public void setAlipay_status(int alipay_status) {
        this.alipay_status = alipay_status;
    }

    public String getAnchor_type() {
        return anchor_type;
    }

    public void setAnchor_type(String anchor_type) {
        this.anchor_type = anchor_type;
    }

    public int getCertify() {
        return certify;
    }

    public void setCertify(int certify) {
        this.certify = certify;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public int getDiamond_status() {
        return diamond_status;
    }

    public void setDiamond_status(int diamond_status) {
        this.diamond_status = diamond_status;
    }

    public int getExchange_diamond_rate() {
        return exchange_diamond_rate;
    }

    public void setExchange_diamond_rate(int exchange_diamond_rate) {
        this.exchange_diamond_rate = exchange_diamond_rate;
    }

    public int getExchange_money_rate() {
        return exchange_money_rate;
    }

    public void setExchange_money_rate(int exchange_money_rate) {
        this.exchange_money_rate = exchange_money_rate;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getIs_live() {
        return is_live;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
    }

    public int getLianlianpay_status() {
        return lianlianpay_status;
    }

    public void setLianlianpay_status(int lianlianpay_status) {
        this.lianlianpay_status = lianlianpay_status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRecv_diamond() {
        return recv_diamond;
    }

    public void setRecv_diamond(int recv_diamond) {
        this.recv_diamond = recv_diamond;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getWxpay_status() {
        return wxpay_status;
    }

    public void setWxpay_status(int wxpay_status) {
        this.wxpay_status = wxpay_status;
    }
}
