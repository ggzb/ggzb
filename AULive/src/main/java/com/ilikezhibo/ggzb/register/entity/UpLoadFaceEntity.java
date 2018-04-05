package com.ilikezhibo.ggzb.register.entity;

import com.ilikezhibo.ggzb.BaseEntity;
import java.util.ArrayList;

/**
 * @ClassName: ProfileUserInfo
 * @Description: 个人详情
 * @author big
 * @date 2014-3-27 下午4:05:19
 * 
 */
public class UpLoadFaceEntity extends BaseEntity {

	private String id;
	private String face;
	private String url;
   public ArrayList<String> sys_msg;

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
