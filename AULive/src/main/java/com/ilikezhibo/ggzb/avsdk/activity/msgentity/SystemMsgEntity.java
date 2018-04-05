package com.ilikezhibo.ggzb.avsdk.activity.msgentity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by big on 3/12/16.
 */
public class SystemMsgEntity implements Serializable {
   public String type;
   public String msg;
   public int alert;
   public String system_content;
   public String sender;
   public String target;
   public List<String> uids;
   public int recv_diamond;

   public SystemMsgEntity() {
   }
}
