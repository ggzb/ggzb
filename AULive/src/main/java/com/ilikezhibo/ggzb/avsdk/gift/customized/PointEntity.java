package com.ilikezhibo.ggzb.avsdk.gift.customized;

import java.util.List;

/**
 * Created by r on 16/11/21.
 */

public class PointEntity {

   /**
    * point_count : 20
    * point_data : [{"x":"103.333","y":"261.667"},{"x":"109.333","y":"250.333"}]
    * square_size : 414
    */

   public int point_count;

   public int getPoint_type() {
      return point_type;
   }

   public void setPoint_type(int point_type) {
      this.point_type = point_type;
   }

   public int point_type;
   public int square_size;
   public List<PointDataBean> point_data;

   public int getPoint_count() {
      return point_count;
   }

   public void setPoint_count(int point_count) {
      this.point_count = point_count;
   }

   public int getSquare_size() {
      return square_size;
   }

   public void setSquare_size(int square_size) {
      this.square_size = square_size;
   }

   public List<PointDataBean> getPoint_data() {
      return point_data;
   }

   public void setPoint_data(List<PointDataBean> point_data) {
      this.point_data = point_data;
   }

   public static class PointDataBean {
      /**
       * x : 103.333
       * y : 261.667
       */

      private String x;
      private String y;

      public String getX() {
         return x;
      }

      public void setX(String x) {
         this.x = x;
      }

      public String getY() {
         return y;
      }

      public void setY(String y) {
         this.y = y;
      }
   }
}
