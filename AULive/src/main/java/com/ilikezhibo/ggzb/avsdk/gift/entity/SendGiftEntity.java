/**
 * @Title: GiftEntity.java
 * @Package com.qixi.caoliu.home.video.entity
 * @Description: TODO
 * @author jack.long
 * @date 2013-10-12 下午9:23:29
 * @version
 */
package com.ilikezhibo.ggzb.avsdk.gift.entity;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * @author jack.long
 * @ClassName: GiftEntity
 * @Description: 礼物对象
 * @date 2013-10-12 下午9:23:29
 */
public class SendGiftEntity extends BaseEntity {

   //{"stat":200,"msg":"","diamond":9999,"send_diamond":1}
   //还有多少钻石
   private int diamond;
   //已经送出多少钻石
   private int send_diamond;

   //当是红包时有packetid
   private int packetid = 0;

   //抢到多少红包
   private int grab = 0;

   //主播所有金币数
   private int recv_diamond;

   public int gift_type;
   public int gift_id;

   public int getGrade() {
      return grade;
   }

   public void setGrade(int grade) {
      this.grade = grade;
   }

   private int grade;

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

   public int getDiamond() {
      return diamond;
   }

   public void setDiamond(int diamond) {
      this.diamond = diamond;
   }

   public int getPacketid() {
      return packetid;
   }

   public void setPacketid(int packetid) {
      this.packetid = packetid;
   }

   public int getGrab() {
      return grab;
   }

   public void setGrab(int grab) {
      this.grab = grab;
   }
}
