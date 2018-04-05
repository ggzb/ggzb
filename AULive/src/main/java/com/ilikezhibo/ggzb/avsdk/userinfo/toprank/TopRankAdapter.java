package com.ilikezhibo.ggzb.avsdk.userinfo.toprank;

import android.content.Context;
import android.text.Html;
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
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.ChatMsgListAdapter;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;

import java.util.ArrayList;

/**
 * @author big
 * @ClassName: TopRankAdapter
 * @Description: 主页适配
 * @date 2014-7-8 下午4:12:57
 */
public class TopRankAdapter extends BaseAdapter {
   private Context context;

   private ViewHolder holder;

   private ArrayList<TopRankEntity> entities;

   public TopRankAdapter(Context context) {
      this.context = context;
   }

   public void setEntities(ArrayList<TopRankEntity> entities) {
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
         convertView = LayoutInflater.from(context).inflate(R.layout.cell_rec_user_list_item, null);
         holder.user_portrait = (ImageView) convertView.findViewById(R.id.user_portrait);
         holder.img_user_type = (ImageView) convertView.findViewById(R.id.img_user_type);
         holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);
         holder.img_gender = (ImageView) convertView.findViewById(R.id.img_gender);

         holder.tv_grade = (TextView) convertView.findViewById(R.id.grade_tv);
         holder.grade_ly = (RelativeLayout) convertView.findViewById(R.id.grade_ly);
         holder.txt_tip = (TextView) convertView.findViewById(R.id.txt_tip);
         holder.img_follow = (ImageView) convertView.findViewById(R.id.img_follow);
         holder.txt_coin_count = (TextView) convertView.findViewById(R.id.txt_coin_count);

         holder.rank_num = (TextView) convertView.findViewById(R.id.rank_num);
         holder.postion = position;
         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }
      if (position == 0 || position == 1 || position == 2) {
         if (position == 0) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.gift_contributor_header_item, null);
         } else if (position == 1) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.gift_contributor_header_item2, null);
         } else if (position == 2) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.gift_contributor_header_item3, null);
         } else {
         }

         holder.user_portrait = (ImageView) convertView.findViewById(R.id.user_portrait);
         holder.img_user_type = (ImageView) convertView.findViewById(R.id.img_user_type);
         holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);
         holder.img_gender = (ImageView) convertView.findViewById(R.id.img_gender);

         holder.tv_grade = (TextView) convertView.findViewById(R.id.grade_tv);
         holder.grade_ly = (RelativeLayout) convertView.findViewById(R.id.grade_ly);
         holder.txt_tip = (TextView) convertView.findViewById(R.id.txt_tip);
         holder.img_follow = (ImageView) convertView.findViewById(R.id.img_follow);
         holder.txt_coin_count = (TextView) convertView.findViewById(R.id.txt_coin_count);

         holder.rank_num = (TextView) convertView.findViewById(R.id.rank_num);
         holder.postion = position;
      }

      final TopRankEntity entity = entities.get(position);

      //排名
      int no_num = position + 1;
      holder.rank_num.setText("NO." + no_num + "");
      holder.rank_num.setVisibility(View.VISIBLE);

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
      ChatMsgListAdapter.setGradeIcon(grade, holder.tv_grade, holder.grade_ly);

      if (holder.txt_tip != null) {
         holder.txt_tip.setText(entity.getSignature());
         holder.txt_tip.setVisibility(View.INVISIBLE);
      }
      //holder.location_tv.setText(entity.getAddr());

      //关注功能用不上
      //boolean has_follow = false;
      //if (MainActivity.atten_uids.contains(entity.getUid())) {
      //   has_follow = true;
      //}
      //final boolean tem_b = has_follow;
      ////按是否关注而改变
      //if (tem_b) {
      //   holder.img_follow.setImageResource(R.drawable.me_following);
      //} else {
      //   holder.img_follow.setImageResource(R.drawable.me_follow);
      //}
      //
      //holder.img_follow.setOnClickListener(new View.OnClickListener() {
      //   @Override public void onClick(View view) {
      //      if (tem_b) {
      //         doDelAttend(entity.getUid());
      //      } else {
      //         doAttend(entity.getUid());
      //      }
      //   }
      //});

      if (holder.img_follow != null) {
         holder.img_follow.setVisibility(View.GONE);
      }

      holder.txt_coin_count.setText(Html.fromHtml("<font color='black'>贡献</font>"
               + "<font color='red'>"
               + entity.getConsume_diamond()
               + "</font>"
               + "<font color='black'>"
               + Utils.trans(R.string.app_money)
               +"</font>")
      );
      holder.txt_coin_count.setVisibility(View.VISIBLE);

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
      ImageView img_follow;

      TextView txt_coin_count;

      TextView rank_num;

      int postion;
   }

   private void doAttend(final String uid) {

      RequestInformation request = new RequestInformation(UrlHelper.ROOM_ADD_ATTEN + "?u=" + uid,
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {

               if (MainActivity.atten_uids.contains(uid)) {
               } else {
                  MainActivity.atten_uids.add(uid);
               }

               TopRankAdapter.this.notifyDataSetChanged();
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private void doDelAttend(final String uid) {

      RequestInformation request = new RequestInformation(UrlHelper.ROOM_DEL_ATTEN + "?u=" + uid,
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {

               if (MainActivity.atten_uids.contains(uid)) {
                  MainActivity.atten_uids.remove(uid);
               } else {

               }

               TopRankAdapter.this.notifyDataSetChanged();
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
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
}
