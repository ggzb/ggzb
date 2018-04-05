package com.ilikezhibo.ggzb.avsdk.activity.msgentity;

import com.ilikezhibo.ggzb.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by big on 3/12/16.
 */
public class LikeUserEntity extends BaseEntity implements Serializable {
   public String type;
   public String uid;
   public String nickname;
   public String face;
   public String grade;
   public String love_pos;
   public int offical;
   //public ArrayList<String> anchor_medal;
   public ArrayList<String> wanjia_medal;
}