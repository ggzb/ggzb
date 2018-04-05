package com.ilikezhibo.ggzb.entity;

import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: LoginUserEntity
 * @Description: 登录用户信息
 * @date 2014-7-1 上午11:13:18
 */
public class LoginUserEntity {
   private String uid;// 用户id
   private String account;
   private int sex;
   private String nickname;
   private String face;

   private String grade;

   private String device_token;// 认证
   private String sip;
   private String sport;
   private String type;// 登陆用到的类型

   private long time;
   private String token;

   private String im_token;
   private int percent;
   private String birthday;

   private int age;

   private int onetone; // >是否1v1第一次登录

   // 推流的地址
   private String uplive_url;




   private String msg_tip;
   private String private_chat_status;
   private String wx_bindid;
   private String exchange_diamond;

   //靓号与座驾
   public int goodid;
   public int zuojia;


   //礼物相关
//   "recv_diamond":0,"send_diamond":0,"family":null,"diamond":10000,
// "fans_total":0,"atten_total":0,"live_total":0,
   public int recv_diamond;
   public int send_diamond;
   public String family;
   public int diamond;
   public int fans_total;
   public int atten_total;
   public int live_total;
   public int guard;
   public String city;
   public String signature;
   public String tag;
   public int offical;
   public ArrayList<String> anchor_medal;
   public ArrayList<String> wanjia_medal;
   //"tops": {
   //   "face": "face/4/18601220.jpg",
   //       "sex": "1",
   //       "uid": "18601220"
   //}

   public ArrayList<LoginUserEntity> tops;

   //直播专用
   public String sdkAppId;
   public String userSig;
   public String accountType;
   public String idenitifer;
   public int show_manager;
   public int is_live;
   public int payliving;
   //-1 不是好友 0-申请 2-互相为好友
   private int friend_status;
   // 简历相关
   // "uid": "16819009",
   // "account": "15078285865",
   // "nickname": "big",
   // "face": "http://img.qxj.me/face/65/16819009.jpg",
   // "sex": "1",

   // "lng": "121.3997",
   // "lat": "31.0456",
   // "geohash": "wtw2de8td",
   // "birthday": "20150716",
   // "job": "后端开发,移动开发",
   // "skill": "java,android,c  ,perl",
   // "intro": "啦咯来咯弄啦咯啦咯来咯空我摸摸陌陌摸摸摸摸我我摸摸噢噢噢哦哦",
   // "company": "腾讯",
   // "post": "经理",
   // "work_time": "0",
   // "begin_work_time": "1436976000",
   // "end_work_time": "1436976000",
   // "job_desc": "哈哈哈",
   // "school_time": "0",
   // "begin_school_time": "1436976000",
   // "end_school_time": "1436976000",
   // "degree": "博士",
   // "specialty": "计算机",
   // "school": "清华北大",
   // "type": "login",
   // "credit": "0",
   // "score": "0",
   // "city": "上海市",
   // "sip": "121.199.1.26",
   // "sport": 8008

   public String lng;
   public String lat;
   public String geohash;
   public String job;
   public String skill;
   public String intro;
   public String company;
   public String post;
   public String work_time;
   public String begin_work_time;
   public String end_work_time;
   public String job_desc;
   public String school_time;
   public String begin_school_time;
   public String end_school_time;
   public String degree;
   public String specialty;
   public String school;
   public String credit;
   public String score;

   // 绑定
   // "ali_account": "123456@163.com",
   // "ali_realname": "黄大明",
   // "ali_verify": "0",
   // "wx_account": "",
   // "wx_realname": ""

   public String ali_account;
   public String ali_realname;
   public String wx_account;
   public String wx_realname;

   public String getGrade() {
      return grade;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }

   private ArrayList<XiangMuEntity> list;

   public ArrayList<XiangMuEntity> getList() {
      return list;
   }

   public void setList(ArrayList<XiangMuEntity> list) {
      this.list = list;
   }

   public String getUid() {
      if (uid == null) {
         return "";
      }
      return uid;
   }

   public void setUid(String uid) {
      this.uid = uid;
   }

   public String getAccount() {
      return account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public int getSex() {
      return sex;
   }

   public void setSex(int sex) {
      this.sex = sex;
   }

   public String getNickname() {
      return nickname;
   }

   public void setNickname(String nickname) {
      this.nickname = nickname;
   }

   public String getFace() {
      return face;
   }

   public void setFace(String face) {
      this.face = face;
   }

   public String getDevice_token() {
      return device_token;
   }

   public void setDevice_token(String device_token) {
      this.device_token = device_token;
   }

   public String getSip() {
      return sip;
   }

   public void setSip(String sip) {
      this.sip = sip;
   }

   public String getSport() {
      return sport;
   }

   public void setSport(String sport) {
      this.sport = sport;
   }

   public long getTime() {
      return time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public int getPercent() {
      return percent;
   }

   public void setPercent(int percent) {
      this.percent = percent;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getBirthday() {
      return birthday;
   }

   public void setBirthday(String birthday) {
      this.birthday = birthday;
   }

   public int getAge() {
      return age;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public int getFriend_status() {
      return friend_status;
   }

   public void setFriend_status(int friend_status) {
      this.friend_status = friend_status;
   }

   public String getIm_token() {
      return im_token;
   }

   public void setIm_token(String im_token) {
      this.im_token = im_token;
   }

   public String getUpLiveUrl() {
      return uplive_url;
   }

   public void setUpLiveUrl(String upLiveUrl) {
      this.uplive_url = upLiveUrl;
   }

   public int getOnetone() {
      return onetone;
   }

   public void setOnetone(int onetone) {
      this.onetone = onetone;
   }



   public void setMsg_tip(String msg_tip) {
      this.msg_tip = msg_tip;
   }
   public String getMsg_tip() {
      return msg_tip;
   }
   public void setPrivate_chat_status(String private_chat_status) {
      this.private_chat_status = private_chat_status;
   }
   public String getPrivate_chat_status() {
      return private_chat_status;
   }


   public String getWx_bindid() {
      return wx_bindid;
   }
   public void setWx_bindid(String wx_bindid) {
      this.wx_bindid = wx_bindid;
   }

   public String getExchange_diamond() {
      return exchange_diamond;
   }
   public void setExchange_diamond(String exchange_diamond) {
      this.exchange_diamond = exchange_diamond;
   }


}
