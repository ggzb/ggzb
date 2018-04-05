package com.ilikezhibo.ggzb.xiangmu.entity;

import java.io.Serializable;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * @ClassName: GroupEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-4-1 下午4:05:19
 * 
 */
public class XiangMuEntity extends BaseEntity implements Serializable {
	// "id": "9",
	// "uid": "12222774",
	// "nickname": "big",
	// "face": "face/54/12222774.jpg",
	// "title": "_vhvvuvuv",
	// "pic": "http://img.qxj.me/photo/54/122227741436878567.jpg",
	// "voice": "",
	// "memo": "",
	// "document": "gghhhhhhhhh",
	// "category": "1",
	// "skills": "",
	// "budget": "55555",
	// "status": "0",
	// "duration": "2",
	// "apply_total": "0",
	// "atten_total": "0",
	// "comm_total": "0",
	// "category_name": "app开发"

	private String id;
	private String uid;
	private String nickname;
	private String face;
	private String title;
	private String pic;
	private String voice;
	private String memo;
	private String document;
	private String category;
	private String skills;
	private String budget;
	private String status;
	private String duration;
	private String apply_total;
	private String atten_total;
	private String comm_total;
	private String category_name;
	private String addr;
	private String job;
	private String add_time;
	
	private String pid;

	// status 默认-1 状态：0:报名；1:拒绝 2:考虑过了 3:同意 4:接受 5:完成
	private String project_status;
	private String project_dev_money;

	// 是否收藏 0-否 1-是
	private String project_follower="0";
	
	// 项目案例字段
	// title
	// pic
	// memo
	// url
	private String url;

	// "dev_money": "2222",
	// "last_time": "1438064936",
	// "exit_time": "0",
	// "ago_status": "0",
	// "u_comment": "",
	// "p_comment": "",
	// "u_score": "0",
	// "p_score": "0"

	private String dev_money;
	private String last_time;
	private String exit_time;
	private String ago_status;

	// 评论相关
	private String u_comment;
	private String p_comment;
	private int u_score;
	private int p_score;

	public String getDev_money() {
		return dev_money;
	}

	public void setDev_money(String dev_money) {
		this.dev_money = dev_money;
	}

	public String getLast_time() {
		return last_time;
	}

	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}

	public String getExit_time() {
		return exit_time;
	}

	public void setExit_time(String exit_time) {
		this.exit_time = exit_time;
	}

	public String getAgo_status() {
		return ago_status;
	}

	public void setAgo_status(String ago_status) {
		this.ago_status = ago_status;
	}

	public String getU_comment() {
		return u_comment;
	}

	public void setU_comment(String u_comment) {
		this.u_comment = u_comment;
	}

	public String getP_comment() {
		return p_comment;
	}

	public void setP_comment(String p_comment) {
		this.p_comment = p_comment;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getApply_total() {
		return apply_total;
	}

	public void setApply_total(String apply_total) {
		this.apply_total = apply_total;
	}

	public String getAtten_total() {
		return atten_total;
	}

	public void setAtten_total(String atten_total) {
		this.atten_total = atten_total;
	}

	public String getComm_total() {
		return comm_total;
	}

	public void setComm_total(String comm_total) {
		this.comm_total = comm_total;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getProject_status() {
		return project_status;
	}

	public void setProject_status(String project_status) {
		this.project_status = project_status;
	}

	public String getProject_dev_money() {
		return project_dev_money;
	}

	public void setProject_dev_money(String project_dev_money) {
		this.project_dev_money = project_dev_money;
	}

	public int getP_score() {
		return p_score;
	}

	public void setP_score(int p_score) {
		this.p_score = p_score;
	}

	public int getU_score() {
		return u_score;
	}

	public void setU_score(int u_score) {
		this.u_score = u_score;
	}

	public String getProject_follower() {
		return project_follower;
	}

	public void setProject_follower(String project_follower) {
		this.project_follower = project_follower;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
