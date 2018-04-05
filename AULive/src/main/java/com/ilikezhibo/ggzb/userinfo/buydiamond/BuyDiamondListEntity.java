package com.ilikezhibo.ggzb.userinfo.buydiamond;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @ClassName: BuyDiamondListEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-4-1 下午4:05:19
 * 
 */
public class BuyDiamondListEntity extends BaseEntity implements Serializable {
      int diamond;

	private ArrayList<BuyDiamondEntity> coins;

	public ArrayList<BuyDiamondEntity> getList() {
		return coins;
	}

	public void setList(ArrayList<BuyDiamondEntity> list) {
		this.coins = list;
	}
	


}
