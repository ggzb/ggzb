package com.ilikezhibo.ggzb.news.detail;

import java.io.Serializable;
import java.util.ArrayList;

import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.CommentEntity;

/**
 * @ClassName: FindDetailEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-4-1 下午4:05:19
 * 
 */
public class NewDetailEntity extends BaseEntity implements Serializable {

	private NewDetailBean detail;

	private ArrayList<CommentEntity> comment;

	public NewDetailBean getDetail() {
		return detail;
	}

	public void setDetail(NewDetailBean detail) {
		this.detail = detail;
	}

	public ArrayList<CommentEntity> getComment() {
		return comment;
	}

	public void setComment(ArrayList<CommentEntity> comment) {
		this.comment = comment;
	}

}
