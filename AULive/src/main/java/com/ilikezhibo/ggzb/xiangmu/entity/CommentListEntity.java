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
public class CommentListEntity extends BaseEntity implements Serializable {
	private ArrayList<CommentEntity> list;

	public ArrayList<CommentEntity> getList() {
		return list;
	}

	public void setList(ArrayList<CommentEntity> list) {
		this.list = list;
	}
}
