package com.ilikezhibo.ggzb.avsdk.search;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jack.utils.ScreenUtils;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;

public class SearchListActivity extends BaseFragmentActivity {

   private SearchFragment userInfoFragment;
   private FragmentManager manager;

   @Override protected void setContentView() {
      setContentView(R.layout.activity_zhaofang);

      userInfoFragment = new SearchFragment();
      manager = getSupportFragmentManager();
      manager.beginTransaction().replace(R.id.map, userInfoFragment, "map_fragment").commit();

      // 创建TextView,为了设置StatusBar的颜色
      TextView textView = new TextView(this);
      getWindowManager().getDefaultDisplay();
      LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusHeight(this));
      textView.setBackgroundColor(ContextCompat.getColor(this,R.color.global_main_bg));
      textView.setLayoutParams(lParams);
      // 获得根视图并把TextView加进去。
      ViewGroup view = (ViewGroup) getWindow().getDecorView();
      view.addView(textView);
   }

   @Override protected void initializeViews() {

   }

   @Override protected void initializeData() {

   }

   @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
   }

   @Override protected void onRestart() {
      super.onRestart();
   }

   @Override public void onStart() {
      super.onStart();
   }

   @Override public void onResume() {
      super.onResume();
   }

   @Override public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
   }

   @Override public void onPause() {
      super.onPause();
   }

   @Override public void onStop() {

      super.onStop();
   }

   @Override public void onDestroy() {
      manager.getFragments().clear();
      super.onDestroy();
   }

   @Override public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
   }
}
