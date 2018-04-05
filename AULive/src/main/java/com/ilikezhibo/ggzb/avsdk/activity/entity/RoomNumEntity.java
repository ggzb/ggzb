package com.ilikezhibo.ggzb.avsdk.activity.entity;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;

/**
 * @author big
 * @ClassName: RoomNumEntity
 * @Description: 主页内容
 * @date 2014-4-1 下午4:05:19
 */
public class RoomNumEntity extends BaseEntity implements Serializable {

   public int getRoom_id() {
      return room_id;
   }

   public void setRoom_id(int room_id) {
      this.room_id = room_id;
   }

   private int room_id;

}
