package com.ilikezhibo.ggzb.avsdk.activity.entity;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: GiftEntity
 * @Description: 主页内容
 * @date 2016-3-1 下午4:05:19
 */
public class AVEntity extends BaseEntity implements Serializable {
   //"uid": "16708479",
   //    "face": "http://img.hrbhzkj.com/face/127/16708479big.jpg?v=34",
   //    "nickname": "饿啊来看看我的小女孩子",
   //    "grade": 29,
   //    "city": "",
   //    "total": 1,
   //    "is_live": "1",
   //    "title": "饿啊来看看我的小女孩子",
   //    "time": "1461383073"

   public String city;
   public String total;
   public String is_live;

   public long time;

   public String url;
   private String img;
   private String type;

   private String name;
   private String id;
   private String memo;

   private ArrayList<String> child;
   // 标题
   private String tab;
   private String has_title = "";
   private boolean empty = false;
   private long news;
   private boolean checked = false;

   public String grade;
   //广场列表专用
   public String face;
   public String nickname;
   public String uid;
   public String title;
   public String viewers;
   public String praise;
   public int payliving;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getImg() {
      return img;
   }

   public void setImg(String img) {
      this.img = img;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getMemo() {
      return memo;
   }

   public void setMemo(String memo) {
      this.memo = memo;
   }

   public String getTab() {
      return tab;
   }

   public void setTab(String tab) {
      this.tab = tab;
   }

   public String getHas_title() {
      return has_title;
   }

   public void setHas_title(String has_title) {
      this.has_title = has_title;
   }

   public boolean isEmpty() {
      return empty;
   }

   public void setEmpty(boolean empty) {
      this.empty = empty;
   }

   public long getNews() {
      return news;
   }

   public void setNews(long news) {
      this.news = news;
   }

   public ArrayList<String> getChild() {
      return child;
   }

   public void setChild(ArrayList<String> child) {
      this.child = child;
   }

   public boolean isChecked() {
      return checked;
   }

   public void setChecked(boolean checked) {
      this.checked = checked;
   }
}
