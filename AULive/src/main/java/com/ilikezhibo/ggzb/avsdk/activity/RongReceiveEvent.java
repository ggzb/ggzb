package com.ilikezhibo.ggzb.avsdk.activity;

import io.rong.imlib.model.Message;

/**
 * Created by big on 10/24/16.
 */

public class RongReceiveEvent {

   public RongReceiveEvent(Message message, int left) {
      this.message = message;
      this.left = left;
   }

   public Message message;
   public int left;
}
