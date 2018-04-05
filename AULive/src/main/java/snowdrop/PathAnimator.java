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
package snowdrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import com.jack.utils.Trace;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class PathAnimator extends AbstractPathAnimator {
   private final AtomicInteger mCounter = new AtomicInteger(0);
   private Handler mHandler;
   Context context;

   public PathAnimator(Config config, Context context) {
      super(config);
      mHandler = new Handler(Looper.getMainLooper());
      this.context = context;
   }

   public static int clean_gc_count = 0;

   @Override public void start(final View child, final ViewGroup parent) {
      parent.addView(child, new ViewGroup.LayoutParams(mConfig.heartWidth, mConfig.heartHeight));
      FloatAnimation anim =
          new FloatAnimation(createPath(mCounter, parent, 2), randomRotation(), parent, child);
      anim.setDuration(mConfig.animDuration);
      anim.setInterpolator(new LinearInterpolator());
      anim.setAnimationListener(new Animation.AnimationListener() {
         @Override public void onAnimationEnd(Animation animation) {
            mHandler.post(new Runnable() {
               @Override public void run() {
                  parent.removeView(child);
                  imageViews_cache_in_use.remove(child);
                  imageViews_cache_no_use.addLast((ImageView) child);

                  //Trace.d("imageViews_cache_no_use size:"
                  //    + imageViews_cache_no_use.size()
                  //    + "imageViews_cache_in_use size:"
                  //    + imageViews_cache_in_use.size());

                  //recycleImageView(child);
                  //2百个心，gc一下
                  if (clean_gc_count < 300) {
                     clean_gc_count++;
                  } else {
                     clean_gc_count = 0;
                     if (imageViews_cache_no_use.size() > 100) {
                        imageViews_cache_no_use.clear();
                     }
                     //System.gc();
                     Trace.d("PathAnimator System.gc();");
                  }
               }
            });
            mCounter.decrementAndGet();
         }

         @Override public void onAnimationRepeat(Animation animation) {

         }

         @Override public void onAnimationStart(Animation animation) {
            mCounter.incrementAndGet();
         }
      });
      anim.setInterpolator(new LinearInterpolator());
      child.startAnimation(anim);
   }

   static class FloatAnimation extends Animation {
      private PathMeasure mPm;
      private View mView;
      private float mDistance;
      private float mRotation;

      public FloatAnimation(Path path, float rotation, View parent, View child) {
         mPm = new PathMeasure(path, false);
         mDistance = mPm.getLength();
         mView = child;
         mRotation = rotation;
         parent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
      }

      @Override protected void applyTransformation(float factor, Transformation transformation) {
         Matrix matrix = transformation.getMatrix();
         mPm.getMatrix(mDistance * factor, matrix, PathMeasure.POSITION_MATRIX_FLAG);
         mView.setRotation(mRotation * factor);
         float scale = 1F;
         if (3000.0F * factor < 200.0F) {
            scale =
                scale(factor, 0.0D, 0.06666667014360428D, 0.20000000298023224D, 1.100000023841858D);
         } else if (3000.0F * factor < 300.0F) {
            scale =
                scale(factor, 0.06666667014360428D, 0.10000000149011612D, 1.100000023841858D, 1.0D);
         }
         mView.setScaleX(scale);
         mView.setScaleY(scale);
         transformation.setAlpha(1.0F - factor);
      }
   }

   private static float scale(double a, double b, double c, double d, double e) {
      return (float) ((a - b) / (c - b) * (e - d) + d);
   }

   /**
    * 回收ImageView占用的图像内存;
    *
    * @param view 123
    */

   public static void recycleImageView(View view) {

      if (view == null) {
         return;
      }

      if (view instanceof ImageView) {
         Drawable drawable = ((ImageView) view).getDrawable();
         if (drawable instanceof BitmapDrawable) {
            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
            if (bmp != null && !bmp.isRecycled()) {
               ((ImageView) view).setImageBitmap(null);
               bmp.recycle();
               bmp = null;
            }
         }
      }
   }

   ///views重复用
   //没有使用
   private LinkedList<ImageView> imageViews_cache_no_use = new LinkedList<ImageView>();
   //使用中
   private LinkedList<ImageView> imageViews_cache_in_use = new LinkedList<ImageView>();

   public ImageView getHeartView() {
      if (imageViews_cache_no_use.size() < 1) {
         ImageView imageView = new ImageView(context);
         imageViews_cache_in_use.addLast(imageView);
         return imageView;
      }
      ImageView no_use_iv = imageViews_cache_no_use.removeFirst();
      imageViews_cache_in_use.addLast(no_use_iv);
      return no_use_iv;
   }
}

