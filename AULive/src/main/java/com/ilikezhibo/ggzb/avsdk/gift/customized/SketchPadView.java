package com.ilikezhibo.ggzb.avsdk.gift.customized;

import android.app.Activity;
import android.graphics.Bitmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.jack.utils.DragViewUtils;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.R;
import java.util.ArrayList;

import snowdrop.HeartLayout;

public class SketchPadView extends View {
   private static Paint f = new Paint();
   private Context context;
   public SketchHelper sketchHelper;
   private Bitmap bitmap;
   private int[] icons = {R.drawable.draw_gift_item0, R.drawable.draw_gift_item1,
           R.drawable.draw_gift_item2, R.drawable.draw_gift_item3};


   public SketchPadView(Context paramContext) {
      super(paramContext);
      this.context = paramContext;
      init();
   }

   public SketchPadView(Context paramContext, AttributeSet paramAttributeSet) {
      super(paramContext, paramAttributeSet);
      this.context = paramContext;
      init();
   }

   public SketchPadView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
      super(paramContext, paramAttributeSet, paramInt);
      this.context = paramContext;
      init();
   }

   private void init() {
      this.sketchHelper = new SketchHelper(context);
      int[] size = DragViewUtils.getScreenSize(context);
      sketchHelper.sideLength = size[0];
      sketchHelper.windowRectF = new RectF();
      sketchHelper.windowRectF.left = 0;
      sketchHelper.windowRectF.right = 0 + size[0];
      sketchHelper.windowRectF.top = 0;
      sketchHelper.windowRectF.bottom = 0 + size[0];
      //设置为正方形
      getViewTreeObserver().addOnGlobalLayoutListener(
          new ViewTreeObserver.OnGlobalLayoutListener() {
             public final void onGlobalLayout() {
                if (bitmap == null) {
                   int width = SketchPadView.this.getMeasuredWidth();
                   int height = SketchPadView.this.getMeasuredHeight();
                   if ((width > 0) && (height > 0)) {
                      ViewGroup.LayoutParams temp_layoutParams =
                          SketchPadView.this.getLayoutParams();
                      //int temp_width = (int) (1.333F * temp_layoutParams.width);
                      //if (temp_width < temp_layoutParams.height) {
                      //   temp_layoutParams.height = temp_width;
                      //   SketchPadView.this.setLayoutParams(temp_layoutParams);
                      //}

                      temp_layoutParams.height = width;
                      SketchPadView.this.setLayoutParams(temp_layoutParams);

                      bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
                      sketchHelper.canvas = new Canvas(bitmap);
                   }
                }
             }
          });

      //滑动的监听器
//      setOnTouchListener(this.onTouchListener);
     /* this.sketchHelper.refleshInterface = new SketchHelper.RefleshInterface() {
         public final void onreflsesh() {

            SketchPadView.this.invalidate();
         }
      };*/

      //test
      // 此处选择绘制的图形
      setIconRes();
   }

   //清空
   public final void clean() {
      SketchHelper temp_SketchHelper = this.sketchHelper;
      Paint localPaint = new Paint();
      localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
      if (temp_SketchHelper.canvas != null) {
         temp_SketchHelper.canvas.drawPaint(localPaint);
      }
      temp_SketchHelper.pointDataBeanList = new ArrayList<PointEntity.PointDataBean>();
      temp_SketchHelper.reset();
      invalidate();
   }

   public int getIconPointCount() {
      return this.sketchHelper.pointDataBeanList.size();
   }

   //接收到个性礼物后显现
   public void getPathPointsAndDraw(PointEntity pointEntity, View root_view, Activity activity) {
      clean();
      sketchHelper.count=0;
      sketchHelper.isShow = true;
      sketchHelper.current_showing_index = 0;
      sketchHelper.current_pointEntity = pointEntity;
      sketchHelper.root_view = root_view;
      sketchHelper.activity = activity;

      //根据返回的图标类型设置图片
//      if(pointEntity.point_type < 0 || pointEntity.point_type > icons.length - 1) {
//         pointEntity.point_type = 0;
//      }
//      Bitmap bitmap = HeartLayout.readBitMap(context, icons[pointEntity.point_type]);
      setIconRes();
      setIconBitmap(pointEntity.point_type);

      //去掉划屏
      this.setOnTouchListener(null);

      int ratio = pointEntity.getSquare_size();
      int count = 0;
      float pointX;
      float pointY;
      while (count < pointEntity.getPoint_data().size()) {
         pointX =
             conversionRatio(ratio, Float.valueOf(pointEntity.getPoint_data().get(count).getX()));
         pointY =
             conversionRatio(ratio, Float.valueOf(pointEntity.getPoint_data().get(count).getY()));
         sketchHelper.drawing(pointX, pointY);
         count++;
      }
   }

   private float conversionRatio(int ratio, Float value) {
      return value / ratio * sketchHelper.sideLength;
   }

   protected void onDraw(Canvas paramCanvas) {
      super.onDraw(paramCanvas);
      if (this.bitmap != null) {
         Trace.d("SketchPadView onDraw");
         paramCanvas.drawBitmap(this.bitmap, 0.0F, 0.0F, f);
      }
   }

   public static int getIconWidth(Context paramContext, float paramFloat) {
      return (int) (0.5D + paramContext.getResources().getDisplayMetrics().density * paramFloat);
   }

   //设置当前要画图片,
   public void setIconRes() {
      SketchHelper sketchHelper1 = this.sketchHelper;
      sketchHelper1.width = getIconWidth(sketchHelper1.context, 30.0F);
      sketchHelper1.height = sketchHelper1.width;
      sketchHelper1.m_paint.setAntiAlias(true);
      sketchHelper1.m_paint.setFilterBitmap(true);

      sketchHelper1.height2 = getIconWidth(sketchHelper1.context, 34.0F);
   }

   public void setIconBitmap(int id) {
      Bitmap bitmap = HeartLayout.readBitMap(context, icons[id]);
      sketchHelper.m_bitmap = bitmap;
   }
}
