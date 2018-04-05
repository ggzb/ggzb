package com.ilikezhibo.ggzb.avsdk.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ilikezhibo.ggzb.views.UpLoadFileWebViewActivity;
import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.jack.utils.Trace;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.avsdk.home.entity.AdvEntity;

import java.util.List;

public class LoopPageAdapter extends LoopPagerAdapter {
   private List<AdvEntity> list;
   private Context context;

   public LoopPageAdapter(Context context1, List<AdvEntity> list, RollPagerView viewPager) {
      super(viewPager);
      this.list = list;
      this.context = context1;
   }

   public void setEntities(List<AdvEntity> list) {
      this.list = list;
   }

   @Override public View getView(ViewGroup container, int position) {
      final int index = position;
      ImageView imgPage = new ImageView(context);
      imgPage.setScaleType(ScaleType.FIT_XY);
      //container.addView(imgPage, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
               Intent intent = new Intent(context, UpLoadFileWebViewActivity.class);
               String url_add =
                   list.get(index).url + "?uid=" + AULiveApplication.getUserInfo().getUid();
               Trace.d("url_add:" + url_add);
               intent.putExtra(WebViewActivity.input_url, url_add);
               intent.putExtra(WebViewActivity.is_from_list_key, true);
               intent.putExtra(WebViewActivity.actity_name, title);
               context.startActivity(intent);
            }
         }
      });
      return imgPage;
   }

   @Override public int getRealCount() {
      return list.size();
   }
}
