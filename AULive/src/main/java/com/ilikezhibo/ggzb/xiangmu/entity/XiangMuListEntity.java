package com.ilikezhibo.ggzb.xiangmu.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * @ClassName: GroupListEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-4-1 下午4:05:19
 * 
 */
public class XiangMuListEntity extends BaseEntity implements Serializable {
	private ArrayList<XiangMuEntity> list;

	public ArrayList<XiangMuEntity> getList() {
		return list;
	}

	public void setList(ArrayList<XiangMuEntity> list) {
		this.list = list;
	}
}
