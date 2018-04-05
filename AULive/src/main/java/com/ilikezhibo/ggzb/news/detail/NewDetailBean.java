package com.ilikezhibo.ggzb.news.detail;

import java.io.Serializable;

/**
 * @ClassName: ExperienceDetailBean
 * @Description: 经验内容
 * @author big
 * @date 2014-07-1 下午4:05:19
 * 
 */
public class NewDetailBean implements Serializable {

	private String id;
	private String uid;
	private String intro;
	private String title;
	private String pics;
	private String memo;
	private String praise;
	private String comment_total;
	private String status;
	private String add_time;
	private String face;

	private String url;
	private String price;
	private String time;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getComment_total() {
		return comment_total;
	}

	public void setComment_total(String comment_total) {
		this.comment_total = comment_total;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
