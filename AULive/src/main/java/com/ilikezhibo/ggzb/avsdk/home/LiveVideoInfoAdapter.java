package com.ilikezhibo.ggzb.avsdk.home;

import android.app.Activity;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jack.utils.DragViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.entity.AVEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LiveVideoInfoAdapter extends BaseAdapter {
   private int resourceId;
   private Activity context;
   private AVEntity liveVideoInfo;
   private ClipboardManager clip;
   //private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

   public LiveVideoInfoAdapter(Activity context, int textViewResourceId) {
      resourceId = textViewResourceId;
      this.context = context;
   }

   private ArrayList<AVEntity> entities;

   public void setEntities(ArrayList<AVEntity> entities) {
      this.entities = entities;
   }

   @Override public int getCount() {
      if (entities != null && entities.size() > 0) {
         return entities.size();
      }

      return 0;
   }

   @Override public AVEntity getItem(int position) {
      return entities.get(position);
   }

   @Override public long getItemId(int position) {

      return 0;
   }

   @Override public View getView(int position, View convertView, ViewGroup parent) {
      final ViewHolder holder;
      if (convertView == null) {

         convertView = LayoutInflater.from(context).inflate(resourceId, null);

         holder = new ViewHolder();
         holder.imageViewCoverImage =
                 (ImageView) convertView.findViewById(R.id.image_view_live_cover_image);
         holder.imageButtonUserFace =
                 (ImageView) convertView.findViewById(R.id.image_btn_user_face);
         holder.textViewUserName = (TextView) convertView.findViewById(R.id.text_view_user_name);
         holder.textViewLiveTitle =
                 (TextView) convertView.findViewById(R.id.text_view_live_nickname);
         holder.tv_topics = (TextView) convertView.findViewById(R.id.tv_topics);
         holder.textViewLiveViewCount =
                 (TextView) convertView.findViewById(R.id.text_view_live_viewcount);
         holder.iv_payliving = (ImageView) convertView.findViewById(R.id.iv_payliving);

         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }
      liveVideoInfo = getItem(position);
      String param = liveVideoInfo.face;
      String coverUrl = param;
      if (param.length() > 0) {

         ImageLoader.getInstance()
                 .displayImage(coverUrl, holder.imageViewCoverImage,
                         AULiveApplication.getGlobalImgOptions());

         //ImageSize targetSize = new ImageSize(200, 200);
         //ImageLoader.getInstance()
         //    .loadImage(coverUrl, targetSize, AULiveApplication.getGlobalImgOptions(),
         //        new ImageLoadingListener() {
         //           @Override public void onLoadingStarted(String s, View view) {
         //
         //           }
         //
         //           @Override
         //           public void onLoadingFailed(String s, View view, FailReason failReason) {
         //
         //           }
         //
         //           @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
         //              holder.imageViewCoverImage.setImageBitmap(bitmap);
         //              //holder.imageButtonUserFace.setImageBitmap(bitmap);
         //              //if (!bitmap.isRecycled()) {
         //              //   bitmap.recycle();  //回收图片所占的内存
         //              //}
         //           }
         //
         //           @Override public void onLoadingCancelled(String s, View view) {
         //
         //           }
         //        });
      }
      // 设置大小
      int[] size = DragViewUtils.getScreenSize(context);
      android.view.ViewGroup.LayoutParams lp = holder.imageViewCoverImage.getLayoutParams();
      lp.height = size[0];
      holder.imageViewCoverImage.setLayoutParams(lp);

      holder.imageButtonUserFace.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View v) {

         }
      });

      //头像与封面相同
      String headurl = liveVideoInfo.face;
      ImageLoader.getInstance()
              .displayImage(headurl, holder.imageButtonUserFace,
                      AULiveApplication.getGlobalImgOptions());

      //ImageSize targetSize = new ImageSize(50, 50);
      //ImageLoader.getInstance()
      //    .loadImage(headurl, targetSize, AULiveApplication.getGlobalImgOptions(),
      //        new ImageLoadingListener() {
      //           @Override public void onLoadingStarted(String s, View view) {
      //
      //           }
      //
      //           @Override public void onLoadingFailed(String s, View view, FailReason failReason) {
      //
      //           }
      //
      //           @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
      //              holder.imageButtonUserFace.setImageBitmap(bitmap);
      //              //if (!bitmap.isRecycled()) {
      //              //   bitmap.recycle();  //回收图片所占的内存
      //              //}
      //           }
      //
      //           @Override public void onLoadingCancelled(String s, View view) {
      //
      //           }
      //        });

      //城市
      holder.textViewUserName.setText(liveVideoInfo.city);
      //昵称
      holder.textViewLiveTitle.setText(liveVideoInfo.nickname);
      //观看人数
      holder.textViewLiveViewCount.setText(liveVideoInfo.total);

      if (liveVideoInfo.payliving == 1) {
         holder.iv_payliving.setBackgroundResource(R.drawable.live_pay);
      } else {
         holder.iv_payliving.setBackgroundResource(R.drawable.live_free);
      }

      //话题
      if (liveVideoInfo.title != null && !liveVideoInfo.title.equals("")) {
         holder.tv_topics.setText(liveVideoInfo.title);
         holder.tv_topics.setVisibility(View.VISIBLE);
      } else {
         holder.tv_topics.setVisibility(View.GONE);
      }
      return convertView;
   }

   private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
      static final List<String> displayedImages =
              Collections.synchronizedList(new LinkedList<String>());

      @Override public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
         if (loadedImage != null) {
            ImageView imageView = (ImageView) view;
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
               FadeInBitmapDisplayer.animate(imageView, 500);
               displayedImages.add(imageUri);
            }
         }
      }
   }

   static class ViewHolder {
      public TextView textViewLiveTitle;
      public TextView tv_topics;
      public ImageView imageButtonUserFace;
      public ImageView imageViewCoverImage;
      public TextView textViewLiveViewCount;
      public TextView textViewUserName;
      public ImageView iv_payliving;
   }
}
