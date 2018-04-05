package com.ilikezhibo.ggzb.avsdk.userinfo.toprank;

import com.ilikezhibo.ggzb.BaseEntity;
import java.util.ArrayList;

/**
 * @ClassName: TopRankEntity
 * @Description: 关注详情
 * @author big
 * @date 2014-3-27 下午4:05:19
 * 
 */
public class TopRankListEntity extends BaseEntity {

	private ArrayList<TopRankEntity> list;

	public ArrayList<TopRankEntity> getList() {
		return list;
	}

	public void setList(ArrayList<TopRankEntity> list) {
		this.list = list;
	}
 
   public int total;

}
