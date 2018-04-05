package com.ilikezhibo.ggzb.avsdk.gift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.ChatMsgListAdapter;
import com.ilikezhibo.ggzb.avsdk.userinfo.UserInfoDialogEntity;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: QiuZuFragmentAdapter
 * @Description: 主页适配
 * @date 2014-7-8 下午4:12:57
 */
public class RedPacketDialgoListAdapter extends BaseAdapter {
   private Context context;

   private ViewHolder holder;

   private ArrayList<UserInfoDialogEntity> entities;

   public RedPacketDialgoListAdapter(Context context) {
      this.context = context;
   }

   public void setEntities(ArrayList<UserInfoDialogEntity> entities) {
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

      if (convertView == null || convertView.getTag() == null) {
         holder = new ViewHolder();
         convertView =
             LayoutInflater.from(context).inflate(R.layout.cell_rec_redpacket_list_item, null);

         holder.user_portrait = (ImageView) convertView.findViewById(R.id.user_portrait);
         holder.img_user_type = (ImageView) convertView.findViewById(R.id.img_user_type);
         holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);
         holder.img_gender = (ImageView) convertView.findViewById(R.id.img_gender);
         holder.tv_grade = (TextView) convertView.findViewById(R.id.grade_tv);
         holder.grade_ly = (RelativeLayout) convertView.findViewById(R.id.grade_ly);
         holder.txt_tip = (TextView) convertView.findViewById(R.id.txt_tip);

         holder.txt_coin_count = (TextView) convertView.findViewById(R.id.txt_coin_count);
         //抢到的红包数
         holder.txt_num_grab = (TextView) convertView.findViewById(R.id.txt_num_grab);
         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }

      final UserInfoDialogEntity entity = entities.get(position);

      DisplayImageOptions options =
          new DisplayImageOptions.Builder().showStubImage(R.drawable.face_male)
              .showImageForEmptyUri(R.drawable.face_male)
              .showImageOnFail(R.drawable.face_male)
              .cacheInMemory()
              .cacheOnDisc()
              .build();

      ImageLoader.getInstance().displayImage(entity.getFace(), holder.user_portrait, options);

      holder.txt_username.setText(entity.getNickname());
      if (entity.getSex() == 1) {
         holder.img_gender.setImageResource(R.drawable.global_male);
      } else {
         holder.img_gender.setImageResource(R.drawable.global_female);
      }

      //等级

      String grade = entity.getGrade();
      ChatMsgListAdapter.setGradeIcon(grade,holder.tv_grade,holder.grade_ly);

      holder.txt_tip.setText(entity.getSignature());
      //holder.location_tv.setText(entity.getAddr());

      holder.txt_coin_count.setVisibility(View.INVISIBLE);
      holder.txt_num_grab.setText(entity.getGrab()+"");

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

      TextView txt_coin_count;

      TextView txt_num_grab;
   }
}
