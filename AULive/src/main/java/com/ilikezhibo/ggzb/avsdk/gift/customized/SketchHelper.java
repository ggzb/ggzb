package com.ilikezhibo.ggzb.avsdk.gift.customized;

/**
 * Created by big on 11/18/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;

import android.view.View;
import com.jack.utils.Trace;

import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.gift.luxurygift.LuxuryGiftUtil;
import java.util.ArrayList;
import java.util.List;

public final class SketchHelper {
   int height2;
   Bitmap m_bitmap;
   Canvas canvas;
   Context context;
   int width;
   int height;
   int sideLength;
   Paint m_paint = new Paint();
   PointF start_point;
   AllPoint allPoint = new AllPoint();
   public RefleshInterface refleshInterface;
   RectF rectF;
   RectF windowRectF;
   PointEntity.PointDataBean pointDataBean;
   List<PointEntity.PointDataBean> pointDataBeanList;
   public int count;
   private Handler handler = new Handler();
   public int point_type;

   public SketchHelper(Context paramContext) {
      this.context = paramContext;
      this.rectF = new RectF();
      pointDataBeanList = new ArrayList<PointEntity.PointDataBean>();
   }

   public final void reset() {
      this.start_point = null;
   }

   //按坐标画图标
   final void drawIcon(float point_x, float point_y) {
      if ((this.m_bitmap == null)
          || (this.m_bitmap.isRecycled() || pointDataBeanList.size() >= 200)
           ) {
         return;
      }
      // 如果绘制到边界外, 则return
      if(windowRectF != null && !windowRectF.contains(point_x, point_y)) {
         return;
      }

      if (this.start_point != null) {
         /*if (allPoint.points.length == allPoint.count) {
            return;
         }*/
         // 得到移动的距离
         double dis = Math.sqrt(
             Math.pow((point_x - start_point.x), 2) + Math.pow((point_y - start_point.y), 2));
         // 移动距离过短
         if (dis < this.width) {
            return;
         } else {
            // 按照移动方向确定下一个点
            double degrees = Math.atan2(point_x - start_point.x, start_point.y - point_y);
            point_x = (float) (start_point.x + this.width * Math.sin(degrees));
            point_y = (float) (start_point.y - this.width * Math.cos(degrees));
         }
         drawing(point_x, point_y);
      }
      if (this.start_point == null) {
         this.start_point = new PointF();
         drawing(point_x, point_y);
      }
      //this.canvas.drawBitmap(this.m_bitmap, point_x,point_y, this.m_paint);
      recordPoint(point_x, point_y);

      this.start_point.set(point_x, point_y);

      this.refleshInterface.onreflsesh();
   }

   /**
    * 在此点画图
    *
    * @param point_x
    * @param point_y
    */

   //用于记录显示到的指数,显示完后关闭
   public static int current_showing_index = 0;
   public PointEntity current_pointEntity;
   public View root_view;
   public Activity activity;
   public boolean isShow;

   public void drawing(final float point_x, final float point_y) {
      // 如果是为了显示
      if (isShow) {
         handler.postDelayed(new Runnable() {
            @Override public synchronized void run() {
               current_showing_index++;
               if (current_pointEntity != null
                   && current_pointEntity.getPoint_data().size() == current_showing_index) {
                  handler.postDelayed(new Runnable() {
                     @Override public void run() {
                        root_view.setVisibility(View.GONE);
                        LuxuryGiftUtil.is_showing_luxury_gift = false;
                        if (activity instanceof AvActivity) {
                           AvActivity temp_activity = (AvActivity) context;
                           //显示礼物动画
                           temp_activity.hasAnyLuxuryGift();
                        }
                     }
                  }, 2000);
               }
               RectF newrectF = new RectF();
               newrectF.left = (point_x - width / 2);
               newrectF.top = (point_y - height / 2);
               newrectF.right = (point_x + width / 2);
               newrectF.bottom = (point_y + height / 2);
               canvas.drawBitmap(m_bitmap, null, newrectF, m_paint);
               refleshInterface.onreflsesh();
            }
         }, 150 * count);
         count++;
      } else {
         this.rectF.left = (point_x - this.width / 2);
         this.rectF.top = (point_y - this.height / 2);
         this.rectF.right = (point_x + this.width / 2);
         this.rectF.bottom = (point_y + this.height / 2);
         Trace.d("SketchHelper drawIcon");
         this.canvas.drawBitmap(this.m_bitmap, null, this.rectF, this.m_paint);
      }
   }

   /**
    * 存储点位置
    *
    * @param point_x x坐标
    * @param point_y y坐标
    */
   private void recordPoint(float point_x, float point_y) {
//      float recordX = point_x / sideLength * ratio;
//      float recordY = point_y / sideLength * ratio;
      pointDataBean = new PointEntity.PointDataBean();
      pointDataBean.setX(String.format("%.3f", point_x));
      pointDataBean.setY(String.format("%.3f", point_y));
      pointDataBeanList.add(pointDataBean);
   }

   public static abstract interface RefleshInterface {
      public abstract void onreflsesh();
   }
}
