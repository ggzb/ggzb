package com.ilikezhibo.ggzb.xiangmu.entity;

import java.io.Serializable;

/**
 * 
 * @ClassName: CommentEntity
 * @Description: 评论
 * @author big
 * @date 2014-7-1 上午11:13:18
 * 
 */
public class CommentEntity implements Serializable {
	private String id;
	private String pid;
	private String uid;
	private String cont;
	private String type;
	private String add_time;
	private String nickname;
	private String face;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

}
