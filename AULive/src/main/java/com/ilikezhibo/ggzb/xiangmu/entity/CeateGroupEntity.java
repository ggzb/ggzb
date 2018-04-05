package com.ilikezhibo.ggzb.xiangmu.entity;

import java.io.Serializable;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * 
 * @ClassName: CeateGroupEntity
 * @Description: 评论
 * @author big
 * @date 2014-7-1 上午11:13:18
 * 
 */
public class CeateGroupEntity extends BaseEntity implements Serializable {

	private String name;
	private String face;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

}
