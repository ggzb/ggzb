package com.ilikezhibo.ggzb.avsdk.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.home.entity.AdvEntity;
import java.util.ArrayList;
import java.util.List;

public class PageUtil {
   /** 是否循环 */
   public static final boolean isCycle = true;

   /**
    * 获取ViewPage适配器数据
    */
   public static List<View> getPageList(Context context, ArrayList<AdvEntity> advEntities) {
      List<View> pageList = new ArrayList<View>();
      for (AdvEntity advEntity : advEntities) {
         pageList.add(getPageView(context, advEntity.pic));
      }
      return pageList;
   }

   private static View getPageView(Context context, String url) {
      LayoutInflater inflater = LayoutInflater.from(context);
      View pageView = inflater.inflate(R.layout.page_item, null);
      ImageView imgPage = (ImageView) pageView.findViewById(R.id.imgPage);
      ImageLoader.getInstance().displayImage(url, imgPage, AULiveApplication.getGlobalImgOptions());
      return pageView;
   }
}
