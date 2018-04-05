package com.ilikezhibo.ggzb.avsdk.chat.blacklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jack.utils.Trace;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.dbhelper.DatabaseHelper;
import com.ilikezhibo.ggzb.dbhelper.entity.DBUserInfo;
import io.rong.imlib.model.UserInfo;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: QiuZuFragmentAdapter
 * @Description: 主页适配
 * @date 2014-7-8 下午4:12:57
 */
public class BlackListAdapter extends BaseAdapter {
   private Context context;

   private ArrayList<String> entities;

   public BlackListAdapter(Context context) {
      this.context = context;
   }

   public void setEntities(ArrayList<String> entities) {
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

   @Override public View getView(final int position, View convertView, ViewGroup parent) {

      //if (convertView == null || convertView.getTag() == null) {
      final ViewHolder holder = new ViewHolder();
      convertView = LayoutInflater.from(context).inflate(R.layout.cell_chat_list_item, null);

      holder.user_portrait = (ImageView) convertView.findViewById(R.id.user_portrait);
      holder.img_user_type = (ImageView) convertView.findViewById(R.id.img_user_type);
      holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);
      holder.img_gender = (ImageView) convertView.findViewById(R.id.img_gender);

      holder.img_level = (ImageView) convertView.findViewById(R.id.img_level);
      holder.txt_tip = (TextView) convertView.findViewById(R.id.txt_tip);
      holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
      holder.txt_coin_count = (TextView) convertView.findViewById(R.id.txt_coin_count);
      holder.unread_num = convertView.findViewById(R.id.unread_num);
      holder.grade_ly=(RelativeLayout)convertView.findViewById(R.id.grade_ly);

      convertView.setTag(holder);
      //} else {
      //   holder = (ViewHolder) convertView.getTag();
      //}

      final String entity = entities.get(position);

      final DisplayImageOptions options =
          new DisplayImageOptions.Builder().showStubImage(R.drawable.face_male)
              .showImageForEmptyUri(R.drawable.face_male)
              .showImageOnFail(R.drawable.face_male)
              .cacheInMemory()
              .cacheOnDisc()
              .build();

      new Thread() {
         @Override public void run() {
            final UserInfo tem_userInfo = AULiveApplication.userInfoProvider.getUserInfo(entity);
            //每次有新数据都跟新
            try {
               if (tem_userInfo != null) {
                  DatabaseHelper.getHelper(context)
                      .addUserInfo(new DBUserInfo(tem_userInfo.getUserId(), tem_userInfo.getName(),
                          tem_userInfo.getPortraitUri().toString()));
               }
            } catch (SQLException e) {

            }
            //final UserInfo tem_userInfo =
            //    entity.getLatestMessage().getUserInfo();
            if (tem_userInfo != null && tem_userInfo.getPortraitUri() != null) {
               if (holder.user_portrait != null) {
                  holder.user_portrait.post(new Runnable() {
                     @Override public void run() {
                        ImageLoader.getInstance()
                            .displayImage(tem_userInfo.getPortraitUri().toString(),
                                holder.user_portrait, options);
                        Trace.d(
                            "entity.getPortraitUrl()" + tem_userInfo.getPortraitUri().toString());
                        holder.img_user_type.setVisibility(View.INVISIBLE);

                        holder.txt_username.setText(tem_userInfo.getName());
                     }
                  });
               }
            }
         }
      }.start();

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

      holder.txt_tip.setText("");
      holder.txt_tip.setVisibility(View.VISIBLE);

      holder.tv_time.setVisibility(View.VISIBLE);

      holder.txt_coin_count.setVisibility(View.INVISIBLE);

      return convertView;
   }

   class ViewHolder {
      // 分享
      ImageView user_portrait;
      ImageView img_user_type;

      TextView txt_username;

      //性别
      ImageView img_gender;
      ImageView img_level;
      TextView txt_tip;

      TextView tv_time;

      TextView txt_coin_count;
      View unread_num;

      RelativeLayout grade_ly;
   }
}
