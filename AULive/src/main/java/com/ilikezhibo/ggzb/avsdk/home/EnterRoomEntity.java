package com.ilikezhibo.ggzb.avsdk.home;

import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.avsdk.activity.ActEntity;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: GiftEntity
 * @Description: 主页内容
 * @date 2016-3-1 下午4:05:19
 */
public class EnterRoomEntity extends BaseEntity implements Serializable {
   public int is_manager;
   public int show_manager;
   public int is_gag;
   public int recv_diamond;
   public int is_live;
   public int total;
   public ArrayList<String> sys_msg;
   public String url;
   public int sendmsg_grade;
   public String memo;

   public ArrayList<String> anchor_medal;
   public ArrayList<String> wanjia_medal;
   public ActEntity act;

   @Override
   public String toString() {
      return "EnterRoomEntity{" +
              "is_manager=" + is_manager +
              ", show_manager=" + show_manager +
              ", is_gag=" + is_gag +
              ", recv_diamond=" + recv_diamond +
              ", is_live=" + is_live +
              ", total=" + total +
              ", sys_msg=" + sys_msg +
              ", url='" + url + '\'' +
              ", sendmsg_grade=" + sendmsg_grade +
              ", memo=" + memo +
              ", anchor_medal=" + anchor_medal +
              ", wanjia_medal=" + wanjia_medal +
              ", act=" + act +
              '}';
   }
}
