package com.ilikezhibo.ggzb.avsdk.activity.msgentity;

import com.ilikezhibo.ggzb.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by big on 3/12/16.
 */
   public class UserBarrageEntity extends BaseEntity implements Serializable {
   public String type;
   public String uid;
   public String nickname;
   public String grade;
   public String face;
   public String chat_msg;
   public int offical;
   //public ArrayList<String> anchor_medal;
   public ArrayList<String> wanjia_medal;


}