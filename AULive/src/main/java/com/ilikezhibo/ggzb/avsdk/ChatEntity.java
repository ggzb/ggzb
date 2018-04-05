package com.ilikezhibo.ggzb.avsdk;

import com.ilikezhibo.ggzb.avsdk.activity.msgentity.ChatMsgEntity;

public class ChatEntity {

   private ChatMsgEntity chatMsgEntity;
   private String grpSendName;
   private long time;
   private boolean bSelf;

   public ChatEntity() {

   }

   public ChatMsgEntity getChatMsgEntity() {
      return chatMsgEntity;
   }

   public void setChatMsgEntity(ChatMsgEntity chatMsgEntity) {
      this.chatMsgEntity = chatMsgEntity;
   }

   public String getGrpSendName() {
      return grpSendName;
   }

   public void setGrpSendName(String grpSendName) {
      this.grpSendName = grpSendName;
   }

   public long getTime() {
      return time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public boolean isbSelf() {
      return bSelf;
   }

   public void setbSelf(boolean bSelf) {
      this.bSelf = bSelf;
   }
}
