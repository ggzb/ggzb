package com.ilikezhibo.ggzb.avsdk.chat.room_chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.ArrayList;

/**
 * Created by big on 4/26/16.
 */

public class PrivateChatListAdapter extends FragmentPagerAdapter {

   private ArrayList<Fragment> pages;
   private String[] tabs;

   public PrivateChatListAdapter(FragmentManager fm, ArrayList<Fragment> pages, String[] tabs) {
      super(fm);
      this.pages = pages;
      this.tabs = tabs;
   }

   @Override public int getCount() {
      return pages.size();
   }

   @Override public Fragment getItem(int position) {
      return pages.get(position);
   }

   @Override public Object instantiateItem(ViewGroup container, int position) {
      Object item = super.instantiateItem(container, position);
      return item;
   }

   @Override public void destroyItem(ViewGroup container, int position, Object object) {
      super.destroyItem(container, position, object);
   }

   @Override public CharSequence getPageTitle(int position) {
      return tabs[position];
   }

   @Override public float getPageWidth(int position) {
      return super.getPageWidth(position);
   }
}
