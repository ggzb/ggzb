package com.ilikezhibo.ggzb.dbhelper.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by big on 4/13/16.
 */

@DatabaseTable(tableName = "db_userinfo")
public class DBUserInfo {
   @DatabaseField(id = true)
   private String id;
   @DatabaseField
   private String name;
   @DatabaseField
   private String portraitUri;

   public DBUserInfo() {
   }

   public DBUserInfo(String id, String name, String portraitUri) {
      this.id = id;
      this.name = name;
      this.portraitUri = portraitUri;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPortraitUri() {
      return portraitUri;
   }

   public void setPortraitUri(String portraitUri) {
      this.portraitUri = portraitUri;
   }
}
