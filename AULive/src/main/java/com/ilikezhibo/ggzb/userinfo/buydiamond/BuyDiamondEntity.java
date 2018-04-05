package com.ilikezhibo.ggzb.userinfo.buydiamond;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;

/**
 * @ClassName: NewEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-4-1 下午4:05:19
 * 
 */
public class BuyDiamondEntity extends BaseEntity implements Serializable {

   //{
   //   "diamond": 42,
   //    "money": 6,
   //    "memo": ""
   //}

	private int diamond;
	private int money;
	private String memo;

   public int getDiamond() {
      return diamond;
   }

   public void setDiamond(int diamond) {
      this.diamond = diamond;
   }

   public int getMoney() {
      return money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public String getMemo() {
      return memo;
   }

   public void setMemo(String memo) {
      this.memo = memo;
   }
}
