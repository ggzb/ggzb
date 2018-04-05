package com.ilikezhibo.ggzb.avsdk.userinfo.toprank;

import com.ilikezhibo.ggzb.BaseEntity;

import java.util.ArrayList;

/**
 * Created by stephensun on 2017/2/16.
 */
public class TopRankDayEntity extends BaseEntity {
    private ArrayList<TopRankEntity> list;

    public ArrayList<TopRankEntity> getList() {
        return list;
    }

    public void setList(ArrayList<TopRankEntity> list) {
        this.list = list;
    }

    public int total;


}
