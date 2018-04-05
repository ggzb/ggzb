package com.ilikezhibo.ggzb.step;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @ClassName: FenLeiListEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-4-1 下午4:05:19
 * 
 */
public class FenLeiListEntity extends BaseEntity implements Serializable {

	private ArrayList<AdvEntity> list;
	private String news;
	public ArrayList<AdvEntity> getList() {
		return list;
	}

	public void setList(ArrayList<AdvEntity> list) {
		this.list = list;
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}

}
