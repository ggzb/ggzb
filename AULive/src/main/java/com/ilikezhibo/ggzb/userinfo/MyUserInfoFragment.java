package com.ilikezhibo.ggzb.userinfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.views.UpLoadFileWebViewActivity;
import com.ilikezhibo.ggzb.views.UserInfoWebViewActivity;
import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.ActivityStackManager;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.ChatMsgListAdapter;
import com.ilikezhibo.ggzb.avsdk.activity.MedalLayoutHelper;
import com.ilikezhibo.ggzb.avsdk.chat.PrivateChatListActivity;
import com.ilikezhibo.ggzb.avsdk.chat.event.UpDateUnReadEvent;
import com.ilikezhibo.ggzb.avsdk.userinfo.AttenListActivity;
import com.ilikezhibo.ggzb.avsdk.userinfo.FansListActivity;
import com.ilikezhibo.ggzb.avsdk.userinfo.LivesListActivity;
import com.ilikezhibo.ggzb.avsdk.userinfo.homepage.HomePageActivity;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.TopRankActivity;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import com.ilikezhibo.ggzb.home.listener.NavigationListener;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.photos.photobrowser.PicBrowseActivity;
import com.ilikezhibo.ggzb.register.RegisterActivity;
import com.ilikezhibo.ggzb.userinfo.buydiamond.BuyDiamondActivity;
import com.ilikezhibo.ggzb.userinfo.setting.SettingActivity;
import com.ilikezhibo.ggzb.views.BadgeView;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import java.util.ArrayList;
import pulltozoomview.PullToZoomScrollViewEx;

/**
 * @author big
 * @ClassName: HomePageFragment
 * @Description: 我的资料
 * @date 2015-3-12 下午9:49:56
 */
