package com.ilikezhibo.ggzb.news.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * @ClassName: ProductListEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-4-1 下午4:05:19
 * 
 */
public class NewListEntity extends BaseEntity implements Serializable {
	private ArrayList<NewEntity> list;

	public ArrayList<NewEntity> getList() {
		return list;
	}

	public void setList(ArrayList<NewEntity> list) {
		this.list = list;
	}
	


}
