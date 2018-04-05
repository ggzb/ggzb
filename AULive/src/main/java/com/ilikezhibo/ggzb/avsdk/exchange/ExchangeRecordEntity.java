package com.ilikezhibo.ggzb.avsdk.exchange;

import java.util.List;

/**
 * Created by stephensun on 2017/2/11.
 */
public class ExchangeRecordEntity {

    /**
     * data : [{"add_time":"1486779440","ali_account":"123","ali_realname":"哈哈","id":"173518","last_diamond":"70749","mch_billno":"","money":"0","openid":"","send_listid":"","status":"0","type":"2","ucoins":"1","uid":"17993425"}]
     * msg :
     * stat : 200
     */

    private String msg;
    private int stat;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * add_time : 1486779440
         * ali_account : 123
         * ali_realname : 哈哈
         * id : 173518
         * last_diamond : 70749
         * mch_billno :
         * money : 0
         * openid :
         * send_listid :
         * status : 0
         * type : 2
         * ucoins : 1
         * uid : 17993425
         */

        private String add_time;
        private String ali_account;
        private String ali_realname;
        private String id;
        private String last_diamond;
        private String mch_billno;
        private String money;
        private String openid;
        private String send_listid;
        private String status;
        private String type;
        private String ucoins;
        private String uid;

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getAli_account() {
            return ali_account;
        }

        public void setAli_account(String ali_account) {
            this.ali_account = ali_account;
        }

        public String getAli_realname() {
            return ali_realname;
        }

        public void setAli_realname(String ali_realname) {
            this.ali_realname = ali_realname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLast_diamond() {
            return last_diamond;
        }

        public void setLast_diamond(String last_diamond) {
            this.last_diamond = last_diamond;
        }

        public String getMch_billno() {
            return mch_billno;
        }

        public void setMch_billno(String mch_billno) {
            this.mch_billno = mch_billno;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSend_listid() {
            return send_listid;
        }

        public void setSend_listid(String send_listid) {
            this.send_listid = send_listid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUcoins() {
            return ucoins;
        }

        public void setUcoins(String ucoins) {
            this.ucoins = ucoins;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
