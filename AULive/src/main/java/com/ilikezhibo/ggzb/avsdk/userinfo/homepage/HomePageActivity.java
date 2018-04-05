package com.ilikezhibo.ggzb.avsdk.userinfo.homepage;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.chat.event.UnReadMsgCountEvent;
import de.greenrobot.event.EventBus;

public class HomePageActivity extends BaseFragmentActivity {

   private HomePageFragment userInfoFragment;
   private FragmentManager manager;

   public static String HOMEPAGE_UID="HOMEPAGE_UID";
   @Override protected void setContentView() {
      setContentView(R.layout.activity_zhaofang);

     String uid= getIntent().getStringExtra(HOMEPAGE_UID);
      userInfoFragment = new HomePageFragment(uid);
      manager = getSupportFragmentManager();
      manager.beginTransaction().replace(R.id.map, userInfoFragment, "map_fragment").commit();
      EventBus.getDefault().register(this);
   }
   //显示有多少条未读信息
   public void onEvent(UnReadMsgCountEvent unread_event) {
      Utils.showCroutonText(this, "您有" + unread_event.count + "条信息未阅读");
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
