package com.ilikezhibo.ggzb.avsdk.activity.msgentity;

import com.ilikezhibo.ggzb.BaseEntity;

import java.io.Serializable;

/**
 * Created by big on 3/12/16.
 */
   public class StartLiveEntity extends BaseEntity implements Serializable {
   public String type;
   public String live_uid;
   public String room_id;

}