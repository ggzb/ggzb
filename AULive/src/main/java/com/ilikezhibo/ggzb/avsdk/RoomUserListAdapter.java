package com.ilikezhibo.ggzb.avsdk;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.userinfo.UserInfoHelper;
import java.util.ArrayList;
import popwindow.PopupWindowUtil;

/**
 * Created by big on 3/5/16.
 */
public class RoomUserListAdapter extends RecyclerView.Adapter<RoomUserListAdapter.ViewHolder> {

   // 数据集
   private ArrayList<MemberInfo> mNormalMemberList;
   private AvActivity avActivity;

   public RoomUserListAdapter(ArrayList<MemberInfo> mNormalMemberList, AvActivity avActivity) {
      super();
      this.mNormalMemberList = mNormalMemberList;
      this.avActivity = avActivity;
   }

   @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
      // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
      View view = View.inflate(viewGroup.getContext(), R.layout.room_user_item, null);
      // 创建一个ViewHolder
      ViewHolder holder = new ViewHolder(view);
      return holder;
   }

   @Override public void onBindViewHolder(final ViewHolder viewHolder, int i) {
      // 绑定数据到ViewHolder上
      final MemberInfo memberInfo = mNormalMemberList.get(i);
      ImageLoader.getInstance()
          .displayImage(memberInfo.getHeadImagePath(), viewHolder.img_room_creator,
              AULiveApplication.getGlobalImgOptions());
      viewHolder.img_room_creator.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            final PopupWindowUtil popupWindow = new PopupWindowUtil(viewHolder.img_room_creator);
            popupWindow.setContentView(R.layout.dialog_myroom_userinfo);
            popupWindow.setOutsideTouchable(false);
            //popupWindow.showCenter();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
               @Override public void onDismiss() {

               }
            });

            UserInfoHelper userInfoHelper =
                UserInfoHelper.getInstance(viewHolder.img_room_creator, popupWindow, avActivity,
                    memberInfo);
         }
      });

      viewHolder.img_user_type.setVisibility(View.VISIBLE);
      setTopIcon(memberInfo.grade, viewHolder.img_user_type);

      if (memberInfo.offical == 1) {
         setTopIcon("-100", viewHolder.img_user_type);
      }
   }

   @Override public int getItemCount() {
      return mNormalMemberList.size();
   }

   public static class ViewHolder extends RecyclerView.ViewHolder {

      public ImageView img_room_creator;
      public ImageView img_user_type;
      public View root_view;

      public ViewHolder(View itemView) {
         super(itemView);
         root_view = itemView;
         img_room_creator = (ImageView) itemView.findViewById(R.id.img_room_creator);
         img_user_type = (ImageView) itemView.findViewById(R.id.img_user_type);
      }
   }

   public static void setTopIcon(String grade1, ImageView cib) {
      if (grade1 == null || grade1.equals("")) {
         grade1 = "1";
      }
      int grade = Integer.parseInt(grade1);
      int res;
      int each_grade = 16;
      if (grade < each_grade) {
         res = R.drawable.top_icon1;
      } else if (grade < 2 * each_grade && grade >= each_grade) {
         res = R.drawable.top_icon2;
      } else if (grade < 3 * each_grade && grade >= 2 * each_grade) {
         res = R.drawable.top_icon3;
      } else if (grade < 4 * each_grade && grade >= 3 * each_grade) {
         res = R.drawable.top_icon4;
      } else if (grade < 5 * each_grade && grade >= 4 * each_grade) {
         res = R.drawable.top_icon5;
      } else if (grade < 6 * each_grade && grade >= 5 * each_grade) {
         res = R.drawable.top_icon6;
      } else if (grade < 7 * each_grade && grade >= 6 * each_grade) {
         res = R.drawable.top_icon7;
      } else if (grade < 8 * each_grade && grade >= 7 * each_grade) {
         res = R.drawable.top_icon8;
      } else if (grade < 9 * each_grade && grade >= 8 * each_grade) {
         res = R.drawable.top_icon9;
      } else if (grade < 10 * each_grade && grade >= 9 * each_grade) {
         res = R.drawable.top_icon10;
      } else if (grade < 11 * each_grade && grade >= 10 * each_grade) {
         res = R.drawable.top_icon11;
      } else if (grade < 12 * each_grade && grade >= 11 * each_grade) {
         res = R.drawable.top_icon12;
      } else {
         res = R.drawable.top_icon13;
      }

      if (grade == -100) {
         res = R.drawable.official_tag_top;
      }

      cib.setBackgroundResource(res);
   }
}