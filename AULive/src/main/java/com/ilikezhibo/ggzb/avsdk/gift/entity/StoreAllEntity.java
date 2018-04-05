/**
 * @Title: StoreAllEntity.java
 * @Package com.qixi.caoliu.home.video.entity
 * @Description: TODO
 * @author jack.long
 * @date 2013-10-15 上午11:04:20
 * @version
 */
package com.ilikezhibo.ggzb.avsdk.gift.entity;

import com.google.myjson.stream.JsonReader;
import com.ilikezhibo.ggzb.BaseEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName: StoreAllEntity
 * @Description: 仓库数据结构
 * @author jack.long
 * @date 2013-10-15 上午11:04:20
 *
 */
public class StoreAllEntity extends BaseEntity {
   private HashMap<String, GiftEntity> storeGift = new HashMap<String, GiftEntity>();
   private ArrayList<GiftEntity> allStoreGifts = new ArrayList<GiftEntity>();

   @Override public boolean readFromJson(JsonReader reader, String tag) throws IOException {
      reader.beginObject();
      String name = null;
      GiftEntity entity = null;
      while (reader.hasNext()) {
         name = reader.nextName();
         if (super.readFromJson(reader, name)) {
            continue;
         } else if ("list".equals(name)) {
            reader.beginObject();
            while (reader.hasNext()) {
               name = reader.nextName();
               entity = new GiftEntity();
               entity.readFromJson(reader, name);
               entity.setIndex(name);
               storeGift.put(name, entity);
               allStoreGifts.add(entity);
            }
            reader.endObject();
         } else {
            reader.skipValue();
         }
      }
      reader.endObject();
      return true;
   }

   public HashMap<String, GiftEntity> getStoreGift() {
      return storeGift;
   }

   public ArrayList<GiftEntity> getAllStoreGifts() {

      return allStoreGifts;
   }

}
