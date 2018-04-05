package com.ilikezhibo.ggzb.avsdk.activity.msgentity;

import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.avsdk.gift.customized.PointEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by big on 3/12/16.
 */
public class SendGiftEntity extends BaseEntity implements Serializable {
   public String type;
   public String uid;
   public String nickname;
   public String grade;
   public String face;
   public String gift_name;
   public int gift_id;
   public int gift_nums;
   public int gift_type;
   public int offical;
   //public ArrayList<String> anchor_medal;
   public ArrayList<String> wanjia_medal;
   //红包id
   public int packetid;

   public int price;
   //主播的金币数
   public int recv_diamond;

   //个性礼物个字段
   public PointEntity paint;

}