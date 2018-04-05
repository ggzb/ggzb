package com.ilikezhibo.ggzb.avsdk.userinfo.toprank;

/**
 * @ClassName: TopRankEntity
 * @Description: 关注详情
 * @author big
 * @date 2014-3-27 下午4:05:19
 * 
 */
public class TopRankEntity {

//{
   //   "stat": 200,
   //    "msg": "",
   //    "list": [
   //   {
   //      "uid": 16708479,
   //       "nickname": "Jack ",
   //       "face": "http://img.hrbhzkj.com/face/127/16708479.jpg",
   //       "sex": "1",
   //       "grade": 8,
   //       "signature": "",
   //       "tag": ""
   //"send_diamond": "138900",
   //"recv_diamond": "5912",
   //   }
   //   ]
   //}

   private String uid;
   private String nickname;
   private String face;
   private int sex;
   private String grade;
   private String signature;
   private String tag;
   private int send_diamond;

   private int consume_diamond;

   public int getConsume_diamond() {
      return consume_diamond;
   }

   public void setConsume_diamond(int consume_diamond) {
      this.consume_diamond = consume_diamond;
   }


   public boolean is_ranking() {
      return is_ranking;
   }

   public void setIs_ranking(boolean is_ranking) {
      this.is_ranking = is_ranking;
   }

   private boolean is_ranking=false;
   public int getRecv_diamond() {
      return recv_diamond;
   }

   public void setRecv_diamond(int recv_diamond) {
      this.recv_diamond = recv_diamond;
   }

   public int getSend_diamond() {
      return send_diamond;
   }

   public void setSend_diamond(int send_diamond) {
      this.send_diamond = send_diamond;
   }

   private int  recv_diamond;

   public String getUid() {
      return uid;
   }

   public void setUid(String uid) {
      this.uid = uid;
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

   public int getSex() {
      return sex;
   }

   public void setSex(int sex) {
      this.sex = sex;
   }

   public String getGrade() {
      return grade;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }

   public String getSignature() {
      return signature;
   }

   public void setSignature(String signature) {
      this.signature = signature;
   }

   public String getTag() {
      return tag;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }
	
}