@SuppressLint("ValidFragment") public class MyUserInfoFragment extends BaseFragment
    implements TitleListener, OnClickListener {
   private LinearLayout layout;
   private int[] listItems;

   private View root_view;
   private ImageView my_image;
   private RelativeLayout imageLayout;

   private TextView hasVistorTv;
   private TextView hasVipNewTv;
   private TextView hasCreditTv;
   private TextView hasPublishVideoTv;

   private NavigationListener listener;
   private ImageButton ib_chat_enter;
   private ImageButton ib_modify;

   private ImageView blur_bg_iv;
   private LinearLayout ib_modify_contain;
   private TextView mGuanzhuCountTv;
   private TextView mFansCountTv;
   public MyUserInfoFragment() {
   }

   public MyUserInfoFragment(NavigationListener listener) {
      this.listener = listener;
   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      ActivityStackManager.getInstance().pushActivity(getActivity());
      root_view = inflater.inflate(R.layout.jiesihuo_my_info_layout, null);

      PullToZoomScrollViewEx scrollView =
          (PullToZoomScrollViewEx) root_view.findViewById(R.id.scroll_view);
      View headView = LayoutInflater.from(MyUserInfoFragment.this.getActivity())
          .inflate(R.layout.jiesihuo_my_info_layout_top, null, false);
      mGuanzhuCountTv = (TextView)headView.findViewById(R.id.guanzhu_value_tv);
      mFansCountTv = (TextView)headView.findViewById(R.id.fans_value_tv);
      View zoomView = LayoutInflater.from(MyUserInfoFragment.this.getActivity())
          .inflate(R.layout.jiesihuo_my_info_layout_zoom_view, null, false);
      View contentView = LayoutInflater.from(MyUserInfoFragment.this.getActivity())
          .inflate(R.layout.jiesihuo_my_info_layout_content, null, false);
      scrollView.setHeaderView(headView);
      scrollView.setZoomView(zoomView);
      scrollView.setScrollContentView(contentView);
      DisplayMetrics localDisplayMetrics = new DisplayMetrics();
      MyUserInfoFragment.this.getActivity()
          .getWindowManager()
          .getDefaultDisplay()
          .getMetrics(localDisplayMetrics);
      int mScreenHeight = localDisplayMetrics.heightPixels;
      int mScreenWidth = localDisplayMetrics.widthPixels;
      LinearLayout.LayoutParams localObject =
          new LinearLayout.LayoutParams(mScreenWidth, (int) (9.7F * (mScreenWidth / 16.0F)));
      scrollView.setHeaderLayoutParams(localObject);

      Button rl_back = (Button) root_view.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.INVISIBLE);

      TextView tv_title = (TextView) root_view.findViewById(R.id.title);
      tv_title.setText("我的");

      TextView topRightBtn = (TextView) root_view.findViewById(R.id.topRightBtn);
      topRightBtn.setText("注册");
      topRightBtn.setOnClickListener(this);
      topRightBtn.setVisibility(View.INVISIBLE);

      try {
         initializeData();
      } catch (InflateException e) {
         e.printStackTrace();
      }
      EventBus.getDefault().register(this);

      return root_view;
   }

   protected void initializeData() {
      imageLayout = (RelativeLayout) root_view.findViewById(R.id.imageLayout);
      imageLayout.setOnClickListener(this);
      my_image = (ImageView) root_view.findViewById(R.id.my_image);

//      listItems = new int[] {
//          R.id.in_out_record, R.id.suggest, R.id.aboat, R.id.miaobi_gongxianbang,
//              R.id.miaoqu_my_live,R.id.invite_friends, R.id.setting
//      };
      listItems = new int[] {
              R.id.in_out_record, R.id.aboat, R.id.my_vip, R.id.setting
      };

      int[] images = new int[] {
          R.drawable.userinfo_mymoney, R.drawable.userinfo_aboat_us, R.drawable.userinfo_suggest,
          R.drawable.userinfo_gongxianbang,R.drawable.userinfo_mylive,R.drawable.userinfo_invite_friends, R.drawable.userinfo_setting
      };

      setListItemValue(listItems[0], images[0], "收益");
//      setListItemValue(listItems[1], images[1], "等级");
      setListItemValue(listItems[1], images[2], "钻石");
      setListItemValue(listItems[2], images[2], "VIP会员");
      //setListItemValue(listItems[3], images[3], "实名认证");
//      setListItemValue(listItems[3], images[3], "魅力贡献榜");
//      setListItemValue(listItems[4], images[4], "我的直播");
//      setListItemValue(listItems[5], images[5], "邀请好友");
      setListItemValue(listItems[3], images[6], "设置");
      // updateChatMsgTotalReceiver = new UpdateChatMsgTotalReceiver();
      // updateChatMsgTotalReceiver.setListener(this);
      // IntentFilter intentFilter3 = new IntentFilter();
      // intentFilter3
      // .addAction(UpdateChatMsgTotalReceiver.UPDATE_CHAT_MSG_TOTAL);
      // intentFilter3.addAction(UpdateChatMsgTotalReceiver.SHOW_NEW_VISTOR);
      // getActivity().registerReceiver(updateChatMsgTotalReceiver,
      // intentFilter3);

      ib_modify = (ImageButton) root_view.findViewById(R.id.ib_modify);
      ib_modify_contain = (LinearLayout) root_view.findViewById(R.id.ib_modify_contain);
      ib_modify_contain.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View view) {
            doModify();
         }
      });
      ib_modify.setOnClickListener(new OnClickListener() {
         @Override public void onClick(View v) {
            doModify();
         }
      });
      ib_chat_enter = (ImageButton) root_view.findViewById(R.id.ib_chat_enter);
      ib_chat_enter.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            startActivity(
                new Intent(MyUserInfoFragment.this.getActivity(), PrivateChatListActivity.class));
         }
      });
      badgeView = new BadgeView(this.getContext());
      badgeView.setTargetView(ib_chat_enter);
      //ib_chat_enter.setVisibility(View.INVISIBLE);

      blur_bg_iv = (ImageView) root_view.findViewById(R.id.iv_zoom);
      blur_bg_iv.setBackgroundResource(R.color.global_main_bg);
      //模糊背景
      //ImageLoader.getInstance()
      //    .displayImage(AULiveApplication.getUserInfo().getFace(), blur_bg_iv,
      //        new ImageLoadingListener() {
      //           @Override public void onLoadingStarted(String s, View view) {
      //
      //           }
      //
      //           @Override public void onLoadingFailed(String s, View view, FailReason failReason) {
      //
      //           }
      //
      //           @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
      //              blur_bg_iv.setImageBitmap(FastBlurUtil.toBlur(bitmap, 10));
      //           }
      //
      //           @Override public void onLoadingCancelled(String s, View view) {
      //
      //           }
      //        });
   }

   private void setListItemValue(int id, int name1, String value1) {
      layout = (LinearLayout) root_view.findViewById(id);
      ImageView name = (ImageView) layout.findViewById(R.id.itemHeadImg);
      name.setBackgroundResource(name1);
      TextView value = (TextView) layout.findViewById(R.id.item_value);
      value.setText(value1);
      layout.setOnClickListener(MyUserInfoFragment.this);

      // 去最后那根线
      LinearLayout lineLayout = (LinearLayout) layout.findViewById(R.id.lineLayout);

      if (id == R.id.setting) {
         lineLayout.setVisibility(View.GONE);
      } else if (id == R.id.aboat) {
         lineLayout.setVisibility(View.GONE);
      }else {
         lineLayout.setVisibility(View.VISIBLE);
      }

      if (id == R.id.setting) {
         TextView hasNewAppTv = (TextView) layout.findViewById(R.id.hasNewTv);
      }
   }

   @Override public void onBack() {

   }

   @Override public void onTopRightEvent() {

   }

   public void onShowVisotr() {
      if (hasVistorTv != null) {
         hasVistorTv.setVisibility(View.VISIBLE);
      } else if (hasVistorTv == null) {
         Trace.d("hasVistorTv is null");
      }
   }

   public void onShowVipNew() {
      if (hasVipNewTv != null) {
         hasVipNewTv.setVisibility(View.VISIBLE);
      } else if (hasVipNewTv == null) {
         Trace.d("hasVipNewTv is null");
      }
   }

   public void onShowCreditNew() {
      if (hasCreditTv != null) {
         hasCreditTv.setVisibility(View.VISIBLE);
      } else if (hasCreditTv == null) {
         Trace.d("hasVipNewTv is null");
      }
   }

   @Override public void onClick(View v) {
      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }
      String uid = AULiveApplication.getUserInfo().getUid();
      String nickname = AULiveApplication.getUserInfo().getNickname();
      if (AULiveApplication.getUserInfo() == null || uid == null || uid.equals("") || uid.equals(
          "0")) {
         Intent login_intent =
             new Intent(MyUserInfoFragment.this.getActivity(), LoginActivity.class);
         login_intent.putExtra(LoginActivity.back_home_key, true);
         startActivity(login_intent);
         return;
      }

      switch (v.getId()) {

         case R.id.setting:
            startActivity(new Intent(this.getActivity(), SettingActivity.class));
            break;

         case R.id.imageLayout:
            Trace.d(AULiveApplication.getUserInfo().getUid());
            //doModify();

            break;

         case R.id.suggest:
            //Intent suggest_intent =
            //    new Intent(MyUserInfoFragment.this.getActivity(), SuggestActivity.class);
            //startActivity(suggest_intent);
            Intent intent_level =
                new Intent(MyUserInfoFragment.this.getActivity(), UserInfoWebViewActivity.class);
            intent_level.putExtra(WebViewActivity.input_url,
                UrlHelper.SERVER_URL + "profile/mygrade"+"?uid="+uid);
            intent_level.putExtra(WebViewActivity.back_home_key, false);
            intent_level.putExtra(WebViewActivity.actity_name, "我的等级");
            MyUserInfoFragment.this.startActivity(intent_level);
            break;
         case R.id.in_out_record:

            Intent intent =
                new Intent(MyUserInfoFragment.this.getActivity(), UserInfoWebViewActivity.class);

            intent.putExtra(WebViewActivity.input_url, UrlHelper.SERVER_URL + "profile/earnings"+"?uid="+uid);
            intent.putExtra(WebViewActivity.back_home_key, false);
            intent.putExtra(WebViewActivity.actity_name, "我的收益(提现)");
            MyUserInfoFragment.this.startActivity(intent);

//            String isBindPublic = SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.BIND_WX_PUBLIC, "1");
//
//            if ("0".equals(isBindPublic)) {
//               startActivity(new Intent(this.getActivity(), ExchangeActivity.class));
//            } else {
//               startActivity(new Intent(this.getActivity(), BindPublicActivity.class));
//            }

//            startActivity(new Intent(this.getActivity(), ExchangeActivity.class));

            break;
         case R.id.my_vip:

            Intent intent_vip =
                    new Intent(MyUserInfoFragment.this.getActivity(), UserInfoWebViewActivity.class);

            intent_vip.putExtra(WebViewActivity.input_url, UrlHelper.SERVER_URL + "profile/vip");
            intent_vip.putExtra(WebViewActivity.back_home_key, false);
            intent_vip.putExtra(WebViewActivity.actity_name, "VIP会员");
            MyUserInfoFragment.this.startActivity(intent_vip);

            break;
         case R.id.aboat:
            //Intent aboat_intent =
            //    new Intent(MyUserInfoFragment.this.getActivity(), AboatActivity.class);
            //startActivity(aboat_intent);
            //我的钻石
            Intent moeny_intent =
                new Intent(MyUserInfoFragment.this.getActivity(), BuyDiamondActivity.class);
            startActivity(moeny_intent);
            break;
//            Intent intent4 =
//                    new Intent(MyUserInfoFragment.this.getActivity(), UserInfoWebViewActivity.class);
//            intent4.putExtra(WebViewActivity.input_url,
//                    UrlHelper.SERVER_URL + "profile/h5charge");
//            intent4.putExtra(WebViewActivity.back_home_key, false);
//            intent4.putExtra(WebViewActivity.actity_name, "充值");
//            MyUserInfoFragment.this.startActivity(intent4);
//            break;
         case R.id.certification:
            //Intent aboat_intent =
            //    new Intent(MyUserInfoFragment.this.getActivity(), AboatActivity.class);
            //startActivity(aboat_intent);
            //我的钻石
            Intent certi_intent =
                new Intent(MyUserInfoFragment.this.getActivity(), UpLoadFileWebViewActivity.class);

            certi_intent.putExtra(WebViewActivity.input_url,
                UrlHelper.SERVER_URL + "other/certify"+"?uid="+uid);
            certi_intent.putExtra(WebViewActivity.back_home_key, false);
            certi_intent.putExtra(WebViewActivity.actity_name, "实名认证");
            MyUserInfoFragment.this.startActivity(certi_intent);
            break;
         case  R.id.miaobi_gongxianbang:
            //贡献榜
            Intent top_rank = new Intent(MyUserInfoFragment.this.getActivity(),
                    TopRankActivity.class).putExtra(TopRankActivity.MemberInfo_key,
                    userinfo.getUid());
            startActivity(top_rank);

            break;
         case R.id.miaoqu_my_live:
            Intent top_rank2 = new Intent(MyUserInfoFragment.this.getActivity(),
                    LivesListActivity.class);
            startActivity(top_rank2);
         break;
         case R.id.invite_friends:
            Intent intent3 =
                    new Intent(MyUserInfoFragment.this.getActivity(), UserInfoWebViewActivity.class);
            intent3.putExtra(WebViewActivity.input_url,
                    UrlHelper.SERVER_URL + "other/referfriends"+"?uid="+uid);
            intent3.putExtra(WebViewActivity.back_home_key, false);
            intent3.putExtra(WebViewActivity.actity_name, "邀请朋友");
            MyUserInfoFragment.this.startActivity(intent3);
            break;
      }
   }

   private void doModify() {
      String uid = AULiveApplication.getUserInfo().getUid();
      if (AULiveApplication.getUserInfo() == null || uid == null || uid.equals("") || uid.equals(
          "0")) {
         Intent login_intent =
             new Intent(MyUserInfoFragment.this.getActivity(), LoginActivity.class);
         login_intent.putExtra(LoginActivity.back_home_key, true);
         startActivity(login_intent);
      } else {
         // 作个人资料更改
         Intent login_intent =
             new Intent(MyUserInfoFragment.this.getActivity(), RegisterActivity.class);
         login_intent.putExtra(RegisterActivity.INFO_MODIFY_KEY, true);
         login_intent.putExtra(LoginActivity.back_home_key, true);
         startActivity(login_intent);
      }
   }

   @Override public void onResume() {
      super.onResume();
      initUserData();
      getHomeInfo();

      //启动时，与关闭ConversationList时更新
      onEvent(null);
   }

   @Override public void onDestroy() {
      super.onDestroy();
      EventBus.getDefault().unregister(this);
   }

   BadgeView badgeView = null;

   public void onEvent(UpDateUnReadEvent token_event) {
      //更新私聊未读数

      if (RongIM.getInstance().getRongIMClient() == null) {
         return;
      }
      int count = RongIM.getInstance()
          .getRongIMClient()
          .getUnreadCount(Conversation.ConversationType.PRIVATE);
      badgeView.setBadgeCount(count);
      if (count < 1) {
         badgeView.setVisibility(View.INVISIBLE);
      } else {
         badgeView.setVisibility(View.VISIBLE);
      }

      Trace.d("MyUserInfoFragment onEvent(UpDateUnReadEvent count:" + count);
   }

   private void initUserData() {

   }

   LoginUserEntity userinfo;

   private void getHomeInfo() {
      RequestInformation request = null;
      String uid = AULiveApplication.getUserInfo().getUid();
      if (uid == null || uid.equals("")) {
         //Utils.showMessage("uid为空");
         return;
      }
      try {
         StringBuilder sb = new StringBuilder(UrlHelper.user_home_Url + "?u=" + uid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<UserInfo>() {

         private TextView mQuantity_3;
         private LinearLayout mQuantity_diamond;
         private TextView mQuantity_2;
         private LinearLayout mQuantity_grade;
         private TextView mQuantity_1;
         private LinearLayout mQuantity_earn;

         @Override public void onCallback(UserInfo callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {

               userinfo = callback.getUserinfo();
               //模糊背景
            /*   ImageLoader.getInstance()
                   .displayImage(userinfo.getFace(), blur_bg_iv, new ImageLoadingListener() {
                      @Override public void onLoadingStarted(String s, View view) {

                      }

                      @Override
                      public void onLoadingFailed(String s, View view, FailReason failReason) {

                      }

                      @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                         if (bitmap == null) {
                            return;
                         }
                         blur_bg_iv.setImageBitmap(FastBlurUtil.toBlur(bitmap, 10));
                      }

                      @Override public void onLoadingCancelled(String s, View view) {

                      }
                   });*/

               AULiveApplication.setUserInfo(userinfo);

               //保存微信公众号绑定状态
               SharedPreferenceTool.getInstance().saveString(SharedPreferenceTool.BIND_WX_PUBLIC, userinfo.getWx_bindid());

               DisplayImageOptions options =
                   new DisplayImageOptions.Builder().showStubImage(R.drawable.face_male)
                       .showImageForEmptyUri(R.drawable.face_male)
                       .showImageOnFail(R.drawable.face_male)
                       .cacheInMemory()
                       .cacheOnDisc()
                       .build();

               if (userinfo != null) {
                  // http://img.yuanphone.com/http://img.yuanphone.com/face/238/12526574.jpg
                  String face = userinfo.getFace();
                  face = Utils.getImgUrl(face);
                  ImageLoader.getInstance().displayImage(face, my_image, options);

                  if (face != null) {
                     my_image.setOnClickListener(new OnClickListener() {
                        @Override public void onClick(View view) {
                           String face = userinfo.getFace();
                           face = Utils.getImgUrl(face);
                           String[] urls = { face };
                           Intent intent = new Intent(MyUserInfoFragment.this.getActivity(),
                               PicBrowseActivity.class);
                           intent.putExtra(PicBrowseActivity.INTENT_BROWSE_POS_KEY, 0);
                           intent.putExtra(PicBrowseActivity.INTENT_BROWSE_LST_KEY, urls);
                           MyUserInfoFragment.this.getActivity().startActivity(intent);
                        }
                     });
                  }

                  TextView name = (TextView) root_view.findViewById(R.id.id_name);
                  name.setText(userinfo.getNickname());
                  TextView id = (TextView) root_view.findViewById(R.id.id_value);
                  id.setText("ID:" + userinfo.getUid());

                  if (userinfo.goodid != 0) {
                     id.setText("ID:" + userinfo.goodid);
                     id.setTextColor(AULiveApplication.mContext.getResources()
                         .getColor(R.color.btn_bottom_send_msg_c));
                  } else {
                     id.setTextColor(
                         AULiveApplication.mContext.getResources().getColor(R.color.white));
                  }

                  //等级
                  TextView tv_grade = (TextView) root_view.findViewById(R.id.grade_tv);
                  RelativeLayout grade_ly = (RelativeLayout) root_view.findViewById(R.id.grade_ly);
                  String grade = userinfo.getGrade();
                  ChatMsgListAdapter.setGradeIcon(grade, tv_grade, grade_ly);

                  //官方标志
                  if (userinfo.offical == 1) {
                     root_view.findViewById(R.id.offical_tag_iv).setVisibility(View.VISIBLE);
                  } else {
                     root_view.findViewById(R.id.offical_tag_iv).setVisibility(View.GONE);
                  }
         ///////////////////////////////////////////////////////////
                  ArrayList<String> tem_urls = new ArrayList<String>();
                  //先添加主播勋章
                  if (userinfo.anchor_medal != null) {
                     tem_urls.addAll(userinfo.anchor_medal);
                  }
                  //再添加玩家勋章
                  if (userinfo.wanjia_medal != null) {
                     tem_urls.addAll(userinfo.wanjia_medal);
                  }
                  //把所有的子view隐藏
                 LinearLayout chat_medal_ly=(LinearLayout)   root_view.findViewById(R.id.chat_medal_ly);
                  for (int i = 0; i < chat_medal_ly.getChildCount(); i++) {
                     chat_medal_ly.getChildAt(i).setVisibility(View.GONE);
                  }

                  for (int i = 0; i < chat_medal_ly.getChildCount() && i < tem_urls.size(); i++) {
                     String url = tem_urls.get(i);
                     MedalLayoutHelper.showGifImage(url, MyUserInfoFragment.this.getActivity(), chat_medal_ly.getChildAt(i));
                  }
         ///////////////////////////////////////////////////////////
                  // 设置性别
                  ImageView sex_iv = (ImageView) root_view.findViewById(R.id.sex_iv);
                  int sex = userinfo.getSex();
                  if (sex == 1) {
                     sex_iv.setImageResource(R.drawable.userinfo_male);
                  } else {
                     sex_iv.setImageResource(R.drawable.userinfo_female);
                  }
                  //认证
                  TextView txt_verify_reason =
                      (TextView) root_view.findViewById(R.id.txt_verify_reason);
                  if (userinfo.tag == null || userinfo.tag.equals("")) {
                     txt_verify_reason.setVisibility(View.GONE);
                  } else {
                     txt_verify_reason.setText("认证:" + userinfo.tag);
                  }

                  //签名
                  TextView txt_desc = (TextView) root_view.findViewById(R.id.txt_desc);

                  if (userinfo.signature == null || userinfo.signature.equals("")) {
                     txt_desc.setText("Ta好像忘记写签名了...");
                  } else {
                     txt_desc.setText(userinfo.signature);
                  }
                  //送出金币
                  TextView txt_account_inout =
                      (TextView) root_view.findViewById(R.id.txt_account_inout);
                  txt_account_inout.setText("送出:" + userinfo.send_diamond + "钻");

                  //贡献榜处理
                  int size = 0;
                  if (userinfo.tops != null) {
                     ImageView piaopiap_top1 =
                         (ImageView) root_view.findViewById(R.id.piaopiap_top1);

                     if (userinfo.tops.size() > 0) {
                        ImageLoader.getInstance()
                            .displayImage(userinfo.tops.get(0).getFace(), piaopiap_top1, options);
                        piaopiap_top1.setOnClickListener(new OnClickListener() {
                           @Override public void onClick(View view) {

                              Intent homepage_Intent =
                                  new Intent(MyUserInfoFragment.this.getActivity(),
                                      HomePageActivity.class);
                              homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID,
                                  userinfo.tops.get(0).getUid());
                              MyUserInfoFragment.this.getActivity().startActivity(homepage_Intent);
                           }
                        });
                     } else {
                        piaopiap_top1.setImageResource(R.drawable.me_qiuzhan);
                        piaopiap_top1.setOnClickListener(new OnClickListener() {
                           @Override public void onClick(View v) {

                           }
                        });
                     }
                     if (userinfo != null) {
                        mQuantity_earn = (LinearLayout) root_view.findViewById(listItems[0]);
                        mQuantity_1 = (TextView) mQuantity_earn.findViewById(R.id.quantity);
                        mQuantity_1.setText("" + (userinfo.recv_diamond - Integer.parseInt(userinfo.getExchange_diamond())));
//                        mQuantity_grade = (LinearLayout) root_view.findViewById(listItems[1]);
//                        mQuantity_2 = (TextView) mQuantity_grade.findViewById(R.id.quantity);
//                        mQuantity_2.setText(userinfo.getGrade());
                        mQuantity_diamond = (LinearLayout) root_view.findViewById(listItems[1]);
                        mQuantity_3 = (TextView) mQuantity_diamond.findViewById(R.id.quantity);
                        mQuantity_3.setText("" + userinfo.diamond);
                        mGuanzhuCountTv.setText("" + userinfo.atten_total);
                        mFansCountTv.setText("" + userinfo.fans_total);
                     }

                     ImageView piaopiap_top2 =
                         (ImageView) root_view.findViewById(R.id.piaopiap_top2);
                     if (userinfo.tops.size() > 1) {
                        ImageLoader.getInstance()
                            .displayImage(userinfo.tops.get(1).getFace(), piaopiap_top2, options);
                        piaopiap_top2.setOnClickListener(new OnClickListener() {
                           @Override public void onClick(View view) {
                              Intent homepage_Intent =
                                  new Intent(MyUserInfoFragment.this.getActivity(),
                                      HomePageActivity.class);
                              homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID,
                                  userinfo.tops.get(1).getUid());
                              MyUserInfoFragment.this.getActivity().startActivity(homepage_Intent);
                           }
                        });
                     } else {
                        piaopiap_top2.setImageResource(R.drawable.me_qiuzhan);
                        piaopiap_top2.setOnClickListener(new OnClickListener() {
                           @Override public void onClick(View v) {

                           }
                        });
                     }

                     ImageView piaopiap_top3 =
                         (ImageView) root_view.findViewById(R.id.piaopiap_top3);
                     if (userinfo.tops.size() > 2) {
                        ImageLoader.getInstance()
                            .displayImage(userinfo.tops.get(2).getFace(), piaopiap_top3, options);
                        piaopiap_top3.setOnClickListener(new OnClickListener() {
                           @Override public void onClick(View view) {
                              Intent homepage_Intent =
                                  new Intent(MyUserInfoFragment.this.getActivity(),
                                      HomePageActivity.class);
                              homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID,
                                  userinfo.tops.get(2).getUid());
                              MyUserInfoFragment.this.getActivity().startActivity(homepage_Intent);
                           }
                        });
                     } else {
                        piaopiap_top3.setImageResource(R.drawable.me_qiuzhan);
                        piaopiap_top3.setOnClickListener(new OnClickListener() {
                           @Override public void onClick(View v) {

                           }
                        });
                     }

                     TextView tv_piaopiapTop =
                         (TextView) root_view.findViewById(R.id.tv_piaopiapTop);
                     tv_piaopiapTop.setOnClickListener(new OnClickListener() {
                        @Override public void onClick(View view) {
                           //贡献榜
                           Intent top_rank = new Intent(MyUserInfoFragment.this.getActivity(),
                               TopRankActivity.class).putExtra(TopRankActivity.MemberInfo_key,
                               userinfo.getUid());
                           startActivity(top_rank);
                        }
                     });
                  }

                  //关注
                  root_view.findViewById(R.id.btn_followings)
                      .setOnClickListener(new OnClickListener() {
                         @Override public void onClick(View view) {
                            Intent top_rank = new Intent(MyUserInfoFragment.this.getActivity(),
                                AttenListActivity.class);
                            startActivity(top_rank);
                         }
                      });
                  //粉丝
                  root_view.findViewById(R.id.btn_fans).setOnClickListener(new OnClickListener() {
                     @Override public void onClick(View view) {
                        Intent top_rank = new Intent(MyUserInfoFragment.this.getActivity(),
                            FansListActivity.class);
                        startActivity(top_rank);
                     }
                  });
                  //
                  root_view.findViewById(R.id.btn_lives).setOnClickListener(new OnClickListener() {
                     @Override public void onClick(View view) {
                        Intent top_rank = new Intent(MyUserInfoFragment.this.getActivity(),
                            LivesListActivity.class);
                        startActivity(top_rank);
                     }
                  });
               } else {
                  ImageLoader.getInstance().displayImage(null, my_image, options);
                  TextView name = (TextView) root_view.findViewById(R.id.id_name);
                  name.setText("");
                  TextView id = (TextView) root_view.findViewById(R.id.id_value);
                  id.setText("ID:" + "");
               }
            } else {

            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(UserInfo.class));
      request.execute();
   }
}
