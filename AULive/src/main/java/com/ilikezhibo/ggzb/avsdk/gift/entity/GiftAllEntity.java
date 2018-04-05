/**
 * @Title: GiftAllEntity.java
 * @Package com.qixi.caoliu.home.video.entity
 * @Description: TODO
 * @author jack.long
 * @date 2013-10-12 下午9:22:30
 * @version
 */
package com.ilikezhibo.ggzb.avsdk.gift.entity;

import com.google.myjson.stream.JsonReader;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.TextUtil;
import com.ilikezhibo.ggzb.avsdk.gift.GiftPagerUtil;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @ClassName: GiftAllEntity
 * @Description: 所有礼物的集合
 * @author jack.long
 * @date 2013-10-12 下午9:22:30
 *
 */
public class GiftAllEntity {
   private HashMap<String, GiftEntity> gifts = new HashMap<String, GiftEntity>();
   private ArrayList<GiftEntity> allGifts = new ArrayList<GiftEntity>();

   private String version;
   private static GiftAllEntity instance;
   private boolean isLoaded;

   private GiftAllEntity() {
      initializeGifts(SharedPreferenceTool.getInstance()
          .getString(GiftPagerUtil.GIFT_MALL_LOCALE_DATA_KEY, ""));
   }

   public static GiftAllEntity getInstance() {
      if (instance == null) {
         instance = new GiftAllEntity();
      }

      return instance;
   }

   public synchronized void initializeGifts(String json) {
      // Trace.d(json);
      if (TextUtil.isValidate(json)) {
         JsonReader reader = new JsonReader(new StringReader(json));
         try {
            readFromJson(reader, json);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   private boolean readFromJson(JsonReader reader, String tag) throws IOException {
      reader.beginObject();
      String name = null;
      GiftEntity entity = null;
      while (reader.hasNext()) {
         name = reader.nextName();
         if (name.equalsIgnoreCase("version")) {
            version = reader.nextString();
         } else if ("gifts".equals(name) && (!isLoaded || !version.equals(
             SharedPreferenceTool.getInstance().getString(GiftPagerUtil.GIF_MALL_VERSION_KEY, "")))) {
            allGifts.clear();
            gifts.clear();
            reader.beginObject();
            while (reader.hasNext()) {
               name = reader.nextName();
               entity = new GiftEntity();
               entity.readFromJson(reader, name);
               entity.setIndex(name);
               allGifts.add(entity);
               gifts.put(name, entity);
            }
            reader.endObject();
            isLoaded = true;
            SharedPreferenceTool.getInstance().saveString(GiftPagerUtil.GIFT_MALL_LOCALE_DATA_KEY, tag);
            SharedPreferenceTool.getInstance().saveString(GiftPagerUtil.GIF_MALL_VERSION_KEY, version);
         } else {
            reader.skipValue();
         }
      }
      reader.endObject();
      // 填充集合
      return true;
   }

   public int size() {
      return gifts.size();
   }

   public HashMap<String, GiftEntity> getGifts() {
      return gifts;
   }

   public void setGifts(HashMap<String, GiftEntity> gifts) {
      this.gifts = gifts;
   }

   public ArrayList<GiftEntity> getAllGifts() {
      Collections.sort(allGifts);
      return allGifts;
   }

   public void setAllGifts(ArrayList<GiftEntity> allGifts) {
      this.allGifts = allGifts;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public GiftEntity getGiftEntity(String key) {
      return gifts.get(key);
   }

}
