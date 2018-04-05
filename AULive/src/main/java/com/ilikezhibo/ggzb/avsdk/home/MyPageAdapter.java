package com.ilikezhibo.ggzb.avsdk.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.avsdk.home.entity.AdvEntity;

import java.util.List;

public class MyPageAdapter extends PagerAdapter {
   private List<AdvEntity> list;
   private int pageCount;
   private Context context;

   public MyPageAdapter(Context context1, List<AdvEntity> list) {
      this.list = list;
      pageCount = list.size();
      this.context = context1;
   }

   @Override public int getCount() {
      if (PageUtil.isCycle) {
         return list == null ? 0 : Integer.MAX_VALUE;
      } else {
         return list == null ? 0 : list.size();
      }
   }

   @Override public boolean isViewFromObject(View arg0, Object arg1) {
      return arg0 == arg1;
   }

   @Override public int getItemPosition(Object object) {
      return super.getItemPosition(object);
   }

   @Override public void destroyItem(ViewGroup container, int position, Object object) {
      if (PageUtil.isCycle && pageCount > 0) {
         position = position % pageCount;
      }
      container.removeView((View) object);
   }

   @Override public Object instantiateItem(ViewGroup container, int position) {

      if (PageUtil.isCycle && pageCount > 0) {
         position = position % pageCount;
      }
      final int index = position;
      ImageView imgPage = new ImageView(context);
      imgPage.setScaleType(ScaleType.CENTER_CROP);
      container.addView(imgPage, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      ImageLoader.getInstance()
          .displayImage(list.get(position).pic, imgPage, AULiveApplication.getGlobalImgOptions());
      imgPage.setOnClickListener(new View.OnClickListener() {

         @Override public void onClick(View view) {
            String url = list.get(index).url;
            String title = list.get(index).title;

            if (url == null) {
               return;
            }

            if (url.startsWith("http")) {
               Intent intent = new Intent(context, WebViewActivity.class);

               intent.putExtra(WebViewActivity.input_url, list.get(index).url);
               intent.putExtra(WebViewActivity.is_from_list_key, true);
               intent.putExtra(WebViewActivity.actity_name, title);
               context.startActivity(intent);
            }
         }
      });
      return imgPage;
   }
}
