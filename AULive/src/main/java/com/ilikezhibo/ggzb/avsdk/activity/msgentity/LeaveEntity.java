package com.ilikezhibo.ggzb.avsdk.activity.msgentity;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;

/**
 * Created by big on 3/12/16.
 */
public class LeaveEntity extends BaseEntity implements Serializable {
   public String type;
   public String uid;
   public int audience_count;
   public int praise_count;
   public int price;
}