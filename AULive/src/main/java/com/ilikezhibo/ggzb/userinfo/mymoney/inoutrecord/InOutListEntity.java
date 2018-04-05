package com.ilikezhibo.ggzb.userinfo.mymoney.inoutrecord;

import java.io.Serializable;
import java.util.ArrayList;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * @ClassName: ProductListEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-4-1 下午4:05:19
 * 
 */
public class InOutListEntity extends BaseEntity implements Serializable {
	private ArrayList<InOutEntity> list;

	public ArrayList<InOutEntity> getList() {
		return list;
	}

	public void setList(ArrayList<InOutEntity> list) {
		this.list = list;
	}
	


}
