package com.ilikezhibo.ggzb.userinfo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.DateUtils;
import com.jack.utils.TextUtils;
import com.jack.utils.UrlHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.login.channel.ChannelItem;
import com.ilikezhibo.ggzb.photos.photobrowser.PicBrowseActivity;
import com.ilikezhibo.ggzb.tool.Utils;
import com.ilikezhibo.ggzb.userinfo.myjointedpro.HeJointProjectActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.OtherAdapter;
import com.ilikezhibo.ggzb.views.OtherGridView;
import com.ilikezhibo.ggzb.wxapi.ShareHelper;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;
import com.ilikezhibo.ggzb.xiangmu.myproject.HeProjectActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends BaseFragmentActivity implements OnClickListener {

   public static String PROFILE_UID = "PROFILE_UID";

   private LoginUserEntity userInfoEntity;

   private TextView tv_add_contact;
   private TextView tv_chat;

   private String uid;

   @Override protected void setContentView() {
      setContentView(R.layout.activity_profile);
   }

   @Override protected void initializeViews() {
      Button rl_back = (Button) this.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      TextView tv_title = (TextView) this.findViewById(R.id.title);
      tv_title.setText("简历详情");

      TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
      topRightBtn.setText("分享");
      topRightBtn.setOnClickListener(this);
      topRightBtn.setVisibility(View.VISIBLE);

      tv_add_contact = (TextView) findViewById(R.id.tv_forbid_add_contact);
      tv_add_contact.setOnClickListener(this);

      tv_chat = (TextView) findViewById(R.id.tv_chat);
      tv_chat.setOnClickListener(this);

      TextView tv_he_project = (TextView) this.findViewById(R.id.tv_he_project);
      tv_he_project.setOnClickListener(this);

      TextView tv_he_jointed_pro = (TextView) this.findViewById(R.id.tv_he_jointed_pro);
      tv_he_jointed_pro.setOnClickListener(this);
   }

   @Override protected void initializeData() {

   }

   @Override protected void onResume() {

      super.onResume();
      uid = getIntent().getStringExtra(PROFILE_UID);
      if (uid != null && !uid.equals("")) {
         getMyProducs(uid);
      }
   }

   @Override public void onClick(View v) {
      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }

      switch (v.getId()) {

         case R.id.tv_forbid_add_contact:
            if (userInfoEntity == null) {
               Utils.showMessage("正在请求用户信息，请稍候。。。");
               return;
            }
            if (Utils.isLogin(this)) {
               doSureAddFriend(userInfoEntity.getUid());
            }

            break;
         case R.id.topRightBtn:
            doShare();
            break;
         case R.id.back:
            this.finish();
            break;
         case R.id.tv_chat:// 随便聊聊
            if (Utils.isLogin(this)) {

            }
            break;

         case R.id.tv_he_jointed_pro:
            if (Utils.isLogin(this)) {
               Intent he_joint_pro = new Intent(this, HeJointProjectActivity.class);
               he_joint_pro.putExtra(HeJointProjectActivity.uid_key, uid);
               startActivity(he_joint_pro);
            }

            break;
         case R.id.tv_he_project:
            if (Utils.isLogin(this)) {
               Intent he_pro = new Intent(this, HeProjectActivity.class);
               he_pro.putExtra(HeProjectActivity.uid_key, uid);
               startActivity(he_pro);
            }
            break;
      }
   }

   private void getMyProducs(String uid) {
      RequestInformation request =
          new RequestInformation(UrlHelper.URL_HEAD + "/reg/by_user?uid=" + uid,
              RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UserInfo>() {

         @Override public void onCallback(UserInfo callback) {
            if (callback == null) {

               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }
            if (callback.getStat() == 200) {

               userInfoEntity = callback.getUserinfo();
               // 算生日
               Date mSelectDate = DateUtils.getDate2(userInfoEntity.getBirthday());

               Calendar mCalendar = Calendar.getInstance();
               mCalendar.setTime(mSelectDate);
               int age =
                   TextUtils.getAge(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                       mCalendar.get(Calendar.DAY_OF_MONTH));

               // 个人信息
               ImageView face_img = (ImageView) ProfileActivity.this.findViewById(R.id.iv_head);

               DisplayImageOptions options =
                   new DisplayImageOptions.Builder().showStubImage(R.drawable.face_male)
                       .showImageForEmptyUri(R.drawable.face_male)
                       .showImageOnFail(R.drawable.face_male)
                       .cacheInMemory()
                       .cacheOnDisc()
                       .build();

               ImageLoader.getInstance().displayImage(userInfoEntity.getFace(), face_img, options);

               TextView real_name_tv = (TextView) ProfileActivity.this.findViewById(R.id.tv_name);
               real_name_tv.setText(userInfoEntity.getNickname());

               TextView tv_desc = (TextView) ProfileActivity.this.findViewById(R.id.tv_desc);

               int sex = userInfoEntity.getSex();
               if (sex == 1) {
                  tv_desc.setText(age + "岁 男");
               } else {
                  tv_desc.setText(age + "岁 女");
               }

               // 个人简介
               TextView tv_self_intro =
                   (TextView) ProfileActivity.this.findViewById(R.id.tv_self_intro);
               tv_self_intro.setText(userInfoEntity.intro);
               // ///////////////////////////////////////////////////////////////////////////
               // TextView major_tv = (TextView) ProfileActivity.this
               // .findViewById(R.id.major_tv);
               // skill_tv.setText(loginUserEntity.skill);
               // major_tv.setText(loginUserEntity.job);

               // 标签
               OtherGridView otherGridView =
                   (OtherGridView) ProfileActivity.this.findViewById(R.id.tagview);
               ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();

               String skill = userInfoEntity.skill;
               if (skill != null && !skill.equals("")) {
                  otherGridView.setVisibility(View.VISIBLE);
                  String[] skills = skill.split(",");
                  for (int i = 0; i < skills.length; i++) {
                     if (skills[i] != null && !skills[i].equals(",")) {
                        otherChannelList.add(new ChannelItem(i, skills[i], i, 0));
                     }
                  }

                  OtherAdapter otherAdapter =
                      new OtherAdapter(ProfileActivity.this, otherChannelList);
                  otherGridView.setAdapter(otherAdapter);
               } else {
                  otherGridView.setVisibility(View.INVISIBLE);
               }
               // ///////////////////////////////////////////////////////////////////////////
               // 作品样列
               LinearLayout layout_product_list =
                   (LinearLayout) ProfileActivity.this.findViewById(R.id.layout_product_list);
               ArrayList<XiangMuEntity> entities = userInfoEntity.getList();
               layout_product_list.removeAllViews();

               if (entities.size() > 0) {
                  int index = -1;
                  for (final XiangMuEntity xiangMuEntity : entities) {
                     final View edu_item = LayoutInflater.from(ProfileActivity.this)
                         .inflate(R.layout.profile_product_item, null);
                     index++;

                     View top_seperate_line = edu_item.findViewById(R.id.top_seperate_line);
                     // 当为第一个项时显示
                     if (index == 0) {
                        top_seperate_line.setVisibility(View.GONE);
                     } else {
                        top_seperate_line.setVisibility(View.GONE);
                     }

                     TextView app_name = (TextView) edu_item.findViewById(R.id.app_name_tv);
                     app_name.setText(xiangMuEntity.getTitle());

                     ImageView addphoto_img = (ImageView) edu_item.findViewById(R.id.addphoto_img);
                     ImageLoader.getInstance()
                         .displayImage(xiangMuEntity.getPic(), addphoto_img,
                             AULiveApplication.getGlobalImgOptions());
                     addphoto_img.setOnClickListener(new OnClickListener() {

                        @Override public void onClick(View arg0) {
                           String url = xiangMuEntity.getPic();
                           if (url == null || url.equals("")) {
                              return;
                           }
                           String[] urls = { url };
                           Intent intent =
                               new Intent(ProfileActivity.this, PicBrowseActivity.class);
                           intent.putExtra(PicBrowseActivity.INTENT_BROWSE_POS_KEY, 0);
                           intent.putExtra(PicBrowseActivity.INTENT_BROWSE_LST_KEY, urls);
                           startActivity(intent);
                        }
                     });

                     TextView app_url_tv = (TextView) edu_item.findViewById(R.id.app_url_tv);
                     app_url_tv.setText(xiangMuEntity.getUrl());

                     app_url_tv.setOnClickListener(new OnClickListener() {

                        @Override public void onClick(View arg0) {
                           Intent intent = new Intent(ProfileActivity.this, WebViewActivity.class);
                           if (xiangMuEntity.getUrl() == null || xiangMuEntity.getUrl()
                               .equals("")) {
                              return;
                           }

                           intent.putExtra(WebViewActivity.input_url, xiangMuEntity.getUrl());
                           intent.putExtra(WebViewActivity.back_home_key, false);
                           intent.putExtra(WebViewActivity.actity_name, "网页详情");
                           startActivity(intent);
                        }
                     });

                     TextView content_tv = (TextView) edu_item.findViewById(R.id.content_tv);
                     content_tv.setText(xiangMuEntity.getMemo());

                     layout_product_list.addView(edu_item);
                  }
               } else {
                  // 当没有时，加一个空的
                  // final View edu_item = LayoutInflater.from(
                  // ProfileActivity.this).inflate(
                  // R.layout.userinfo_myproduct_item, null);
                  // layout_product_list.addView(edu_item);
               }

               // ///////////////////////////////////////////////////////////////////////////

               // 公司
               TextView company_tv = (TextView) ProfileActivity.this.findViewById(R.id.company_tv);
               TextView position_tv =
                   (TextView) ProfileActivity.this.findViewById(R.id.position_tv);
               TextView company_time_tv =
                   (TextView) ProfileActivity.this.findViewById(R.id.company_time_tv);
               TextView content_tv =
                   (TextView) ProfileActivity.this.findViewById(R.id.work_desc_tv);

               company_tv.setText(userInfoEntity.company);
               position_tv.setText(userInfoEntity.post);
               company_time_tv.setText(
                   userInfoEntity.begin_work_time + "至" + userInfoEntity.end_work_time);
               content_tv.setText(userInfoEntity.job_desc);

               // 教育
               TextView school_tv = (TextView) ProfileActivity.this.findViewById(R.id.school_tv);
               TextView school_major_tv =
                   (TextView) ProfileActivity.this.findViewById(R.id.school_major_tv);
               TextView degree_tv = (TextView) ProfileActivity.this.findViewById(R.id.degree_tv);
               TextView school_time_tv =
                   (TextView) ProfileActivity.this.findViewById(R.id.school_time_tv);

               school_tv.setText(userInfoEntity.school);
               school_major_tv.setText(userInfoEntity.specialty);
               degree_tv.setText(userInfoEntity.degree);
               school_time_tv.setText(
                   userInfoEntity.begin_school_time + "至" + userInfoEntity.end_school_time);

               // 是否为好友处理
               if (userInfoEntity.getFriend_status() == 2) {
                  // 为朋友不显示
                  tv_add_contact.setVisibility(View.GONE);
                  tv_add_contact.setEnabled(true);
               } else if (userInfoEntity.getFriend_status() == 0) {
                  tv_add_contact.setVisibility(View.VISIBLE);
                  tv_add_contact.setText("好友申请中...");
                  tv_add_contact.setEnabled(false);
               } else {
                  // 什么也不做
                  tv_add_contact.setEnabled(true);
               }

               // 不跟自己聊
               if (AULiveApplication.getUserInfo() != null
                   && AULiveApplication.getUserInfo().getUid() != null
                   && AULiveApplication.getUserInfo().getUid().equals(userInfoEntity.getUid())) {
                  tv_chat.setVisibility(View.GONE);
                  tv_add_contact.setVisibility(View.GONE);
               }
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(UserInfo.class));
      request.execute();
   }

   private void doSureAddFriend(final String uid) {

      CustomDialog userBlackDialog = new CustomDialog(this, new CustomDialogListener() {
         @Override public void onDialogClosed(int closeType) {
            switch (closeType) {
               case CustomDialogListener.BUTTON_POSITIVE:
                  doAddFriend(uid);
                  break;
            }
         }
      });

      userBlackDialog.setCustomMessage("确定要加好友吗?");
      userBlackDialog.setCancelable(true);
      userBlackDialog.setType(CustomDialog.DOUBLE_BTN);

      if (null != userBlackDialog) {
         userBlackDialog.show();
      }
   }

   private void doAddFriend(final String uid) {

      // startProgressDialog();
      RequestInformation request =
          new RequestInformation(UrlHelper.URL_HEAD + "/friend/add?user=" + uid,
              RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("请求添加成功");
               getMyProducs(uid);
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {

            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private void doShare() {
      if (userInfoEntity == null) {
         Utils.showMessage("数据加载中...");
         return;
      }
      ShareHelper shareDialog = null;
      String target_url = null;
      try {
         target_url = "http://wx.qxj.me/p/programmer.html?uid=" + uid;
      } catch (Exception e1) {
         e1.printStackTrace();
      }
      if (shareDialog == null) {
         shareDialog = new ShareHelper(this);
      }

      shareDialog.setShareUrl(target_url);
      shareDialog.setShareTitle(Utils.trans(R.string.app_name));
      shareDialog.setShareContent(userInfoEntity.getNickname(), userInfoEntity.getFace());

      if (!shareDialog.isShowing()) {
         shareDialog.show();
      }
   }
}
