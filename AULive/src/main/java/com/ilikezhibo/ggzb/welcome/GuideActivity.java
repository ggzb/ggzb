package com.ilikezhibo.ggzb.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.animation.DepthPageTransformer;
import com.ilikezhibo.ggzb.avsdk.ChatMsgListAdapter;
import com.ilikezhibo.ggzb.home.MainActivity;
import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements OnPageChangeListener {

   private TextView pageNum;
   private ViewPager vp;
   private List<View> views;
   private ViewPagerAdapter vpAdapter;
   private LayoutInflater inflater;
   private Bitmap first_bitmap;
   private Bitmap second_bitmap;
   private Bitmap third_bitmap;

   @Override protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // 去掉系统状态栏
      //requestWindowFeature(Window.FEATURE_NO_TITLE);
      //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

      setContentView(R.layout.activity_guide);

      if (Build.VERSION.SDK_INT >= 21) {
         View decorView = getWindow().getDecorView();
         int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
         decorView.setSystemUiVisibility(option);
         getWindow().setStatusBarColor(Color.TRANSPARENT);
      }

      inflater = LayoutInflater.from(this);
      initViews();
      initPageNum();
   }

   private void initPageNum() {
      pageNum = (TextView) findViewById(R.id.page_num);
      pageNum.setText("");
   }

   @Override protected void onDestroy() {
      super.onDestroy();
      if (!first_bitmap.isRecycled()) {
         first_bitmap.recycle();  //回收图片所占的内存
      }
      if (!second_bitmap.isRecycled()) {
         second_bitmap.recycle();  //回收图片所占的内存
      }
      if (!second_bitmap.isRecycled()) {
         second_bitmap.recycle();  //回收图片所占的内存
      }
   }

   private void initViews() {
      views = new ArrayList<View>();

      View views_one = inflater.inflate(R.layout.views_one, null);

      ImageView first_image = (ImageView) views_one.findViewById(R.id.first_image);
      first_bitmap = ChatMsgListAdapter.readBitMap(this, R.drawable.welcom1);
      first_image.setImageBitmap(first_bitmap);

      views.add(views_one);
   //////////////////////////////////////
      View views_tow = inflater.inflate(R.layout.views_two, null);

      ImageView second_image = (ImageView) views_tow.findViewById(R.id.second_image);
      second_bitmap = ChatMsgListAdapter.readBitMap(this, R.drawable.welcom2);
      second_image.setImageBitmap(second_bitmap);

      views.add(views_tow);
   //////////////////////////////////////
      View views_three = inflater.inflate(R.layout.views_three, null);

      ImageView third_image = (ImageView) views_three.findViewById(R.id.third_image);
      third_bitmap = ChatMsgListAdapter.readBitMap(this, R.drawable.welcom3);
      third_image.setImageBitmap(third_bitmap);

      views.add(views_three);

      //////////////////////////////////////
      //views.add(inflater.inflate(R.layout.views_four, null));
      vpAdapter = new ViewPagerAdapter(views, this);

      vp = (ViewPager) findViewById(R.id.viewpager);
      vp.setPageTransformer(true, new DepthPageTransformer());
      vp.setAdapter(vpAdapter);
      vp.setOnPageChangeListener(this);
   }

   public class ViewPagerAdapter extends PagerAdapter {

      private List<View> views;
      private Activity activity;

      public ViewPagerAdapter(List<View> views, Activity activity) {
         this.views = views;
         this.activity = activity;
      }

      @Override public void destroyItem(View arg0, int arg1, Object arg2) {
         ((ViewPager) arg0).removeView(views.get(arg1));
      }

      @Override public int getCount() {

         if (views != null) {
            return views.size();
         }

         return 0;
      }

      @Override public Object instantiateItem(View view, int position) {
         ((ViewPager) view).addView(views.get(position), 0);
         if (position == 0) {
            // 第一项的动画,目前暂用
            // AnimationSet animationSet = new AnimationSet(true);
            // Animation alphaAnimation =
            // AnimationUtils.loadAnimation(GiftPagerUtil.this,
            // R.anim.alpha);
            // Animation tAnimation =
            // AnimationUtils.loadAnimation(GiftPagerUtil.this,
            // R.anim.trans);
            // animationSet.addAnimation(alphaAnimation);
            // animationSet.addAnimation(tAnimation);
            // ImageView imageView =
            // (ImageView)arg0.findViewById(R.id.first_image);
            // imageView.startAnimation(animationSet);
         }
         if (position == views.size() - 1) {
            Button mStart = (Button) view.findViewById(R.id.mstart);

            mStart.setOnClickListener(new OnClickListener() {

               @Override public void onClick(View v) {
                  setGuided();
                  goHome();
               }
            });
         }
         return views.get(position);
      }

      @Override public boolean isViewFromObject(View arg0, Object arg1) {
         return (arg0 == arg1);
      }

      public void goHome() {
         Intent intent = new Intent(activity, MainActivity.class);
         activity.startActivity(intent);
         overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
         activity.finish();
      }

      public void setGuided() {
         SharedPreferences preferences =
             activity.getSharedPreferences("first_pref", Context.MODE_PRIVATE);
         Editor editor = preferences.edit();
         editor.putBoolean("isFirst", false);
         editor.commit();
      }
   }

   @Override public void onPageScrollStateChanged(int arg0) {

   }

   @Override public void onPageScrolled(int arg0, float arg1, int arg2) {

   }

   @Override public void onPageSelected(int arg0) {

   }
}
