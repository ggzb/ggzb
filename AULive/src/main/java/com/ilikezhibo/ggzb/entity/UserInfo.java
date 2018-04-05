package com.ilikezhibo.ggzb.entity;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * @author big
 * @ClassName: UserDetailEntity
 * @Description: 用户详情
 * @date 2014-3-27 下午4:05:19
 */
public class UserInfo extends BaseEntity {

   private LoginUserEntity userinfo;
   private int step;
   private int is_push;
   private String alipay;
   private String show_pay;
   private String is_report;
   private String token;

   private String sdkAppId;
   private String userSig;
   private String accountType;
   private String idenitifer;
   private String uplive_url;


   private String msg_tip;
   private String wx_bindid;



   public UpdateApkEntity upnew;
   public UpdateApkEntity fixpatch;
   public String filterurl;

   public int getIs_atten() {
      return is_atten;
   }

   public void setIs_atten(int is_atten) {
      this.is_atten = is_atten;
   }

   private int is_atten;

   public String getSdkAppId() {
      return sdkAppId;
   }

   public void setSdkAppId(String sdkAppId) {
      this.sdkAppId = sdkAppId;
   }

   public String getUserSig() {
      return userSig;
   }

   public void setUserSig(String userSig) {
      this.userSig = userSig;
   }

   public String getAccountType() {
      return accountType;
   }

   public void setAccountType(String accountType) {
      this.accountType = accountType;
   }

   public String getIdenitifer() {
      return idenitifer;
   }

   public void setIdenitifer(String idenitifer) {
      this.idenitifer = idenitifer;
   }

   public LoginUserEntity getUserinfo() {
      return userinfo;
   }

   public void setUserinfo(LoginUserEntity userinfo) {
      this.userinfo = userinfo;
   }

   public int getStep() {
      return step;
   }

   public void setStep(int step) {
      this.step = step;
   }

   public int getIs_push() {
      return is_push;
   }

   public void setIs_push(int is_push) {
      this.is_push = is_push;
   }

   public String getAlipay() {
      return alipay;
   }

   public void setAlipay(String alipay) {
      this.alipay = alipay;
   }

   public String getShow_pay() {
      return show_pay;
   }

   public void setShow_pay(String show_pay) {
      this.show_pay = show_pay;
   }

   public String getIs_report() {
      return is_report;
   }

   public void setIs_report(String is_report) {
      this.is_report = is_report;
   }

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public String getUplive_url() {
      return uplive_url;
   }

   public void setUplive_url(String uplive_url) {
      this.uplive_url = uplive_url;
   }


   public String getWx_bindid() {
      return wx_bindid;
   }

   public void setWx_bindid(String wx_bindid) {
      this.wx_bindid = wx_bindid;
   }




//   public void setMsg_tip(String msg_tip) {
//      this.msg_tip = msg_tip;
//   }
//   public String getMsg_tip() {
//      return msg_tip;
//   }
}
