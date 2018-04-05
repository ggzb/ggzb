package com.ilikezhibo.ggzb.avsdk.activity.msgentity;

import com.ilikezhibo.ggzb.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by big on 3/12/16.
 */
public class ChatMsgEntity extends BaseEntity implements Serializable {
   public String type;
   public String uid;
   public String nickname;
   public String grade;
   public String face;
   public String chat_msg;
   //gift_type 1=普通礼物 2=红包 3=豪华礼物
   public int gift_type;
   public int packetid;

   public String content;

   //超管关闭主播房间
   public String system_content;

   public int offical;
   //public ArrayList<String> anchor_medal;
   public ArrayList<String> wanjia_medal;
}