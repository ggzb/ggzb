package com.ilikezhibo.ggzb.avsdk.activity.entity;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;

/**
 * @author big
 * @ClassName: GiftListEntity
 * @Description: 主页内容
 * @date 2014-4-1 下午4:05:19
 */
public class UidListEntity extends BaseEntity implements Serializable {
   public String getUids() {
      return uids;
   }

   public void setUids(String uids) {
      this.uids = uids;
   }

   String uids;
}
