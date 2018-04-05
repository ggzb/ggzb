package com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jack.utils.FastBlurUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ilikezhibo.ggzb.avsdk.activity.entity.AVEntity;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.List;

public class SwitchAnchorPagerAdapter extends PagerAdapter {
   private SoftReference<Activity> mActRef;
   private List<View> mCacheList = new LinkedList();
   private List<AVEntity> mData;

   public SwitchAnchorPagerAdapter(Activity paramActivity) {
      this.mActRef = new SoftReference(paramActivity);
   }

   public void destroyItem(ViewGroup container, int position, Object object) {
      View localView = (View) object;
      ((VerticalViewPager) container).removeView(localView);
      //localView.setVisibility(View.GONE);
      //组件复用
      this.mCacheList.add(localView);
   }

   public int getCount() {
      int i = 1;
      if ((this.mData == null) || (this.mData.isEmpty())) {
         i = 0;
         return i;
      }
      i = mData.size();
      return i;
   }

   public Object instantiateItem(ViewGroup container, int position) {
      int i = this.mData.size();
      AVEntity avEntity = (AVEntity) this.mData.get(position % i);
      Context activity = this.mActRef.get();

      //TextView image_iv = new TextView(activity);

      ImageView image_iv = null;
      if (activity != null) {
         if (!(this.mCacheList.isEmpty())) {
            image_iv = (ImageView) this.mCacheList.remove(0);
         } else {
            image_iv = new ImageView(activity);
            //View content=LayoutInflater.from(mActRef.get()).inflate(R.layout.jiesihuo_my_info_layout_zoom_view, null);
            //image_iv=(ImageView) content.findViewById(R.id.iv_zoom);

         }
      }

      //test
      //int color=R.color.yellow;
      //int color2=R.color.qqblue;
      //int[] colors={color,color2};
      //image_iv.setBackgroundColor(localObject.getResources().getColor(colors[position%2]));

      final ImageView image_iv_temp = image_iv;
      image_iv_temp.setScaleType(ImageView.ScaleType.CENTER_CROP);
      //模糊背景
      ImageLoader.getInstance().displayImage(avEntity.face, image_iv, new ImageLoadingListener() {
         @Override public void onLoadingStarted(String s, View view) {

         }

         @Override public void onLoadingFailed(String s, View view, FailReason failReason) {

         }

         @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            if (bitmap == null) {
               return;
            }
            image_iv_temp.setImageBitmap(FastBlurUtil.toBlur(bitmap, 10));
         }

         @Override public void onLoadingCancelled(String s, View view) {

         }
      });

      image_iv.setVisibility(View.VISIBLE);
      image_iv.setTag(Integer.valueOf(position));

      //ViewPager.LayoutParams localLayoutParams = new ViewPager.LayoutParams();
      //localLayoutParams.width = -1;
      //localLayoutParams.height = WatchSwitchViewPager.status_screen_height-25;
      //container.addView(image_iv, localLayoutParams);

      image_iv.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
      container.addView(image_iv);

      return image_iv;
   }

   public boolean isViewFromObject(View paramView, Object paramObject) {
      return (paramView == paramObject);
   }

   public void setData(List<AVEntity> paramList) {
      this.mData = paramList;
      notifyDataSetChanged();
   }
}
