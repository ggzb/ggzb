package com.ilikezhibo.ggzb.userinfo.myfriends;

import java.util.ArrayList;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * @ClassName: TopRankEntity
 * @Description: 关注详情
 * @author big
 * @date 2014-3-27 下午4:05:19
 * 
 */
public class FriendListEntity extends BaseEntity {

	private ArrayList<FriendEntity> list;

	public ArrayList<FriendEntity> getList() {
		return list;
	}

	public void setList(ArrayList<FriendEntity> list) {
		this.list = list;
	}
 
 

}
