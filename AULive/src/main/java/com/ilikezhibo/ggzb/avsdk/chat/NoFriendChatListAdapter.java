package com.ilikezhibo.ggzb.avsdk.chat;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.DateUtils;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.dbhelper.DatabaseHelper;
import com.ilikezhibo.ggzb.dbhelper.entity.DBUserInfo;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfoList;
import com.ilikezhibo.ggzb.views.BadgeView;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

/**
 * @author big
 * @ClassName: QiuZuFragmentAdapter
 * @Description: 主页适配
 * @date 2014-7-8 下午4:12:57
 */
public class NoFriendChatListAdapter extends BaseAdapter {
   private Context context;

   private ArrayList<Conversation> entities;

   public NoFriendChatListAdapter(Context context) {
      this.context = context;
   }

   public void setEntities(ArrayList<Conversation> entities) {
      this.entities = entities;
   }

   @Override public int getCount() {
      if (entities != null && entities.size() > 0) {
         return entities.size();
      }

      return 0;
   }

   @Override public Object getItem(int position) {
      return entities.get(position);
   }

   @Override public long getItemId(int position) {

      return 0;
   }

   ViewHolder holder;
   DisplayImageOptions options =
       new DisplayImageOptions.Builder().showStubImage(R.drawable.face_male)
           .showImageForEmptyUri(R.drawable.face_male)
           .showImageOnFail(R.drawable.face_male)
           .cacheInMemory()
           .cacheOnDisc()
           .build();

   @Override public View getView(final int position, View convertView, ViewGroup parent) {

      if (convertView == null || convertView.getTag() == null) {
         holder = new ViewHolder();
         convertView = LayoutInflater.from(context).inflate(R.layout.cell_chat_list_item, null);

         holder.user_portrait = (ImageView) convertView.findViewById(R.id.user_portrait);
         holder.img_user_type = (ImageView) convertView.findViewById(R.id.img_user_type);
         holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);
         holder.img_gender = (ImageView) convertView.findViewById(R.id.img_gender);
         holder.tv_grade = (TextView) convertView.findViewById(R.id.grade_tv);
         holder.grade_ly = (RelativeLayout) convertView.findViewById(R.id.grade_ly);
         holder.txt_tip = (TextView) convertView.findViewById(R.id.txt_tip);
         holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
         holder.txt_coin_count = (TextView) convertView.findViewById(R.id.txt_coin_count);
         holder.unread_num = convertView.findViewById(R.id.unread_num);
         holder.badgeView = new BadgeView(context);
         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }

      final Conversation entity = entities.get(position);

      getUserinfo(entity.getTargetId(), holder.user_portrait, holder.txt_username);
      holder.img_user_type.setVisibility(View.INVISIBLE);

      //if (entity.getSex() == 0) {
      //   holder.img_gender.setImageResource(R.drawable.global_male);
      //} else {
      //   holder.img_gender.setImageResource(R.drawable.global_female);
      //}
      holder.img_gender.setVisibility(View.INVISIBLE);

      //等级
      //String grade = entity.getGrade();
      //String imgname = "rank_" + grade;
      //int imgid =
      //    context.getResources().getIdentifier(imgname, "drawable", context.getPackageName());
      //holder.img_level.setImageResource(imgid);
      holder.grade_ly.setVisibility(View.INVISIBLE);

      if (entity.getLatestMessage() instanceof TextMessage) {
         TextMessage lastMsg = (TextMessage) entity.getLatestMessage();
         holder.txt_tip.setText(lastMsg.getContent());
      } else {
         holder.txt_tip.setText("");
      }
      holder.txt_tip.setVisibility(View.VISIBLE);

      Date send_tiem = new Date(entity.getReceivedTime());
      holder.tv_time.setText(DateUtils.getTimestampString(send_tiem));
      holder.tv_time.setVisibility(View.VISIBLE);

      holder.txt_coin_count.setVisibility(View.INVISIBLE);


      holder.badgeView.setTargetView(holder.unread_num);
      holder.badgeView.setBadgeCount(entity.getUnreadMessageCount());
      //if (entity.getUnreadMessageCount() <= 0) {
      //   badgeView.setVisibility(View.INVISIBLE);
      //}
      return convertView;
   }

   class ViewHolder {
      // 分享
      ImageView user_portrait;
      ImageView img_user_type;

      TextView txt_username;

      //性别
      ImageView img_gender;
      public TextView tv_grade;
      public RelativeLayout grade_ly;
      TextView txt_tip;

      TextView tv_time;

      TextView txt_coin_count;
      View unread_num;
      BadgeView badgeView;
   }

   private void showPromptDialog(final XiangMuEntity qiuZuEntity) {
      final CustomDialog customDialog = new CustomDialog(context, new CustomDialogListener() {

         @Override public void onDialogClosed(int closeType) {
            switch (closeType) {
               case CustomDialogListener.BUTTON_POSITIVE:
            }
         }
      });

      customDialog.setCustomMessage("确认要删除吗?");
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      customDialog.show();
   }

   //异步获取用户信息
   private void getUserinfo(String uid, final ImageView face, final TextView nickname) {

      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(
             UrlHelper.user_home_Url + "/userinfo" + "?uid=" + uid + "&pf=android");
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<UserInfoList>() {

         @Override public void onCallback(UserInfoList callback) {
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {

               ArrayList<LoginUserEntity> userinfo = callback.getUserinfo();

               io.rong.imlib.model.UserInfo tem_userInfo =
                   new io.rong.imlib.model.UserInfo(userinfo.get(0).getUid(),
                       userinfo.get(0).getNickname(), Uri.parse(userinfo.get(0).getFace()));

               //每次有新数据都跟新
               try {
                  if (tem_userInfo != null) {
                     DatabaseHelper.getHelper(context)
                         .addUserInfo(
                             new DBUserInfo(tem_userInfo.getUserId(), tem_userInfo.getName(),
                                 tem_userInfo.getPortraitUri().toString()));
                  }
               } catch (SQLException e) {

               }

               if (tem_userInfo != null && tem_userInfo.getPortraitUri() != null) {

                  ImageLoader.getInstance()
                      .displayImage(tem_userInfo.getPortraitUri().toString(), face, options);
                  Trace.d("entity.getPortraitUrl()" + tem_userInfo.getPortraitUri().toString());

                  nickname.setText(tem_userInfo.getName());
               }
            } else {

            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(UserInfoList.class));
      request.execute();
   }
}
