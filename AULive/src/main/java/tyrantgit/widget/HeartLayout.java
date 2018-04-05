/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tyrantgit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.ilikezhibo.ggzb.R;
import java.io.InputStream;
import java.util.HashMap;

public class HeartLayout extends RelativeLayout {

   private AbstractPathAnimator mAnimator;
   private Context context;

   public HeartLayout(Context context) {
      super(context);
      this.context = context;
      init(null, 0);
   }

   public HeartLayout(Context context, AttributeSet attrs) {
      super(context, attrs);
      this.context = context;
      init(attrs, 0);
   }

   public HeartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      this.context = context;
      init(attrs, defStyleAttr);
   }

   private void init(AttributeSet attrs, int defStyleAttr) {

      final TypedArray a =
          getContext().obtainStyledAttributes(attrs, R.styleable.HeartLayout, defStyleAttr, 0);

      mAnimator = new PathAnimator(AbstractPathAnimator.Config.fromTypeArray(a), context);

      a.recycle();
   }

   public AbstractPathAnimator getAnimator() {
      return mAnimator;
   }

   public void setAnimator(AbstractPathAnimator animator) {
      clearAnimation();
      mAnimator = animator;
   }

   public void clearAnimation() {
      for (int i = 0; i < getChildCount(); i++) {
         getChildAt(i).clearAnimation();
      }
      removeAllViews();
   }

   //动态随机颜色的方法
   public void addHeart(int color) {
      HeartView heartView = new HeartView(getContext());
      heartView.setColor(color);
      mAnimator.start(heartView, this);
   }

   public void addHeart(int color, int heartResId, int heartBorderResId) {
      HeartView heartView = new HeartView(getContext());
      heartView.setColorAndDrawables(color, heartResId, heartBorderResId);
      mAnimator.start(heartView, this);
   }

   //////////////////////////////////////////////////////////////////////////////////////////////
   //随机图片选择方法
   public static int[] pics_other = {
       R.drawable.like_other1, R.drawable.like_other2, R.drawable.like_other3,
       R.drawable.like_other4, R.drawable.like_other5, R.drawable.like_other6,
       R.drawable.like_other7
   };
   public static int[] pics_like = {
       R.drawable.like_self1, R.drawable.like_self2, R.drawable.like_self3
   };

   public void addHeart2(int ResourceId) {
      //方法1
      //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ResourceId);

      //方法2,最耗内存
      //ImageView imageView = new ImageView(getContext());
      //imageView.setImageResource(ResourceId);

      PathAnimator pathAnimator = (PathAnimator) mAnimator;
      ImageView imageView = pathAnimator.getHeartView();
      Bitmap bitmap = getBitMap(ResourceId);
      imageView.setImageBitmap(bitmap);
      mAnimator.start(imageView, this);
   }

   //图片缓存
   private HashMap<Integer, Bitmap> bitmap_weakHashMap = new HashMap<Integer, Bitmap>();

   private Bitmap getBitMap(int res_id) {
      //Trace.d("bitmap_weakHashMap.get(res_id):"
      //    + bitmap_weakHashMap.get(res_id)
      //    + "size:"
      //    + bitmap_weakHashMap.size());
      if (bitmap_weakHashMap.get(res_id) == null) {
         Bitmap bitmap = readBitMap(context, res_id);
         bitmap_weakHashMap.put(res_id, bitmap);
         return bitmap;
      }
      return bitmap_weakHashMap.get(res_id);
   }

   /**
    * 以最省内存的方式读取本地资源的图片
    */
   public static Bitmap readBitMap(Context context, int resId) {
      BitmapFactory.Options opt = new BitmapFactory.Options();
      opt.inPreferredConfig = Bitmap.Config.RGB_565;
      opt.inPurgeable = true;
      opt.inInputShareable = true;
// 获取资源图片
      InputStream is = context.getResources().openRawResource(resId);
      return BitmapFactory.decodeStream(is, null, opt);
   }
}
