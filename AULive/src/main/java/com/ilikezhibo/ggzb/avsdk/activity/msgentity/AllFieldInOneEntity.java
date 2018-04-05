package com.ilikezhibo.ggzb.avsdk.activity.msgentity;

import android.os.Parcel;
import android.os.Parcelable;

import io.rong.common.ParcelUtils;

/**
 * Created by big on 3/12/16.
 */
public class AllFieldInOneEntity implements Parcelable {
   public String type;
   public String uid;
   public String nickname;
   public String grade;
   public String face;
   public String chat_msg;
   public String welcome_msg;
   public String live_uid;
   public String room_id;
   public String msg;
   public String gift_name;
   //gift_type 1=普通礼物 2=红包 3=豪华礼物
   public int gift_type;
   public int packetid;
   public int gift_id;
   public int gift_nums;

   //给消息赋值。
   public AllFieldInOneEntity(Parcel in) {
      type = ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
      uid = ParcelUtils.readFromParcel(in);
      nickname = ParcelUtils.readFromParcel(in);
      grade = ParcelUtils.readFromParcel(in);
      face = ParcelUtils.readFromParcel(in);
      chat_msg = ParcelUtils.readFromParcel(in);
      welcome_msg = ParcelUtils.readFromParcel(in);
      live_uid = ParcelUtils.readFromParcel(in);
      room_id = ParcelUtils.readFromParcel(in);
      msg = ParcelUtils.readFromParcel(in);
      gift_name = ParcelUtils.readFromParcel(in);

      gift_type = ParcelUtils.readIntFromParcel(in);
      packetid = ParcelUtils.readIntFromParcel(in);
      gift_id = ParcelUtils.readIntFromParcel(in);
      gift_nums = ParcelUtils.readIntFromParcel(in);
   }

   /**
    * 将类的数据写入外部提供的 Parcel 中。
    *
    * @param dest 对象被写入的 Parcel。
    * @param flags 对象如何被写入的附加标志。
    */
   @Override public void writeToParcel(Parcel dest, int flags) {
      ParcelUtils.writeToParcel(dest, type);//该类为工具类，对消息中属性进行序列化
      ParcelUtils.writeToParcel(dest, uid);
      ParcelUtils.writeToParcel(dest, nickname);
      ParcelUtils.writeToParcel(dest, grade);
      ParcelUtils.writeToParcel(dest, face);
      ParcelUtils.writeToParcel(dest, chat_msg);
      ParcelUtils.writeToParcel(dest, welcome_msg);
      ParcelUtils.writeToParcel(dest, live_uid);
      ParcelUtils.writeToParcel(dest, room_id);
      ParcelUtils.writeToParcel(dest, msg);
      ParcelUtils.writeToParcel(dest, gift_name);

      ParcelUtils.writeToParcel(dest, gift_type);
      ParcelUtils.writeToParcel(dest, packetid);
      ParcelUtils.writeToParcel(dest, gift_id);
      ParcelUtils.writeToParcel(dest, gift_nums);
      //这里可继续增加你消息的属性
   }

   /**
    * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
    */
   public static final Creator<AllFieldInOneEntity> CREATOR = new Creator<AllFieldInOneEntity>() {

      @Override public AllFieldInOneEntity createFromParcel(Parcel source) {
         return new AllFieldInOneEntity(source);
      }

      @Override public AllFieldInOneEntity[] newArray(int size) {
         return new AllFieldInOneEntity[size];
      }
   };

   /**
    * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
    *
    * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
    */
   public int describeContents() {
      return 0;
   }
}