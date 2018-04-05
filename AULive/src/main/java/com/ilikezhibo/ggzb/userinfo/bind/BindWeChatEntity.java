package com.ilikezhibo.ggzb.userinfo.bind;

/**
 * Created by stephensun on 2017/2/9.
 */
public class BindWeChatEntity{

    /**
     * msg : 绑定成功
     * stat : 200
     */

    private String msg;
    private int stat;

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
