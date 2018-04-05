package com.ilikezhibo.ggzb.avsdk;

public class UserInfo {
   //uid
   private String mPhone = "null";
   //nickname
   private String mName = "null";
   //face
   private String mHeadImagePath = "null";
   //msg_tip
   private String mMsg_tip;
   private String mPrivate_chat_status;

   private Boolean mIsCreater = false; //ture-直播创建者，false-直播观众
   private String mUserSig = "";

   public UserInfo(String phone, String name) {
      this.mPhone = phone;
      this.mName = name;
   }

//   public UserInfo(String phone, String name, String headImage) {
//      this.mPhone = phone;
//      this.mName = name;
//      mHeadImagePath = headImage;
//   }


   public UserInfo(String phone, String name, String headImage, String msg_tip, String private_chat_status) {
      this.mPhone = phone;
      this.mName = name;
      mHeadImagePath = headImage;
      this.mMsg_tip = msg_tip;
      this.mPrivate_chat_status = private_chat_status;
   }

   public void setHeadImagePath(String headImagePath) {
      this.mHeadImagePath = headImagePath;
   }

   public String getHeadImagePath() {
      return mHeadImagePath;
   }

   public void setUsersig(String mUsersig) {

      this.mUserSig = mUsersig;
   }

   public void setUserPhone(String phone) {

      this.mPhone = phone;
   }

   public void setUserName(String name) {
      this.mName = name;
   }

   public String getUsersig() {
      return mUserSig;
   }

   public String getUserName() {
      return mName;
   }

   public String getUserPhone() {
      return mPhone;
   }

   public Boolean isCreater() {
      return mIsCreater;
   }

   public void setIsCreater(Boolean state) {
      mIsCreater = state;
   }




   public void setmMsg_tip(String msg_tip) {
      this.mMsg_tip = msg_tip;
   }
   public String getmMsg_tip() {
      return mMsg_tip;
   }
   public void setmPrivate_chat_status(String private_chat_status) {
      this.mPrivate_chat_status = private_chat_status;
   }
   public String getmPrivate_chat_status() {
      return mPrivate_chat_status;
   }



}
