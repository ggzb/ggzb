package com.ilikezhibo.ggzb.userinfo.mymoney;

import java.io.Serializable;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * 
 * @ClassName: CommentEntity
 * @Description: 评论
 * @author big
 * @date 2014-7-1 上午11:13:18
 * 
 */
public class MyMoneyEntity extends BaseEntity implements Serializable {
	private String money;
	private String pid;
	private String credit;

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

}
