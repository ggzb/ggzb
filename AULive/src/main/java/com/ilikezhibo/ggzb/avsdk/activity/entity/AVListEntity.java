package com.ilikezhibo.ggzb.avsdk.activity.entity;

import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.avsdk.home.entity.AdvEntity;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: GiftListEntity
 * @Description: 主页内容
 * @date 2014-4-1 下午4:05:19
 */
public class AVListEntity extends BaseEntity implements Serializable {
   private ArrayList<AVEntity> list;

   public ArrayList<AVEntity> getList() {
      return list;
   }

   public void setList(ArrayList<AVEntity> list) {
      this.list = list;
   }

   public ArrayList<AdvEntity> getAdv() {
      return adv;
   }

   public void setAdv(ArrayList<AdvEntity> adv) {
      this.adv = adv;
   }

   public ArrayList<AdvEntity> adv;

}
