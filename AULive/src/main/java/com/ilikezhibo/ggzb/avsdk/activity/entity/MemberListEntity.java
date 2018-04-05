package com.ilikezhibo.ggzb.avsdk.activity.entity;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: GiftListEntity
 * @Description: 主页内容
 * @date 2014-4-1 下午4:05:19
 */
public class MemberListEntity extends BaseEntity implements Serializable {
   private ArrayList<MemberEntity> list;

   public ArrayList<MemberEntity> getList() {
      return list;
   }

   public void setList(ArrayList<MemberEntity> list) {
      this.list = list;
   }
}
