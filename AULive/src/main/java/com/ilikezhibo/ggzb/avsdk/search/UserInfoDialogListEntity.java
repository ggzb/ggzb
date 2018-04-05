package com.ilikezhibo.ggzb.avsdk.search;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: GiftListEntity
 * @Description: 主页内容
 * @date 2014-4-1 下午4:05:19
 */
public class UserInfoDialogListEntity extends BaseEntity implements Serializable {
   private ArrayList<UserInfoDialogEntity> list;

   public ArrayList<UserInfoDialogEntity> getList() {
      return list;
   }

   public void setList(ArrayList<UserInfoDialogEntity> list) {
      this.list = list;
   }
}
