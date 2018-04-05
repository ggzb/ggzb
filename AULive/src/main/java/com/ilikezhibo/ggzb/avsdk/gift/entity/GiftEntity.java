/**
 * @Title: GiftEntity.java
 * @Package com.qixi.caoliu.home.video.entity
 * @Description: TODO
 * @author jack.long
 * @date 2013-10-12 下午9:23:29
 * @version
 */
package com.ilikezhibo.ggzb.avsdk.gift.entity;

import com.google.myjson.stream.JsonReader;
import com.google.myjson.stream.JsonWriter;
import com.ilikezhibo.ggzb.avsdk.gift.customized.PointEntity;
import com.ilikezhibo.ggzb.listener.IJSON;
import java.io.IOException;

/**
 * @author jack.long
 * @ClassName: GiftEntity
 * @Description: 礼物对象
 * @date 2013-10-12 下午9:23:29
 */
public class GiftEntity implements IJSON,Comparable {

   private int id;
   private String name;
   private int gid;
   private int price;
   private int type;
   public PointEntity paint;

   public int getGrade() {
      return grade;
   }

   public void setGrade(int grade) {
      this.grade = grade;
   }

   private int grade;

   private int toanchor;
   private String play;
   private String effect;
   private int award;
   private int awards;
   private int num;
   private int hot;
   private int total;
   private String index;
   private int only;
   private int act;// 活动礼物标记

   //{"stat":200,"msg":"","diamond":9999,"send_diamond":1}
   private int diamond;
   private int send_diamond;


   private int packetid;
   //主播所有的金币数
   public  int recv_diamond;

   public int getPacketid() {
      return packetid;
   }

   public void setPacketid(int packetid) {
      this.packetid = packetid;
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

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getType() {
      return type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getOnly() {
      return only;
   }

   public void setOnly(int only) {
      this.only = only;
   }

   @Override public void writeToJson(JsonWriter writer) {

   }

   @Override public boolean readFromJson(JsonReader reader, String tag) throws IOException {
      reader.beginObject();
      String name = null;
      while (reader.hasNext()) {
         name = reader.nextName();
         if (name.equalsIgnoreCase("id")) {
            this.id = reader.nextInt();
         } else if (name.equalsIgnoreCase("name")) {
            this.name = reader.nextString();
         } else if (name.equalsIgnoreCase("gid")) {
            gid = reader.nextInt();
         } else if (name.equalsIgnoreCase("price")) {
            price = reader.nextInt();
         } else if (name.equalsIgnoreCase("type")) {
            type = reader.nextInt();
         } else if (name.equalsIgnoreCase("toanchor")) {
            toanchor = reader.nextInt();
         } else if (name.equalsIgnoreCase("play")) {
            play = reader.nextString();
         } else if (name.equalsIgnoreCase("effect")) {
            effect = reader.nextString();
         } else if (name.equalsIgnoreCase("award")) {
            award = reader.nextInt();
         } else if (name.equalsIgnoreCase("awards")) {
            awards = reader.nextInt();
         } else if (name.equalsIgnoreCase("num")) {
            num = reader.nextInt();
         } else if (name.equalsIgnoreCase("hot")) {
            hot = reader.nextInt();
         } else if (name.equalsIgnoreCase("total")) {
            total = reader.nextInt();
         } else if (name.equalsIgnoreCase("act")) {
            act = reader.nextInt();
         } else {
            reader.skipValue();
         }
      }
      reader.endObject();
      return true;
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return the gid
    */
   public int getGid() {
      return gid;
   }

   /**
    * @param gid the gid to set
    */
   public void setGid(int gid) {
      this.gid = gid;
   }

   /**
    * @return the price
    */
   public int getPrice() {
      return price;
   }

   /**
    * @param price the price to set
    */
   public void setPrice(int price) {
      this.price = price;
   }

   /**
    * @return the toanchor
    */
   public int getToanchor() {
      return toanchor;
   }

   /**
    * @param toanchor the toanchor to set
    */
   public void setToanchor(int toanchor) {
      this.toanchor = toanchor;
   }

   /**
    * @return the play
    */
   public String getPlay() {
      return play;
   }

   /**
    * @param play the play to set
    */
   public void setPlay(String play) {
      this.play = play;
   }

   /**
    * @return the effect
    */
   public String getEffect() {
      return effect;
   }

   /**
    * @param effect the effect to set
    */
   public void setEffect(String effect) {
      this.effect = effect;
   }

   /**
    * @return the award
    */
   public int getAward() {
      return award;
   }

   /**
    * @param award the award to set
    */
   public void setAward(int award) {
      this.award = award;
   }

   public int getNum() {
      return num;
   }

   public void setNum(int num) {
      this.num = num;
   }

   public int getHot() {
      return hot;
   }

   public void setHot(int hot) {
      this.hot = hot;
   }

   public String getIndex() {
      return index;
   }

   public void setIndex(String index) {
      this.index = index;
   }

   public int getTotal() {
      return total;
   }

   public void setTotal(int total) {
      this.total = total;
   }

   public int getAwards() {
      return awards;
   }

   public void setAwards(int awards) {
      this.awards = awards;
   }

   public int getAct() {
      return act;
   }

   public void setAct(int act) {
      this.act = act;
   }

   @Override public boolean equals(Object o) {
      GiftEntity entity = (GiftEntity) o;
      return id == entity.id && name.equals(entity.name);
   }

   @Override public int hashCode() {
      return super.hashCode();
   }

   @Override
   public int compareTo(Object object) {
      GiftEntity b = (GiftEntity) object;
      return this.num - b.num; // 按书的id比较大小，用于默认排序
   }
}
