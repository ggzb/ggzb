package com.ilikezhibo.ggzb.avsdk.activity.custommsg;

import android.os.Parcel;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by big on 3/12/16.
 */
@MessageTag(value = "app:custom-queue", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class CustomizeMsgQueueMessage extends MessageContent {
   public String type;
   public String data;

   public CustomizeMsgQueueMessage() {
   }

   @Override public byte[] encode() {
      JSONObject jsonObj = new JSONObject();

      try {
         jsonObj.put("type", type);

         JSONObject dataObject = new JSONObject(data);
         JSONArray list = dataObject.optJSONArray("list");
         JSONArray newList = new JSONArray();
         for (int i = 0; i < list.length(); i++) {
            JSONObject obj = list.getJSONObject(i);
            obj.putOpt("data", new JSONObject(obj.opt("data").toString()));
            newList.put(obj);
         }
         dataObject.putOpt("list", newList);
         jsonObj.putOpt("data", dataObject);

         //jsonObj.put("data", data);
      } catch (JSONException e) {
         e.printStackTrace();
      }

      try {
         return jsonObj.toString().getBytes("UTF-8");
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }

      return null;
   }

   public CustomizeMsgQueueMessage(byte[] data1) {
      String jsonStr = null;

      try {
         jsonStr = new String(data1, "UTF-8");
      } catch (UnsupportedEncodingException e1) {

      }

      try {
         JSONObject jsonObj = new JSONObject(jsonStr);

         if (jsonObj.has("data")) {
            type = jsonObj.optString("type");
            JSONObject dataObj = jsonObj.optJSONObject("data");
            data = dataObj.toString();
            //JSONArray list = data.optJSONArray("list");
            //prin
            //data = jsonObj.optString("data");
         }
      } catch (JSONException e) {

      }
   }

   //给消息赋值。
   public CustomizeMsgQueueMessage(Parcel in) {
      type = ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
      data = ParcelUtils.readFromParcel(in);
      //这里可继续增加你消息的属性
   }

   /**
    * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
    */
   public static final Creator<CustomizeMsgQueueMessage> CREATOR =
       new Creator<CustomizeMsgQueueMessage>() {

          @Override public CustomizeMsgQueueMessage createFromParcel(Parcel source) {
             return new CustomizeMsgQueueMessage(source);
          }

          @Override public CustomizeMsgQueueMessage[] newArray(int size) {
             return new CustomizeMsgQueueMessage[size];
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

   /**
    * 将类的数据写入外部提供的 Parcel 中。
    *
    * @param dest 对象被写入的 Parcel。
    * @param flags 对象如何被写入的附加标志。
    */
   @Override public void writeToParcel(Parcel dest, int flags) {
      ParcelUtils.writeToParcel(dest, type);//该类为工具类，对消息中属性进行序列化
      ParcelUtils.writeToParcel(dest, data);
      //这里可继续增加你消息的属性
   }
}